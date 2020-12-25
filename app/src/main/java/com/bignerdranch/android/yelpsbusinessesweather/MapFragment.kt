package com.bignerdranch.android.yelpsbusinessesweather

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.yelpsbusinessesweather.model.Type
import com.bignerdranch.android.yelpsbusinessesweather.model.YelpRestaurant
import com.bignerdranch.android.yelpsbusinessesweather.viewmodel.YelpViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import kotlinx.coroutines.launch


private const val API_KEY = "fIK9HGPtNk-VJEAjIM4YyP0sRdeIpG82w6dnYVw_KsVz5c4RT54du50UT5uDakogcu8ism-9EeiEBc9Ca1014bzMMIejU6neWdmo3Zc6NePREOjcoY2XJ_p8SkTaX3Yx"
private const val LATLNG_KEY = "LatLng"
private const val TYPE_KEY = "Type"


class MapFragment : Fragment() {

    private lateinit var  viewModel : YelpViewModel
    private lateinit var latLng: LatLng
    private lateinit var adapter: TypeAdapter
    private lateinit var dataStoreProvider: DataStoreProvider

    private val callback = OnMapReadyCallback { googleMap ->
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */

        dataStoreProvider = DataStoreProvider(requireContext())

        val connectivityManager = context?.
        getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager


        val sydney = LatLng(-34.0, 151.0)
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))

        viewModel= ViewModelProvider(this).get(YelpViewModel::class.java)

        viewModel.readAllBusinesses.observe(viewLifecycleOwner, Observer {
            googleMap.clear()

            val boundsBuilder = LatLngBounds.Builder()

            it.forEach {yelp->
                val bm: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.avatar)

                val latLng = LatLng(yelp.coordinates.latitude,yelp.coordinates.longitude)
                boundsBuilder.include(latLng)
                googleMap.addMarker(MarkerOptions()
                        .position(latLng)
                        .title(yelp.name)
//                        .snippet("Population: 4,137,400")
//                        .icon(BitmapDescriptorFactory.fromBitmap(customMarker(bm)))
                ).tag = yelp

                googleMap.moveCamera(CameraUpdateFactory
                        .newLatLngBounds(boundsBuilder.build(), 1000, 1000, 0))
            }

        })

        googleMap.setOnMarkerClickListener(object : GoogleMap.OnMarkerClickListener{
            override fun onMarkerClick(p0: Marker?): Boolean {
                if (p0?.tag is YelpRestaurant){
                    val action = MapFragmentDirections
                        .actionMapFragmentToDetailsFragment(p0.tag as YelpRestaurant)
                    findNavController().navigate(action)
                }
                return false
            }

        })




            googleMap.setOnMapClickListener {
                //"40.814564", "-74.220654"
                val activeNetworkInfo = connectivityManager.activeNetworkInfo
                if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {


                    setLatLng(it)

                lifecycleScope.launch {
                    dataStoreProvider.save(LATLNG_KEY,"${it.latitude},${it.longitude}")
                    Log.i(LATLNG_KEY,dataStoreProvider.read(LATLNG_KEY)?:"")
                    dataStoreProvider.save(TYPE_KEY,"")
                }
                viewModel.getYelpBusinesses("Bearer $API_KEY", it.latitude.toString(),it.longitude.toString()).observe(viewLifecycleOwner,
                    Observer {
                        Log.i("MainActivity","$it")
                        googleMap.clear()
                    })
            }else{
                    Toast.makeText(requireContext(),"there is no internet connection",Toast.LENGTH_SHORT).show()
                }
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view=inflater.inflate(R.layout.fragment_map, container, false)

        val TypesList = ArrayList<Type>()
        TypesList.add(Type("coffee",R.drawable.ic_cafe))
        TypesList.add(Type("art",R.drawable.ic_art))
        TypesList.add(Type("fastfood",R.drawable.ic_baseline_fastfood_24))
        TypesList.add(Type("library",R.drawable.ic_library))
        TypesList.add(Type("resturent",R.drawable.ic_restaurant))
        TypesList.add(Type("Museums",R.drawable.ic_museum))
        TypesList.add(Type("gym",R.drawable.ic_sports))

        val dayPlansButton = view.findViewById<Button>(R.id.plan_button)
        dayPlansButton.setOnClickListener {
            val action = MapFragmentDirections.actionMapFragmentToDayPlansListFragment()
            findNavController().navigate(action)
        }


        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        adapter = TypeAdapter(TypesList)
        val mLayoutManager = LinearLayoutManager(context)
        mLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        recyclerView.layoutManager = mLayoutManager
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.adapter = adapter


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map)
                as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    private fun setLatLng(latLng: LatLng){
        this.latLng = latLng
    }


    inner class TypeAdapter( private var types : List<Type>) :
            RecyclerView.Adapter<TypeAdapter.TypeViewHolder>(){

        inner class TypeViewHolder (view : View) : RecyclerView.ViewHolder(view){

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TypeViewHolder {
            val view : View = LayoutInflater.from(parent.context).inflate(R.layout.type_item,parent,false)
            return TypeViewHolder(view)
        }

        override fun getItemCount(): Int {
            return types.size
        }

        override fun onBindViewHolder(holder: TypeViewHolder, position: Int) {
            val imageButton = holder.itemView.findViewById<ImageButton>(R.id.image_type)
            imageButton.setImageResource(types[position].imageResourceId)

            imageButton.setOnClickListener {


//                imageButton.setBackgroundColor(Color.parseColor("#4CB8FB"))
//                imageButton.background = resources.getDrawable(R.drawable.background_select)
//                imageButton.background = resources.getDrawable(R.drawable.background_them)

                lifecycleScope.launch {
//                    if (dataStoreProvider.read(TYPE_KEY).equals(types[position].type)){
//                       imageButton.setBackgroundColor(Color.parseColor("#4CB8FB"))
//                    }
                    dataStoreProvider.save(TYPE_KEY,types[position].type)

                    if (dataStoreProvider.read(TYPE_KEY).equals(types[position].type)){
                        imageButton.background = resources.getDrawable(R.drawable.background_select)
                    }else {
                        imageButton.background = resources.getDrawable(R.drawable.background_them)
                    }
                    val latLng = dataStoreProvider.read(LATLNG_KEY)?:"0.0,0.0"
                           val list = latLng.split(",")
                    Log.i(LATLNG_KEY,latLng)
                    viewModel.getYelpBusinessesByCategory("Bearer $API_KEY", types[position].type,list[0],list[1]).observe(viewLifecycleOwner,
                            Observer {
                                Log.i("MainActivity","$it")
                            })
                }
//
//                lifecycleScope.launch {
//                    if (dataStoreProvider.read(TYPE_KEY).equals(types[position].type)){
//                        imageButton.background = resources.getDrawable(R.drawable.background_select)
//                    }else {
//                        imageButton.background = resources.getDrawable(R.drawable.background_them)
//                    }
//                }





            }
        }


    }

    private fun bitmapDescriptorFromVector(context: Context, @DrawableRes vectorDrawableResourceId: Int): BitmapDescriptor? {
        val background: Drawable = ContextCompat.getDrawable(context, R.drawable.background_select)!!
        background.setBounds(0, 0, background.getIntrinsicWidth(), background.getIntrinsicHeight())
        val vectorDrawable: Drawable = ContextCompat.getDrawable(context, vectorDrawableResourceId)!!
        vectorDrawable.setBounds(40, 20, vectorDrawable.getIntrinsicWidth() + 40, vectorDrawable.getIntrinsicHeight() + 20)
        val bitmap: Bitmap = Bitmap.createBitmap(background.getIntrinsicWidth(), background.getIntrinsicHeight(), Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        background.draw(canvas)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    private fun customMarker(bitmap: Bitmap,text:String="0"):Bitmap {
        /*

         */
        var result: Bitmap? = null
        try {
            result = Bitmap.createBitmap(dp(62F), dp(76F), Bitmap.Config.ARGB_8888)
            result.eraseColor(Color.TRANSPARENT)
            val canvas = Canvas(result)
            val drawable = resources.getDrawable(R.drawable.ic_matker)
            drawable.setBounds(0, 0, dp(50F), dp(50F))
            drawable.draw(canvas)
            val roundPaint = Paint(Paint.ANTI_ALIAS_FLAG)
            val bitmapRect = RectF()
            canvas.save()
            val shader = BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
            val matrix = Matrix()
            val scale = dp(40F) / bitmap.width.toFloat()
            matrix.postTranslate(dp(5F).toFloat(), dp(5F).toFloat())
            matrix.postScale(scale, scale)
            roundPaint.shader = shader
            shader.setLocalMatrix(matrix)
            bitmapRect[dp(5F).toFloat(), dp(5F).toFloat(), dp((52 + 5).toFloat()).toFloat()] =
                    dp((52 + 5).toFloat()).toFloat()
            canvas.drawRoundRect(bitmapRect, dp(26F).toFloat(), dp(26F).toFloat(), roundPaint)
            val paint = Paint()
            paint.color = Color.WHITE
            paint.textSize = dp(30F).toFloat()
            paint.setShadowLayer(1f, 0f, 1f, Color.BLACK)

                canvas.drawText("", dp(32F/2).toFloat(), dp(76F/2).toFloat(), paint)
            canvas.restore()
            try {
                canvas.setBitmap(null)
            } catch (e: java.lang.Exception) {
            }
        } catch (t: Throwable) {
            t.printStackTrace()
        }
        return result!!
    }

    fun dp(value: Float): Int {
        return if (value == 0f) {
            0
        } else Math.ceil(resources.displayMetrics.density * value.toDouble()).toInt()
    }



}