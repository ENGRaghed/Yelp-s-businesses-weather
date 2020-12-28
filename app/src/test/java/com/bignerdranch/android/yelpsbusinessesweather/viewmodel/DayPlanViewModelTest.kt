package com.bignerdranch.android.yelpsbusinessesweather.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.bignerdranch.android.yelpsbusinessesweather.TestCoroutineRule
import com.bignerdranch.android.yelpsbusinessesweather.getOrAwaitValue
import com.bignerdranch.android.yelpsbusinessesweather.model.Coordinates
import com.bignerdranch.android.yelpsbusinessesweather.model.DayPlan
import com.bignerdranch.android.yelpsbusinessesweather.model.YelpCategory
import com.bignerdranch.android.yelpsbusinessesweather.model.YelpLocation
import com.bignerdranch.android.yelpsbusinessesweather.repository.FakeDayPlanRepositry
import org.junit.Assert.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.*

@ExperimentalCoroutinesApi
class DayPlanViewModelTest{


    @get:Rule
    val instantTaskRule = InstantTaskExecutorRule()

    @get:Rule
    val mainCoroutineRule = TestCoroutineRule()

    private val dayPlanRepository = FakeDayPlanRepositry()

    private val viewModel = DayPlanViewModel(dayPlanRepository)
    val category = mutableListOf<YelpCategory>()

    @Before
    fun setUp(){
        dayPlanRepository.add()
    }

    @Test
    fun `when read all day plans`() {
        mainCoroutineRule.dispatcher.runBlockingTest {
            val responseCall = viewModel.readAllDayPlan
            val x = responseCall.getOrAwaitValue()
            assert(x.isNotEmpty())
        }
    }

    @Test
    fun `when read day plan`() {
        mainCoroutineRule.dispatcher.runBlockingTest {

            val responseCall = viewModel.readDayPlan(0.toString())
            val x = responseCall.getOrAwaitValue()
            assert(x != null)
        }
    }

    @Test
    fun `when add day plan`() {
        runBlockingTest {
            val dayPlan =  DayPlan(
                3,
                "", 3.3,
                "",
                3,
                3.3,
                "", category
                , YelpLocation(""), Coordinates(0.0, 0.0), "",
                Date()
            )
            viewModel.addDayPlan(dayPlan)
            val list = viewModel.readAllDayPlan.getOrAwaitValue()
            assert(list.contains(dayPlan))
        }
    }

    @Test
    fun `when delete day plan`() {
        runBlockingTest {
            val dayPlan =  DayPlan(
                1,
                "", 3.3,
                "",
                3,
                3.3,
                "", category
                , YelpLocation(""), Coordinates(0.0, 0.0), "",
                Date()
            )

            viewModel.deleteDayPlan(dayPlan)
            val list = viewModel.readAllDayPlan.getOrAwaitValue()
            assert(!list.contains(dayPlan))
        }
    }
    @Test
    fun `when update day plan`() {
        runBlockingTest {
            val dayPlan =  DayPlan(
                0,
                "name", 3.3,
                "",
                3,
                3.3,
                "", category
                , YelpLocation(""), Coordinates(0.0, 0.0), "",
                Date()
            )

            viewModel.updateDayPlan(dayPlan)
            val list = viewModel.readAllDayPlan.getOrAwaitValue()
            assert(list.contains(dayPlan))
        }
    }

}