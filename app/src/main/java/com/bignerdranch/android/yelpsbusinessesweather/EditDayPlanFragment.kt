package com.bignerdranch.android.yelpsbusinessesweather

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bignerdranch.android.yelpsbusinessesweather.model.DayPlan
import com.bignerdranch.android.yelpsbusinessesweather.viewmodel.DayPlanViewModel
import com.bignerdranch.android.yelpsbusinessesweather.viewmodel.DayPlanViewModelFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_add_day_plan.*
import kotlinx.android.synthetic.main.fragment_edit_day_plan.*
import java.text.SimpleDateFormat
import java.util.*

private const val DIALOG_DATE = "DialogDate"
private const val REQUEST_DATE = 0
class EditDayPlanFragment : Fragment(),DatePickerFragment.Callbacks {

    private val args by navArgs<EditDayPlanFragmentArgs>()
    private lateinit var dayPlanViewModel: DayPlanViewModel
    private lateinit var dayPlanViewModelFactory: DayPlanViewModelFactory
    private lateinit var dayPlan: DayPlan

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_edit_day_plan, container, false)

        val image = view.findViewById<ImageView>(R.id.businesse_imageEDP)
        val datePicker = view.findViewById<FloatingActionButton>(R.id.date_pickerEDP)
        val dateTv = view.findViewById<TextView>(R.id.date_TvEDP)
        val name = view.findViewById<TextView>(R.id.businesse_nameEDP)
        val editButton = view.findViewById<FloatingActionButton>(R.id.edit_day_plan_button)
        val ratingBar = view.findViewById<RatingBar>(R.id.ratingBarEDP)
        val noteTv = view.findViewById<EditText>(R.id.note)


        val connectivityManager = context?.
        getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo

        dayPlanViewModelFactory = DayPlanViewModelFactory(ServiceLocator.dayPlanRepository)
        dayPlanViewModel = ViewModelProvider(this,dayPlanViewModelFactory).get(DayPlanViewModel::class.java)
        dayPlanViewModel.readDayPlan(args.id).observe(viewLifecycleOwner, Observer {
            setDayPlan(it)
            if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {

                if (it.imageUrl.isNotEmpty()) {
                    Picasso.get().load(it.imageUrl).fit().centerCrop().into(image)

                }
            }
            name.text= it.name
            editButton.visibility = View.VISIBLE
            ratingBar.rating = it.rating.toFloat()
            noteTv.setText(it.note)
            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
            if (it.date != null) {
                val dateFormat = simpleDateFormat.format(it.date)
                dateTv.text = dateFormat

            }
        })

        datePicker.setOnClickListener {
            DatePickerFragment.newInstance(Date()).apply {
                setTargetFragment(this@EditDayPlanFragment,REQUEST_DATE)
                show(this@EditDayPlanFragment.requireFragmentManager(), DIALOG_DATE)
            }
        }

        editButton.setOnClickListener {
            dayPlan.note = noteTv.text.toString()
            dayPlanViewModel.updateDayPlan(dayPlan)

            Toast.makeText(requireContext(),"edit ${dayPlan.name} successful",Toast.LENGTH_SHORT).show()

            val action = EditDayPlanFragmentDirections.actionEditDayPlanFragmentToDayPlansListFragment()
            findNavController().navigate(action)
        }

        return view
    }

    fun setDayPlan(dayPlan: DayPlan){
        this.dayPlan = dayPlan
    }

    override fun onDateSelected(date: Date) {
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
        val dateFormat = simpleDateFormat.format(date)
        date_TvEDP.text = dateFormat
        this.dayPlan.date = date
    }

}