package com.bignerdranch.android.yelpsbusinessesweather.database

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.bignerdranch.android.yelpsbusinessesweather.getOrAwaitValueTest
import com.bignerdranch.android.yelpsbusinessesweather.model.Coordinates
import com.bignerdranch.android.yelpsbusinessesweather.model.YelpCategory
import com.bignerdranch.android.yelpsbusinessesweather.model.YelpLocation
import com.bignerdranch.android.yelpsbusinessesweather.model.YelpRestaurant
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import com.google.common.truth.Truth.assertThat

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class DaoTest {
    private lateinit var database: BusinessesDatabase
    private lateinit var dao : Dao

    @get:Rule
    val instantTaskRule = InstantTaskExecutorRule()

    @Before
    fun setup(){
        database = Room.inMemoryDatabaseBuilder(
                ApplicationProvider.getApplicationContext(),
                BusinessesDatabase::class.java
        ).allowMainThreadQueries().build()
        dao = database.businessesDao()
    }

    @Test
    fun addBusinesses() = runBlockingTest {
        val category = mutableListOf<YelpCategory>()
        category.add(YelpCategory("cafe"))
        category.add(YelpCategory("food"))
        val yelpBusinesse = YelpRestaurant( 0,
            "jjjjj","Kim's Sushi",
            4.5,
            "$$",
            812,
            3.3,
            "https://s3-media4.fl.yelpcdn.com/bphoto/FU1dpikJj_A49HUTg16smA/o.jpg", category
            , YelpLocation("458 Eagle Rock Ave"), Coordinates(40.8034302, -74.2490792))

        dao.addBusinesses(yelpBusinesse)

        val AllBusinesses = dao.readAllBusinesses().getOrAwaitValueTest()

        assertThat(AllBusinesses.contains(yelpBusinesse))

    }


    @Test
    fun deleteBusinesses() = runBlockingTest {
        val category = mutableListOf<YelpCategory>()
        category.add(YelpCategory("cafe"))
        category.add(YelpCategory("food"))
        val yelpBusinesse = YelpRestaurant( 0,
                "jjjjj","Kim's Sushi",
                4.5,
                "$$",
                812,
                3.3,
                "https://s3-media4.fl.yelpcdn.com/bphoto/FU1dpikJj_A49HUTg16smA/o.jpg", category
                , YelpLocation("458 Eagle Rock Ave"), Coordinates(40.8034302, -74.2490792))

        dao.addBusinesses(yelpBusinesse)
        dao.deleteAllBusinesses()

        val AllBusinesses = dao.readAllBusinesses().getOrAwaitValueTest()

        assertThat(AllBusinesses).isEmpty()

    }

    @After
    fun teardown(){
        database.close()
    }

}