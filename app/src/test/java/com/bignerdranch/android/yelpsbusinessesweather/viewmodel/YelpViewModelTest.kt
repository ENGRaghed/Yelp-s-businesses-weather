package com.bignerdranch.android.yelpsbusinessesweather.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.bignerdranch.android.yelpsbusinessesweather.TestCoroutineRule
import com.bignerdranch.android.yelpsbusinessesweather.getOrAwaitValue
import com.bignerdranch.android.yelpsbusinessesweather.repository.FakeYelpRepositry
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

private const val API_KEY = "fIK9HGPtNk-VJEAjIM4YyP0sRdeIpG82w6dnYVw_KsVz5c4RT54du50UT5uDakogcu8ism-9EeiEBc9Ca1014bzMMIejU6neWdmo3Zc6NePREOjcoY2XJ_p8SkTaX3Yx"


@ExperimentalCoroutinesApi
class YelpViewModelTest {

    @get:Rule
    val instantTaskRule = InstantTaskExecutorRule()

    @get:Rule
    val mainCoroutineRule = TestCoroutineRule()

    private val fakeYelpRepositry = FakeYelpRepositry()

    private val viewModel = YelpViewModel(fakeYelpRepositry)

    @Before
    fun setUp(){
        fakeYelpRepositry.add()

    }


    @Test
    fun `when read all businesses`(){
        mainCoroutineRule.dispatcher.runBlockingTest {
            val businesses = viewModel.readAllBusinesses.getOrAwaitValue()
            assert(businesses.isNotEmpty())
        }
    }

    @Test
    fun `when get Businesses by inter valid information`(){
        mainCoroutineRule.dispatcher.runBlockingTest {
            val value = viewModel.getYelpBusinesses(API_KEY,"40.777272","-74.269483").getOrAwaitValue()
            assert(value.isNotEmpty())
        }
    }

    @Test
    fun `when get Businesses By inter specific Category`(){
        mainCoroutineRule.dispatcher.runBlockingTest {
            val value = viewModel
                    .getYelpBusinessesByCategory(API_KEY,"all","40.777272","-74.269483")
                    .getOrAwaitValue()
            assert(value.isNotEmpty())
        }
    }

    @Test
    fun `get weather when inter a valid info`(){
        mainCoroutineRule.dispatcher.runBlockingTest {
            val value = viewModel.getCurrentWeather(API_KEY,"40.777272, -74.269483","5").getOrAwaitValue()
            assert(value != null)
        }
    }

    @Test
    fun `read Businesse that it's id  equal the intered id`(){
        mainCoroutineRule.dispatcher.runBlockingTest {
            val value = viewModel.readBusinesse("0").getOrAwaitValue()
            assert(value != null)
        }
    }


}