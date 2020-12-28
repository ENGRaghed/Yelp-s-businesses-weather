package com.bignerdranch.android.yelpsbusinessesweather

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.yelpsbusinessesweather.model.DayPlan
import com.bignerdranch.android.yelpsbusinessesweather.model.Hour
import com.bignerdranch.android.yelpsbusinessesweather.viewmodel.DayPlanViewModel
import com.bignerdranch.android.yelpsbusinessesweather.viewmodel.YelpViewModel
import com.bignerdranch.android.yelpsbusinessesweather.viewmodel.YelpViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.hour_item.view.*

private const val API_KEY = "843eaebc6a294b4593b190359201612"

class DetailsFragment : BottomSheetDialogFragment() {

    private lateinit var  viewModelFactory : YelpViewModelFactory
    private lateinit var  viewModel : YelpViewModel
//    private lateinit var  dayPlanViewModel : DayPlanViewModel
    private lateinit var adapter: ForecastHourAdapter


    private val args by navArgs<DetailsFragmentArgs>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_details, container, false)

        viewModelFactory = YelpViewModelFactory(ServiceLocator.repository)
        viewModel= ViewModelProvider(this,viewModelFactory).get(YelpViewModel::class.java)
//        dayPlanViewModel = ViewModelProvider(this).get(DayPlanViewModel::class.java)

        val recyclerView = view.findViewById<RecyclerView>(R.id.rv_hours)
        adapter = ForecastHourAdapter()
        val mLayoutManager = LinearLayoutManager(context)
        mLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        recyclerView.layoutManager = mLayoutManager
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.adapter = adapter


        val name= view.findViewById<TextView>(R.id.tvName)
        val image = view.findViewById<ImageView>(R.id.imageView)
        val ratingBar = view.findViewById<RatingBar>(R.id.ratingBar)
        val tvNumReviews = view.findViewById<TextView>(R.id.tvNumReviews)
        val address = view.findViewById<TextView>(R.id.tvAddress)
        val Category = view.findViewById<TextView>(R.id.tvCategory)
        val Distance = view.findViewById<TextView>(R.id.tvDistance)
        val tvPrice = view.findViewById<TextView>(R.id.tvPrice)
        val day_1_temp = view.findViewById<TextView>(R.id.day_1)
        val day_2_temp = view.findViewById<TextView>(R.id.day_2)
        val day_3_temp = view.findViewById<TextView>(R.id.day_3)
        val imageState1 = view.findViewById<ImageView>(R.id.state_1)
        val imageState2 = view.findViewById<ImageView>(R.id.state_2)
        val imageState3 = view.findViewById<ImageView>(R.id.state_3)
        val addDayPlanButton = view.findViewById<Button>(R.id.add_day_plan)

        addDayPlanButton.setOnClickListener {
            val action = DetailsFragmentDirections
                .actionDetailsFragmentToAddDayPlanFragment(
                    args.yelp.yelpId.toString(),
                    "${args.yelp.coordinates.latitude},${args.yelp.coordinates.longitude}")
            findNavController().navigate(action)
        }




        viewModel.getCurrentWeather(API_KEY,
            "${args.yelp.coordinates.latitude},${args.yelp.coordinates.longitude}",
            "5").observe(viewLifecycleOwner, Observer {

            adapter.setData(it.forecast.forecastday[0].hour)
            Distance.text = "${it.current.temp_c}°C"
            day_1_temp.text = "${it.forecast.forecastday[0].day.avgtemp_c}°C"
            day_2_temp.text = "${it.forecast.forecastday[1].day.avgtemp_c}°C"
            day_3_temp.text = "${it.forecast.forecastday[2].day.avgtemp_c}°C"
            Picasso.get().load("https://${it.forecast.forecastday[0].day.condition.icon}")
                    .placeholder(R.drawable.wind).error(R.drawable.sunrise).into(imageState1)
            Log.i("image1","https://${it.forecast.forecastday[0].day.condition.icon}")

            Picasso.get().load("https://${it.forecast.forecastday[1].day.condition.icon}")
                    .placeholder(R.drawable.wind).error(R.drawable.sunrise).into(imageState2)
            Log.i("image2","https://${it.forecast.forecastday[1].day.condition.icon}")

            Picasso.get().load("https://${it.forecast.forecastday[2].day.condition.icon}")
                    .placeholder(R.drawable.wind).error(R.drawable.sunrise).into(imageState3)
            Log.i("image3","https://${it.forecast.forecastday[2].day.condition.icon}")






        })

        name.text = args.yelp.name?:""
        ratingBar.rating = args.yelp.rating.toFloat()
        tvNumReviews.text = "${args.yelp.numReviews} Reviews"
        address.text = args.yelp.location.address
        Category.text = args.yelp.categories[0].title
        tvPrice.text = args.yelp.price
        Picasso.get().load(args.yelp.imageUrl).placeholder(R.drawable.wind).error(R.drawable.sunrise).into(image)
        Log.i("yelp_image",args.yelp.imageUrl)




        return view
    }

    inner class ForecastHourAdapter() :
        RecyclerView.Adapter<ForecastHourAdapter.ForecastHourViewHolder>() {

        lateinit var hour: Hour
        var hours = emptyList<Hour>()

        inner class ForecastHourViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastHourViewHolder {
            val view : View = LayoutInflater.from(parent.context).inflate(R.layout.hour_item,parent,false)
            return ForecastHourViewHolder(view)
        }

        override fun getItemCount(): Int {
            return hours.size
        }

        override fun onBindViewHolder(holder: ForecastHourViewHolder, position: Int) {
            hour = hours[position]
            if (position < 12){
                holder.itemView.time_hour.text = "$position am"
            }else{
                holder.itemView.time_hour.text = "$position pm"

            }
            Picasso.get().load("https://${hour.condition.icon}")
                .placeholder(R.drawable.wind).error(R.drawable.sunrise).into(holder.itemView.imageView3)
            holder.itemView.temp_c.text = "${hour.temp_c}°C"

        }
        fun setData(hours: List<Hour>){
            this.hours = hours
            notifyDataSetChanged()
        }

    }


}