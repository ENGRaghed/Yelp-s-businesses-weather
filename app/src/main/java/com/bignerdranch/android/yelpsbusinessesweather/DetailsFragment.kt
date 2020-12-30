package com.bignerdranch.android.yelpsbusinessesweather

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bignerdranch.android.yelpsbusinessesweather.model.DayPlan
import com.bignerdranch.android.yelpsbusinessesweather.model.Hour
import com.bignerdranch.android.yelpsbusinessesweather.viewmodel.DayPlanViewModel
import com.bignerdranch.android.yelpsbusinessesweather.viewmodel.YelpViewModel
import com.bignerdranch.android.yelpsbusinessesweather.viewmodel.YelpViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_details.view.*
import kotlinx.android.synthetic.main.hour_item.view.*
import kotlinx.android.synthetic.main.image_item.view.*
import java.text.SimpleDateFormat
import java.util.*

private const val API_KEY = "843eaebc6a294b4593b190359201612"
private const val API_KEY_YELP = "fIK9HGPtNk-VJEAjIM4YyP0sRdeIpG82w6dnYVw_KsVz5c4RT54du50UT5uDakogcu8ism-9EeiEBc9Ca1014bzMMIejU6neWdmo3Zc6NePREOjcoY2XJ_p8SkTaX3Yx"


class DetailsFragment : BottomSheetDialogFragment() {

    private lateinit var  viewModelFactory : YelpViewModelFactory
    private lateinit var  viewModel : YelpViewModel
    private lateinit var adapter: ForecastHourAdapter
    private lateinit var imageAdapter: ImageAdapter


    private val args by navArgs<DetailsFragmentArgs>()
    @SuppressLint("SetTextI18n")
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


        val recyclerViewPager2 = view.findViewById<ViewPager2>(R.id.imageView)
        imageAdapter = ImageAdapter()
        recyclerViewPager2.adapter = imageAdapter
        val list = mutableListOf<String>()
        list.add(args.yelp.imageUrl)

        imageAdapter.setData(list)

