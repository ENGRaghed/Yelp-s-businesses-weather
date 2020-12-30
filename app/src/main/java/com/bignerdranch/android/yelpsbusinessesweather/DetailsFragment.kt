package com.bignerdranch.android.yelpsbusinessesweather

import android.content.Intent
import android.net.Uri
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
//    private lateinit var  dayPlanViewModel : DayPlanViewModel
    private lateinit var adapter: ForecastHourAdapter
    private lateinit var imageAdapter: ImageAdapter


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

        val recyclerViewPager2 = view.findViewById<ViewPager2>(R.id.imageView)
        imageAdapter = ImageAdapter()
//        adapter = ForecastHourAdapter()
//        val myLayoutManager = LinearLayoutManager(context)
//        myLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
//        view.imageView.layoutManager = myLayoutManager
//        recyclerViewPager2.itemAnimator = DefaultItemAnimator()
        recyclerViewPager2.adapter = imageAdapter
//        imageAdapter.setData(args.yelp.photos)
        var list = mutableListOf<String>()
        list.add(args.yelp.imageUrl)
//        list.add("https://s3-media2.fl.yelpcdn.com/bphoto/HW3Yn47ELXkFc6ZEkc3WIg/o.jpg")

        imageAdapter.setData(list)
        viewModel.getPhotos(args.yelp.id, "Bearer $API_KEY_YELP").observe(viewLifecycleOwner, Observer {
            Log.i("image_url","$it")
            imageAdapter.setData(it)
        })

        val name= view.findViewById<TextView>(R.id.tvName)
//        val image = view.findViewById<ViewPager2>(R.id.imageView)
        val ratingBar = view.findViewById<RatingBar>(R.id.ratingBar)
        val tvNumReviews = view.findViewById<TextView>(R.id.tvNumReviews)
//        val address = view.findViewById<TextView>(R.id.tvAddress)
//        val Category = view.findViewById<TextView>(R.id.tvCategory)
//        val tvPrice = view.findViewById<TextView>(R.id.tvPrice)
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
            Picasso.get().load("https://${it.forecast.forecastday[0].day.condition.icon}").fit().centerCrop()
                    .placeholder(R.drawable.wind).error(R.drawable.sunrise).into(imageState1)
            Log.i("image1","https://${it.forecast.forecastday[0].day.condition.icon}")

            Picasso.get().load("https://${it.forecast.forecastday[1].day.condition.icon}").fit().centerCrop()
                    .placeholder(R.drawable.wind).error(R.drawable.sunrise).into(imageState2)
            Log.i("image2","https://${it.forecast.forecastday[1].day.condition.icon}")

            Picasso.get().load("https://${it.forecast.forecastday[2].day.condition.icon}").fit().centerCrop()
                    .placeholder(R.drawable.wind).error(R.drawable.sunrise).into(imageState3)
            Log.i("image3","https://${it.forecast.forecastday[2].day.condition.icon}")






        })

        name.text = args.yelp.name?:""
        ratingBar.rating = args.yelp.rating.toFloat()
        tvNumReviews.text = "${args.yelp.numReviews} Reviews"

//        address.text = args.yelp.location.address
//        Category.text = args.yelp.categories[0].title
//        tvPrice.text = args.yelp.price
//        Picasso.get().load(args.yelp.imageUrl).fit().centerCrop().into(image)
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
                .placeholder(R.drawable.cloud).error(R.drawable.cloud).into(holder.itemView.imageView3)
            holder.itemView.temp_c.text = "${hour.temp_c}°C"

        }
        fun setData(hours: List<Hour>){
            this.hours = hours
            notifyDataSetChanged()
        }

    }

    inner class ImageAdapter():RecyclerView.Adapter<ImageAdapter.ImageViewHolder>(){
        var images = emptyList<String>()
        lateinit var image : String
        inner class ImageViewHolder(view: View):RecyclerView.ViewHolder(view)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
            val view : View = LayoutInflater.from(parent.context).inflate(R.layout.image_item,parent,false)
            return ImageViewHolder(view)
        }

        override fun getItemCount(): Int {
            return images.size
        }

        override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
            image = images[position]
            Picasso.get().load(image).fit().centerCrop().into(holder.itemView.image_view_pager2)
        }
        fun setData(images: List<String>){
            this.images = images
            notifyDataSetChanged()
        }
    }


}