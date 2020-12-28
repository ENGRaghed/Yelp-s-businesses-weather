package com.bignerdranch.android.yelpsbusinessesweather

import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.yelpsbusinessesweather.model.DayPlan
import com.bignerdranch.android.yelpsbusinessesweather.swipe.MyButton
import com.bignerdranch.android.yelpsbusinessesweather.swipe.MyButtonClickListener
import com.bignerdranch.android.yelpsbusinessesweather.swipe.MySwipeHelper
import com.bignerdranch.android.yelpsbusinessesweather.viewmodel.DayPlanViewModel
import com.bignerdranch.android.yelpsbusinessesweather.viewmodel.DayPlanViewModelFactory
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.day_plan_item.view.*

class DayPlansListFragment : Fragment() {

    private lateinit var dayPlanViewModel: DayPlanViewModel
    private lateinit var dayPlanViewModelFactory: DayPlanViewModelFactory
    private lateinit var adapter: DayPlanAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_day_plans_list, container, false)

        dayPlanViewModelFactory = DayPlanViewModelFactory(ServiceLocator.dayPlanRepository)
        dayPlanViewModel = ViewModelProvider(this,dayPlanViewModelFactory)
            .get(DayPlanViewModel::class.java)
//        dayPlanViewModel = ViewModelProvider(this).get(DayPlanViewModel::class.java)


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


        //add swipe

        val swipe = object : MySwipeHelper(requireContext(),recyclerView,200) {
            override fun instantiateMyButton(
                viewHolder: RecyclerView.ViewHolder,
                buffer: MutableList<MyButton>
            ) {
                //add button
                buffer.add(
                    MyButton(requireContext(),
                        "delete",
                        30,
                        R.drawable.ic_delete_24,
                        Color.parseColor("#FF3c30"),
                        object :
                            MyButtonClickListener {
                            override fun onClick(pos: Int) {
                                deleteDayPlan(adapter.dayPlans[pos])
                            }
                        })
                )

                //edit button
                buffer.add(
                    MyButton(requireContext(),
                        "edit",
                        30,
                        R.drawable.ic_edit_white_24,
                        Color.parseColor("#FF9502"),
                        object :
                            MyButtonClickListener {
                            override fun onClick(pos: Int) {

                                val action =
                                    DayPlansListFragmentDirections
                                        .actionDayPlansListFragmentToEditDayPlanFragment(adapter.dayPlans[pos].dayPlanId.toString())
                                findNavController().navigate(action)
                            }
                        })
                )

            }

        }

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

    private fun deleteDayPlan(dayPlan: DayPlan){
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("yes"){_, _ ->
            dayPlanViewModel.deleteDayPlan(dayPlan)
            Toast.makeText(requireContext(),"deleted ${dayPlan.name}", Toast.LENGTH_SHORT).show()
        }
        builder.setNegativeButton("no"){_, _ ->

        }
        builder.setTitle("delete ${dayPlan.name}")
        builder.setMessage("are you want to delete ${dayPlan.name} ?")
        builder.create().show()
    }








    }