package com.bignerdranch.android.yelpsbusinessesweather

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
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
import java.text.SimpleDateFormat

class DayPlansListFragment : Fragment() {

    private lateinit var dayPlanViewModel: DayPlanViewModel
    private lateinit var dayPlanViewModelFactory: DayPlanViewModelFactory
    private lateinit var adapter: DayPlanAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(
            R.layout.fragment_day_plans_list, container,
            false
        )

        dayPlanViewModelFactory = DayPlanViewModelFactory(ServiceLocator.dayPlanRepository)
        dayPlanViewModel = ViewModelProvider(this, dayPlanViewModelFactory)
            .get(DayPlanViewModel::class.java)


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

        val swipe = object : MySwipeHelper(requireContext(), recyclerView, 200) {
            override fun instantiateMyButton(
                viewHolder: RecyclerView.ViewHolder,
                buffer: MutableList<MyButton>
            ) {
                //delete button
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
                                        .actionDayPlansListFragmentToEditDayPlanFragment(
                                            adapter.dayPlans[pos].dayPlanId.toString()
                                        )
                                findNavController().navigate(action)
                            }
                        })
                )

            }

        }

        return view
    }


    inner class DayPlanAdapter :
        RecyclerView.Adapter<DayPlanAdapter.DayPlanViewHolder>() {

        lateinit var currentDayPlan: DayPlan
        var dayPlans = emptyList<DayPlan>()

        inner class DayPlanViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val tvName = view.findViewById<TextView>(R.id.tvName1)
            val tvDate = view.findViewById<TextView>(R.id.tvDate)
            val tvNote = view.findViewById<TextView>(R.id.tvNote)
            val state_day_plan = view.findViewById<CheckBox>(R.id.state_day_plan)
            val imageView = view.findViewById<ImageView>(R.id.imageView1)


            fun bind(dayPlan: DayPlan) {

                tvName.text = dayPlan.name
                tvNote.text = dayPlan.note
                if (dayPlan.date != null) {
                    val simpleDateFormat = SimpleDateFormat("MMM dd")
                    val dateFormat = simpleDateFormat.format(dayPlan.date)
                    tvDate.text = dateFormat
                }

                state_day_plan.isChecked = dayPlan.state
                if (dayPlan.state) {
                    tvNote.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                } else {
                    tvNote.paintFlags = 0
                }

                state_day_plan.setOnCheckedChangeListener { buttonView, isChecked ->

                    if (isChecked) {
                        dayPlan.state = true
                        dayPlanViewModel.updateDayPlan(dayPlan)
                        tvNote.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                    } else {
                        dayPlan.state = false
                        dayPlanViewModel.updateDayPlan(dayPlan)
                        tvNote.paintFlags = 0
                    }

                }
                val connectivityManager =
                    context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                val activeNetworkInfo = connectivityManager.activeNetworkInfo
                if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
                    if (dayPlan.imageUrl.isNotEmpty()) {
                        Picasso.get().load(dayPlan.imageUrl).into(imageView)
                    }
                }
            }

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayPlanViewHolder {
            val view: View =
                LayoutInflater.from(parent.context).inflate(R.layout.day_plan_item, parent, false)
            return DayPlanViewHolder(view)
        }

        override fun getItemCount(): Int {
            return dayPlans.size
        }

        override fun onBindViewHolder(holder: DayPlanViewHolder, position: Int) {
            currentDayPlan = dayPlans[position]
            holder.bind(currentDayPlan)

        }

        fun setDayPlanList(dayPlans: List<DayPlan>) {
            this.dayPlans = dayPlans
            notifyDataSetChanged()
        }

    }

    private fun deleteDayPlan(dayPlan: DayPlan) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("yes") { _, _ ->
            dayPlanViewModel.deleteDayPlan(dayPlan)
            Toast.makeText(requireContext(), "deleted ${dayPlan.name}",
                Toast.LENGTH_SHORT).show()
        }
        builder.setNegativeButton("no") { _, _ ->

        }
        builder.setTitle("delete ${dayPlan.name}")
        builder.setMessage("are you want to delete ${dayPlan.name} ?")
        builder.create().show()
    }


}