        val connectivityManager = context?.
        getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
            viewModel.getPhotos(args.yelp.id, "Bearer $API_KEY_YELP").observe(viewLifecycleOwner, Observer {
                Log.i("image_url", "$it")
                imageAdapter.setData(it)
            })
        }

        val name= view.findViewById<TextView>(R.id.tvName)
        val ratingBar = view.findViewById<RatingBar>(R.id.ratingBar)
        val tvNumReviews = view.findViewById<TextView>(R.id.tvNumReviews)
        val rain1 = view.findViewById<TextView>(R.id.rain1)
        val rain2 = view.findViewById<TextView>(R.id.rain2)
        val rain3 = view.findViewById<TextView>(R.id.rain3)
        val date1 = view.findViewById<TextView>(R.id.date1)
        val date2 = view.findViewById<TextView>(R.id.date2)
        val date3 = view.findViewById<TextView>(R.id.date3)
        val day_1_temp = view.findViewById<TextView>(R.id.min_tmp)
        val day_2_temp = view.findViewById<TextView>(R.id.min_tmp2)
        val day_3_temp = view.findViewById<TextView>(R.id.min_tmp3)
        val day_1_temp_max = view.findViewById<TextView>(R.id.max_tmp)
        val day_2_temp_max = view.findViewById<TextView>(R.id.max_tmp2)
        val day_3_temp_max = view.findViewById<TextView>(R.id.max_tmp3)
        val imageState1 = view.findViewById<ImageView>(R.id.state_1)
        val imageState2 = view.findViewById<ImageView>(R.id.state_2)
        val imageState3 = view.findViewById<ImageView>(R.id.state_3)
        val addDayPlanButton = view.findViewById<FloatingActionButton>(R.id.add_day_plan)
        val phone = view.findViewById<FloatingActionButton>(R.id.phone)

        addDayPlanButton.setOnClickListener {
            val action = DetailsFragmentDirections
                .actionDetailsFragmentToAddDayPlanFragment(
                    args.yelp.yelpId.toString(),
                    "${args.yelp.coordinates.latitude},${args.yelp.coordinates.longitude}")
            findNavController().navigate(action)
        }

        phone.setOnClickListener {
            var intent = Intent(Intent.ACTION_DIAL)
            intent.setData(Uri.parse("tel:"+args.yelp.phone))
            startActivity(intent)
        }


        val linearLayout = view.findViewById<LinearLayout>(R.id.linearLayout)
        if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {

            linearLayout.visibility = View.VISIBLE
            viewModel.getCurrentWeather(API_KEY,
                    "${args.yelp.coordinates.latitude},${args.yelp.coordinates.longitude}",
                    "5").observe(viewLifecycleOwner, Observer {


                adapter.setData(it.forecast.forecastday[0].hour)
                rain1.text = "${it.forecast.forecastday[0].day.daily_chance_of_rain}%"
                rain2.text = "${it.forecast.forecastday[1].day.daily_chance_of_rain}%"
                rain3.text = "${it.forecast.forecastday[2].day.daily_chance_of_rain}%"
                date1.text = it.forecast.forecastday[0].date
                date2.text = it.forecast.forecastday[1].date
                date3.text = it.forecast.forecastday[2].date
                day_1_temp.text = "${it.forecast.forecastday[0].day.avgtemp_c}°C"
                day_2_temp.text = "${it.forecast.forecastday[1].day.avgtemp_c}°C"
                day_3_temp.text = "${it.forecast.forecastday[2].day.avgtemp_c}°C"
                day_1_temp_max.text = "${it.forecast.forecastday[0].day.maxtemp_c}°C"
                day_2_temp_max.text = "${it.forecast.forecastday[1].day.maxtemp_c}°C"
                day_3_temp_max.text = "${it.forecast.forecastday[2].day.maxtemp_c}°C"

                if (it.forecast.forecastday[0].day.condition.icon.isNotEmpty()) {
                    Picasso.get().load("https://${it.forecast.forecastday[0].day.condition.icon}").fit().centerCrop()
                            .placeholder(R.drawable.wind).error(R.drawable.sunrise).into(imageState1)
                }

                if (it.forecast.forecastday[1].day.condition.icon.isNotEmpty()) {
                    Picasso.get().load("https://${it.forecast.forecastday[1].day.condition.icon}").fit().centerCrop()
                            .placeholder(R.drawable.wind).error(R.drawable.sunrise).into(imageState2)
                }

                if (it.forecast.forecastday[0].day.condition.icon.isNotEmpty()) {
                    Picasso.get().load("https://${it.forecast.forecastday[2].day.condition.icon}").fit().centerCrop()
                            .placeholder(R.drawable.wind).error(R.drawable.sunrise).into(imageState3)
                }

            })
        }

        name.text = args.yelp.name?:""
        ratingBar.rating = args.yelp.rating.toFloat()
        tvNumReviews.text = "${args.yelp.numReviews} Reviews"

        return view
    }

    inner class ForecastHourAdapter() :
        RecyclerView.Adapter<ForecastHourAdapter.ForecastHourViewHolder>() {

        lateinit var currentHour: Hour
        var hours = emptyList<Hour>()

        inner class ForecastHourViewHolder(view: View) : RecyclerView.ViewHolder(view) {

            val time_hour = view.findViewById<TextView>(R.id.time_hour)
            val temp_c = view.findViewById<TextView>(R.id.temp_c)
            val imageView = view.findViewById<ImageView>(R.id.imageView3)

            fun bind(hour : Hour){
                if (position < 12){
                   time_hour.text = "$position am"
                }else{
                    time_hour.text = "$position pm"

                }
                val connectivityManager = context?.
                getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                val activeNetworkInfo = connectivityManager.activeNetworkInfo
                if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
                    if (hour.condition.icon.isNotEmpty()){
                        Picasso.get().load("https://${hour.condition.icon}")
                                .placeholder(R.drawable.cloud).error(R.drawable.cloud).into(imageView)
                    }
                }

                temp_c.text = "${hour.temp_c}°C"

            }

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastHourViewHolder {
            val view : View = LayoutInflater.from(parent.context).inflate(R.layout.hour_item,parent,false)
            return ForecastHourViewHolder(view)
        }

        override fun getItemCount(): Int {
            return hours.size
        }

        override fun onBindViewHolder(holder: ForecastHourViewHolder, position: Int) {
            currentHour = hours[position]
            holder.bind(currentHour)


        }
        fun setData(hours: List<Hour>){
            this.hours = hours
            notifyDataSetChanged()
        }

    }

    inner class ImageAdapter():RecyclerView.Adapter<ImageAdapter.ImageViewHolder>(){
        var images = emptyList<String>()
        lateinit var image : String
        inner class ImageViewHolder(view: View):RecyclerView.ViewHolder(view){
            val imageView = view.findViewById<ImageView>(R.id.image_view_pager2)
            fun bind(currentImage : String){
                val connectivityManager = context?.
                getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                val activeNetworkInfo = connectivityManager.activeNetworkInfo
                if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
                    if (currentImage.isNotEmpty()) {
                        Picasso.get().load(image).fit().centerCrop().into(imageView)

                    }
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
            val view : View = LayoutInflater.from(parent.context).inflate(R.layout.image_item,parent,false)
            return ImageViewHolder(view)
        }

        override fun getItemCount(): Int {
            return images.size
        }

        override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
            image = images[position]
            holder.bind(image)
        }
        fun setData(images: List<String>){
            this.images = images
            notifyDataSetChanged()
        }
    }


}