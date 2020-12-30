package com.bignerdranch.android.yelpsbusinessesweather

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageButton
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
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
import com.bignerdranch.android.yelpsbusinessesweather.viewmodel.YelpViewModelFactory
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import kotlinx.coroutines.launch
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL


private const val API_KEY = "fIK9HGPtNk-VJEAjIM4YyP0sRdeIpG82w6dnYVw_KsVz5c4RT54du50UT5uDakogcu8ism-9EeiEBc9Ca1014bzMMIejU6neWdmo3Zc6NePREOjcoY2XJ_p8SkTaX3Yx"
private const val LATLNG_KEY = "LatLng"
private const val TYPE_KEY = "Type"


class MapFragment : Fragment() {

    private lateinit var  viewModelFactory : YelpViewModelFactory
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

        viewModelFactory = YelpViewModelFactory(ServiceLocator.repository)
        viewModel= ViewModelProvider(this,viewModelFactory).get(YelpViewModel::class.java)

        viewModel.readAllBusinesses.observe(viewLifecycleOwner, Observer {

            googleMap.clear()

            val boundsBuilder = LatLngBounds.Builder()

            it.forEach {yelp->

                val latLng = LatLng(yelp.coordinates.latitude,yelp.coordinates.longitude)
                boundsBuilder.include(latLng)
                googleMap.addMarker(MarkerOptions()
                        .position(latLng)
                        .title(yelp.name)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET))
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
                viewModel.getYelpBusinesses("Bearer $API_KEY", it.latitude.toString()
                    ,it.longitude.toString()).observe(viewLifecycleOwner,
                    Observer {
                        Log.i("MainActivity","$it")
                        googleMap.clear()
                    })
            }else{
                    Toast.makeText(requireContext(),"there is no internet connection",
                        Toast.LENGTH_SHORT).show()
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
            val connectivityManager = context?.
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val imageButton = view.findViewById<ImageButton>(R.id.image_type)

            fun bind(currentType: Type){
                imageButton.setImageResource(types[position].imageResourceId)

                imageButton.setOnClickListener {
                    val activeNetworkInfo = connectivityManager.activeNetworkInfo
                    if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {

                    lifecycleScope.launch {

                        dataStoreProvider.save(TYPE_KEY, currentType.type)


                        val latLng = dataStoreProvider.read(LATLNG_KEY) ?: "0.0,0.0"
                        val list = latLng.split(",")
                        Log.i(LATLNG_KEY, latLng)
                        viewModel.getYelpBusinessesByCategory("Bearer $API_KEY",
                            currentType.type, list[0], list[1]).observe(viewLifecycleOwner,
                                Observer {
                                    Log.i("MainActivity", "$it")
                                })
                    }
                    }else{
                        Toast.makeText(requireContext(),"there is no internet connection",
                            Toast.LENGTH_SHORT).show()
                    }

                }
            }

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TypeViewHolder {
            val view : View = LayoutInflater.from(parent.context).inflate(R.layout.type_item,parent,
                false)
            return TypeViewHolder(view)
        }

        override fun getItemCount(): Int {
            return types.size
        }

        override fun onBindViewHolder(holder: TypeViewHolder, position: Int) {
            holder.bind(types[position])

        }


    }








}