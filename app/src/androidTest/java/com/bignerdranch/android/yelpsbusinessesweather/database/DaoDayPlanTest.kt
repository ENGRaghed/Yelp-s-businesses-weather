package com.bignerdranch.android.yelpsbusinessesweather.database

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.bignerdranch.android.yelpsbusinessesweather.getOrAwaitValueTest
import com.bignerdranch.android.yelpsbusinessesweather.model.*
import com.google.common.truth.Truth.assertThat
import com.google.common.truth.Truth
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class DaoDayPlanTest {
    private lateinit var database: BusinessesDatabase
    private lateinit var dao : DaoDayPlan

    @get:Rule
    val instantTaskRule = InstantTaskExecutorRule()

    @Before
    fun setup(){
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            BusinessesDatabase::class.java
        ).allowMainThreadQueries().build()
        dao = database.daoDayPlan()
    }

    @Test
    fun addDayPlanTest() = runBlockingTest {
        val category = mutableListOf<YelpCategory>()
        category.add(YelpCategory("cafe"))
        category.add(YelpCategory("food"))
        val dayPlan = DayPlan(
            0,
            "", 3.3,
            "",
            3,
            3.3,
            "", category
            , YelpLocation(""), Coordinates(0.0, 0.0), "",
            Date(),false
        )

        dao.addDayPlan(dayPlan)

        val allDayPlan = dao.readAllDayPlans().getOrAwaitValueTest()

       assertThat(allDayPlan.contains(dayPlan))

    }


    @Test
    fun deleteDayPlanTest() = runBlockingTest {
        val category = mutableListOf<YelpCategory>()
        category.add(YelpCategory("cafe"))
        category.add(YelpCategory("food"))
        val dayPlan = DayPlan(
            0,
            "", 3.3,
            "",
            3,
            3.3,
            "", category
            , YelpLocation(""), Coordinates(0.0, 0.0), "",
            Date(),false
        )

        dao.addDayPlan(dayPlan)

        dao.deleteDayPlan(dayPlan)

        val allDayPlan = dao.readAllDayPlans().getOrAwaitValueTest()

        assertThat(allDayPlan).doesNotContain(dayPlan)

    }

    @Test
    fun UpdateDayPlanTest() = runBlockingTest {
        val category = mutableListOf<YelpCategory>()
        category.add(YelpCategory("cafe"))
        category.add(YelpCategory("food"))
        val dayPlan = DayPlan(
            0,
            "", 3.3,
            "",
            3,
            3.3,
            "", category
            , YelpLocation(""), Coordinates(0.0, 0.0), "",
            Date(),false
        )
        val dayPlan1 = DayPlan(
            0,
            "", 3.3,
            "",
            3,
            3.3,
            "", category
            , YelpLocation(""), Coordinates(0.0, 0.0), "",
            Date(),true
        )

        dao.addDayPlan(dayPlan)

        dao.updateDayPlan(dayPlan1)

        val allDayPlan = dao.readAllDayPlans().getOrAwaitValueTest()
        assertThat(!allDayPlan.contains(dayPlan))
    }

    @After
    fun teardown(){
        database.close()
    }

}