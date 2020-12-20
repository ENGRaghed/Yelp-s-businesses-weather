package com.bignerdranch.android.yelpsbusinessesweather

import androidx.fragment.app.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

private const val API_KEY = "fIK9HGPtNk-VJEAjIM4YyP0sRdeIpG82w6dnYVw_KsVz5c4RT54du50UT5uDakogcu8ism-9EeiEBc9Ca1014bzMMIejU6neWdmo3Zc6NePREOjcoY2XJ_p8SkTaX3Yx"

class MapFragment : Fragment() {

    private lateinit var  viewModel : YelpViewModel

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

        val sydney = LatLng(-34.0, 151.0)
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))



        viewModel= ViewModelProvider(this).get(YelpViewModel::class.java)

        googleMap.setOnMapClickListener {
            //"40.814564", "-74.220654"
            viewModel.getYelpBusinesses("Bearer $API_KEY", it.latitude.toString(),it.longitude.toString()).observe(viewLifecycleOwner,
                Observer {
                    Log.i("MainActivity","$it")
                    val boundsBuilder = LatLngBounds.Builder()

                    it.forEach {
                        val latLng = LatLng(it.coordinates.latitude,it.coordinates.longitude)
//                    googleMap.addMarker(MarkerOptions().position(latLng).title("Marker in Sydney"))
//                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))

                        boundsBuilder.include(latLng)
                        googleMap.addMarker(MarkerOptions().position(latLng).title("${it.name}")).tag = it
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(boundsBuilder.build(), 1000, 1000, 0))

                    }
                })
        }

//        viewModel.getYelpBusinesses("Bearer $API_KEY", "40.814564", "-74.220654").observe(viewLifecycleOwner,
//            Observer {
//                Log.i("MainActivity","$it")
//                val boundsBuilder = LatLngBounds.Builder()
//
//                it.forEach {
//                    val latLng = LatLng(it.coordinates.latitude,it.coordinates.longitude)
////                    googleMap.addMarker(MarkerOptions().position(latLng).title("Marker in Sydney"))
////                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
//
//                    boundsBuilder.include(latLng)
//                    googleMap.addMarker(MarkerOptions().position(latLng).title("${it.name}")).tag = it
//                    googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(boundsBuilder.build(), 1000, 1000, 0))
//
//                }
//            })

        googleMap.setOnMarkerClickListener(object : GoogleMap.OnMarkerClickListener{
            override fun onMarkerClick(p0: Marker?): Boolean {
                if (p0?.tag is YelpRestaurant ){
                    val action = MapFragmentDirections.actionMapFragmentToDetailsFragment(p0.tag as YelpRestaurant)
                    findNavController().navigate(action)
                }
                return false
            }

        })

//        val sydney = LatLng(-34.0, 151.0)
//        googleMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
//        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }
}