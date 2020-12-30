package com.bignerdranch.android.yelpsbusinessesweather

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bignerdranch.android.yelpsbusinessesweather.model.DayPlan
import com.bignerdranch.android.yelpsbusinessesweather.model.YelpLocation
import com.bignerdranch.android.yelpsbusinessesweather.model.YelpRestaurant
import com.bignerdranch.android.yelpsbusinessesweather.viewmodel.DayPlanViewModel
import com.bignerdranch.android.yelpsbusinessesweather.viewmodel.DayPlanViewModelFactory
import com.bignerdranch.android.yelpsbusinessesweather.viewmodel.YelpViewModel
import com.bignerdranch.android.yelpsbusinessesweather.viewmodel.YelpViewModelFactory
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_add_day_plan.*
import java.text.SimpleDateFormat
import java.util.*

private const val DIALOG_DATE = "DialogDate"
private const val REQUEST_DATE = 0
class AddDayPlanFragment : Fragment(),DatePickerFragment.Callbacks {

    private val args by navArgs<AddDayPlanFragmentArgs>()


    private lateinit var yelpViewModel: YelpViewModel
    private lateinit var yelpViewModelFactory: YelpViewModelFactory
    private lateinit var dayPlanViewModel: DayPlanViewModel
    private lateinit var dayPlanViewModelFactory: DayPlanViewModelFactory

    lateinit var businesse : YelpRestaurant
    private var date : Date? =null

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
        val location = args.latLng.split(",")
        val latLng = LatLng(location[0].toDouble(),location[1].toDouble())
        val boundsBuilder = LatLngBounds.Builder()
        boundsBuilder.include(latLng)
        googleMap.addMarker(MarkerOptions().position(latLng)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)))
        googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(boundsBuilder.build(),2000,2000,0))

    }


        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add_day_plan, container, false)


            yelpViewModelFactory = YelpViewModelFactory(ServiceLocator.repository)
            yelpViewModel = ViewModelProvider(this,yelpViewModelFactory).get(YelpViewModel::class.java)
            

            dayPlanViewModelFactory = DayPlanViewModelFactory(ServiceLocator.dayPlanRepository)
            dayPlanViewModel = ViewModelProvider(this,dayPlanViewModelFactory)
                .get(DayPlanViewModel::class.java)

            val image = view.findViewById<ImageView>(R.id.businesse_image)
            val datePicker = view.findViewById<FloatingActionButton>(R.id.date_picker)
            val name = view.findViewById<TextView>(R.id.businesse_name)
            val addButton = view.findViewById<FloatingActionButton>(R.id.add_day_plan_button)
            val ratingBar = view.findViewById<RatingBar>(R.id.ratingBar2)
            val descriptionTv = view.findViewById<EditText>(R.id.description)


            val connectivityManager = context?.
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetworkInfo = connectivityManager.activeNetworkInfo

            yelpViewModel.readBusinesse(args.dayPlanId).observe(viewLifecycleOwner, Observer {
            businesse = it

                if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {

                    if (it.imageUrl.isNotEmpty()) {
                        Picasso.get().load(it.imageUrl).fit().centerCrop().into(image)
                    }
                }

                name.text= it.name
                addButton.visibility = View.VISIBLE
                ratingBar.rating = it.rating.toFloat()



        })


        datePicker.setOnClickListener {
            DatePickerFragment.newInstance(Date()).apply {
                setTargetFragment(this@AddDayPlanFragment,REQUEST_DATE)
                show(this@AddDayPlanFragment.requireFragmentManager(), DIALOG_DATE)
            }
        }

        addButton.setOnClickListener {

            val description = descriptionTv.text.toString()
            dayPlanViewModel.addDayPlan(DayPlan(
                0,
                businesse.name,
                businesse.rating,
                businesse.price,
                businesse.numReviews,
                businesse.distanceInMeters,
                businesse.imageUrl,
                businesse.categories,
                businesse.location,
                businesse.coordinates,
            description,
                date
            ))
            Toast.makeText(requireContext(),"add ${businesse.name} successful",Toast.LENGTH_SHORT).show()

            val action = AddDayPlanFragmentDirections.actionAddDayPlanFragmentToDayPlansListFragment()
            findNavController().navigate(action)

        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.mapView)
                as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    override fun onDateSelected(date: Date) {
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
        val dateFormat = simpleDateFormat.format(date)
        date_Tv.text = dateFormat
        this.date = date
    }

}