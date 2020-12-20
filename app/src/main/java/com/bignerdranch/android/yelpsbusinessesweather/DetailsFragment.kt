package com.bignerdranch.android.yelpsbusinessesweather

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.squareup.picasso.Picasso

private const val API_KEY = "843eaebc6a294b4593b190359201612"

class DetailsFragment : BottomSheetDialogFragment() {

    private lateinit var  viewModel : YelpViewModel

    private val args by navArgs<DetailsFragmentArgs>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_details, container, false)

        viewModel= ViewModelProvider(this).get(YelpViewModel::class.java)


        val name= view.findViewById<TextView>(R.id.tvName)
        val image = view.findViewById<ImageView>(R.id.imageView)
        val ratingBar = view.findViewById<RatingBar>(R.id.ratingBar)
        val tvNumReviews = view.findViewById<TextView>(R.id.tvNumReviews)
        val address = view.findViewById<TextView>(R.id.tvAddress)
        val Category = view.findViewById<TextView>(R.id.tvCategory)
        val Distance = view.findViewById<TextView>(R.id.tvDistance)
        val tvPrice = view.findViewById<TextView>(R.id.tvPrice)

        viewModel.getCurrentWeather(API_KEY,
            "${args.yelp.coordinates.latitude},${args.yelp.coordinates.longitude}",
            "5").observe(viewLifecycleOwner, Observer {
            Distance.text = "${it.current.temp_c}°C"
        })

        name.text = args.yelp.name
        ratingBar.rating = args.yelp.rating.toFloat()
        tvNumReviews.text = "${args.yelp.numReviews} Reviews"
        address.text = args.yelp.location.address
        Category.text = args.yelp.categories[0].title
        tvPrice.text = args.yelp.price
        Picasso.get().load(args.yelp.imageUrl).into(image)




        return view
    }

}