package com.bignerdranch.android.yelpsbusinessesweather

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.yelpsbusinessesweather.model.YelpRestaurant
import com.bignerdranch.android.yelpsbusinessesweather.viewmodel.YelpViewModel
import com.bignerdranch.android.yelpsbusinessesweather.viewmodel.YelpViewModelFactory
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.businesse_item.view.*
import kotlinx.android.synthetic.main.day_plan_item.view.*
import kotlinx.coroutines.launch

private const val API_KEY = "fIK9HGPtNk-VJEAjIM4YyP0sRdeIpG82w6dnYVw_KsVz5c4RT54du50UT5uDakogcu8ism-9EeiEBc9Ca1014bzMMIejU6neWdmo3Zc6NePREOjcoY2XJ_p8SkTaX3Yx"
private const val LATLNG_KEY = "LatLng"


class BusinessesListFragment : Fragment() {

    lateinit var adapter : BusinessesAdapter
    private lateinit var  viewModelFactory : YelpViewModelFactory
    private lateinit var  viewModel : YelpViewModel
    private lateinit var dataStoreProvider: DataStoreProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view =inflater.inflate(R.layout.fragment_businesses_list, container, false)

        viewModelFactory = YelpViewModelFactory(ServiceLocator.repository)
        viewModel= ViewModelProvider(this,viewModelFactory).get(YelpViewModel::class.java)

        dataStoreProvider = DataStoreProvider(requireContext())


        val recyclerView = view.findViewById<RecyclerView>(R.id.rv_businesses)
        adapter = BusinessesAdapter()
        val mLayoutManager = LinearLayoutManager(context)
        mLayoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerView.layoutManager = mLayoutManager
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.adapter = adapter

        viewModel.readAllBusinesses.observe(viewLifecycleOwner, Observer {
            adapter.setData(it)
            it.forEach {
                Log.i("image_businesses_list",it.imageUrl)

            }
        })
        return view
    }

    inner class BusinessesAdapter():RecyclerView.Adapter<BusinessesAdapter.BusinessesViewHolder>(){

        var yelpBusinesses : List<YelpRestaurant> = emptyList()
        lateinit var yelpBusinesse : YelpRestaurant
        inner class BusinessesViewHolder(view: View):RecyclerView.ViewHolder(view){
            val tvName = view.findViewById<TextView>(R.id.tvName2)
            val tvAddress = view.findViewById<TextView>(R.id.tvAddress2)
            val tvCategory = view.findViewById<TextView>(R.id.tvCategory2)
            val tvNumReviews = view.findViewById<TextView>(R.id.tvNumReviews2)
            val ratingBar = view.findViewById<RatingBar>(R.id.ratingBar2)
            val imageView = view.findViewById<ImageView>(R.id.imageView2)
            val cardView = view.findViewById<CardView>(R.id.place)



            fun bind(yelp : YelpRestaurant){
                tvName.text = yelp.name
                tvAddress.text = yelp.location.address
                tvCategory.text = yelp.categories[0].title
                tvNumReviews.text = "${yelp.numReviews} Reviews"
                ratingBar.rating = yelp.rating.toFloat()

                val connectivityManager = context?.
                getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                val activeNetworkInfo = connectivityManager.activeNetworkInfo
                if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
                    if (yelp.imageUrl.isNotEmpty()) {
                        Picasso.get().load(yelp.imageUrl).error(R.drawable.sunrise).fit().centerCrop().into(imageView)
                    }
                }
                cardView.setOnClickListener {
                    val action = BusinessesListFragmentDirections.actionBusinessesListFragmentToDetailsFragment(yelp)
                    findNavController().navigate(action)
                }

            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BusinessesViewHolder {
            val view : View = LayoutInflater.from(parent.context).inflate(R.layout.businesse_item,parent,false)
            return BusinessesViewHolder(view)
        }

        override fun getItemCount(): Int {
            return yelpBusinesses.size
        }

        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(holder: BusinessesViewHolder, position: Int) {
            yelpBusinesse = yelpBusinesses[position]
            holder.bind(yelpBusinesse)

        }

        fun setData(yelpBusinesses : List<YelpRestaurant>){
            this.yelpBusinesses = yelpBusinesses
            notifyDataSetChanged()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.businesses_list_search, menu)

        val searchItem: MenuItem = menu.findItem(R.id.menu_item_search)
        val searchView = searchItem.actionView as SearchView

        searchView.apply {
            setOnQueryTextListener(object  : SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(query: String?): Boolean {

                    lifecycleScope.launch {

                        val latLng = dataStoreProvider.read(LATLNG_KEY)?:"0.0,0.0"
                        val list = latLng.split(",")
                        Log.i(LATLNG_KEY,latLng)
                        val connectivityManager = context?.
                        getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                        val activeNetworkInfo = connectivityManager.activeNetworkInfo
                        if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
                            viewModel.getYelpBusinessesByCategory("Bearer $API_KEY", query
                                    ?: "", list[0], list[1]).observe(viewLifecycleOwner,
                                    Observer {
                                        Log.i("MainActivity", "$it")
                                    })
                        }else{
                            Toast.makeText(requireContext(),"there is no internet connection",Toast.LENGTH_SHORT).show()
                        }
                    }

                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }

            })
        }
    }

}