package com.bignerdranch.android.yelpsbusinessesweather

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.widget.SearchView
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
        inner class BusinessesViewHolder(view: View):RecyclerView.ViewHolder(view)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BusinessesViewHolder {
            val view : View = LayoutInflater.from(parent.context).inflate(R.layout.day_plan_item,parent,false)
            return BusinessesViewHolder(view)
        }

        override fun getItemCount(): Int {
            return yelpBusinesses.size
        }

        override fun onBindViewHolder(holder: BusinessesViewHolder, position: Int) {
            yelpBusinesse = yelpBusinesses[position]
            holder.itemView.tvName1.text = yelpBusinesse.name
            holder.itemView.tvAddress1.text = yelpBusinesse.location.address
            holder.itemView.tvCategory1.text = yelpBusinesse.categories[0].title
            holder.itemView.tvNumReviews1.text = "${yelpBusinesse.numReviews} Reviews"
            holder.itemView.ratingBar1.rating = yelpBusinesse.rating.toFloat()
            Picasso.get().load(yelpBusinesse.imageUrl?:"https://s3-media2.fl.yelpcdn.com/bphoto/Jtp8BNSMnlcdbKCNmjUkYA/o.jpg").error(R.drawable.sunrise).fit().centerCrop().into(holder.itemView.imageView1)

            holder.itemView.setOnClickListener {
                val action = BusinessesListFragmentDirections.actionBusinessesListFragmentToDetailsFragment(yelpBusinesse)
                findNavController().navigate(action)
            }

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
//                    photoGalleryViewModel.fetchPhotos(queryText)

                    lifecycleScope.launch {

                        val latLng = dataStoreProvider.read(LATLNG_KEY)?:"0.0,0.0"
                        val list = latLng.split(",")
                        Log.i(LATLNG_KEY,latLng)
                        viewModel.getYelpBusinessesByCategory("Bearer $API_KEY", query?:"",list[0],list[1]).observe(viewLifecycleOwner,
                            Observer {
                                Log.i("MainActivity","$it")
                            })
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