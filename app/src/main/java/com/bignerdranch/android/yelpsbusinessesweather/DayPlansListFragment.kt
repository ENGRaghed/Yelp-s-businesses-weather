package com.bignerdranch.android.yelpsbusinessesweather

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.yelpsbusinessesweather.model.DayPlan
import com.bignerdranch.android.yelpsbusinessesweather.model.Type
import com.bignerdranch.android.yelpsbusinessesweather.viewmodel.DayPlanViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.day_plan_item.view.*

class DayPlansListFragment : Fragment() {

    private lateinit var dayPlanViewModel: DayPlanViewModel
    private lateinit var adapter: DayPlanAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_day_plans_list, container, false)

        dayPlanViewModel = ViewModelProvider(this).get(DayPlanViewModel::class.java)


        dayPlanViewModel.readAllDayPlan.observe(viewLifecycleOwner, Observer {
            adapter.setDayPlanList(it)
        })

        //recycle view
        val recyclerView = view.findViewById<RecyclerView>(R.id.day_plans_rv)
        adapter = DayPlanAdapter()
        val mLayoutManager = LinearLayoutManager(context)
        mLayoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerView.layoutManager = mLayoutManager
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.adapter = adapter


        return view
    }


    inner class DayPlanAdapter() :
        RecyclerView.Adapter<DayPlanAdapter.DayPlanViewHolder>() {

        lateinit var currentDayPlan: DayPlan
        var dayPlans = emptyList<DayPlan>()

        inner class DayPlanViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayPlanViewHolder {
            val view : View = LayoutInflater.from(parent.context).inflate(R.layout.day_plan_item,parent,false)
            return DayPlanViewHolder(view)
        }

        override fun getItemCount(): Int {
            return dayPlans.size
        }

        override fun onBindViewHolder(holder: DayPlanViewHolder, position: Int) {
            currentDayPlan = dayPlans[position]
            holder.itemView.tvName1.text = currentDayPlan.name
            holder.itemView.tvAddress1.text = currentDayPlan.location.address
            Picasso.get().load(currentDayPlan.imageUrl).into(holder.itemView.imageView1)
        }

        fun setDayPlanList(dayPlans : List<DayPlan>){
            this.dayPlans = dayPlans
            notifyDataSetChanged()
        }

    }








    }