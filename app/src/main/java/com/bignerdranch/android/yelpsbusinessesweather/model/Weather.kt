package com.bignerdranch.android.yelpsbusinessesweather.model

data class Weather(
        val current: Current,
        val forecast: Forecast,
        val location: Location
)

data class Current(
            val cloud: Int,
            val condition: Condition,
            val feelslike_c: Double,
            val gust_kph: Double,
            val gust_mph: Double,
            val humidity: Int,
            val is_day: Int,
            val last_updated: String,
            val last_updated_epoch: Int,
            val temp_c: Double,
            val wind_degree: Int
)


data class Forecast(
        val forecastday: List<Forecastday>
)
data class Forecastday(
    val date: String,
    val date_epoch: Long,
    val day: Day,
    val hour: List<Hour>
)


data class Day(
    val avgtemp_c: Double,//*
    val condition: Condition,//*
    val daily_chance_of_rain: String,
    val daily_chance_of_snow: String,
    val daily_will_it_rain: Int,
    val daily_will_it_snow: Int,
    val maxtemp_c: Double,
    val mintemp_c: Double
)
data class Hour(
    val chance_of_rain: String,
    val chance_of_snow: String,
    val condition: Condition,//*
    val temp_c: Double, //*
    val time: String,//*
    val time_epoch: Int
)

data class Condition(
        val icon: String,
        val text: String
)

data class Location(
        val country: String,
        val lat: Double,
        val localtime: String,
        val localtime_epoch: Int, //
        val lon: Double,
        val name: String,
        val region: String
)

/*
data class Weather(
        val current: Current,
        val forecast: Forecast,
        val location: Location
)

data class Current(
            val cloud: Int,
            val condition: Condition,
            val feelslike_c: Double,
            val feelslike_f: Double,
            val gust_kph: Double,
            val gust_mph: Double,
            val humidity: Int,
            val is_day: Int,
            val last_updated: String,
            val last_updated_epoch: Int,
            val precip_in: Double,
            val precip_mm: Double,
            val pressure_in: Double,
            val pressure_mb: Double,
            val temp_c: Double,
            val temp_f: Double,
            val uv: Double,
            val vis_km: Double,
            val vis_miles: Double,
            val wind_degree: Int,
            val wind_dir: String,
            val wind_kph: Double,
            val wind_mph: Double
)


data class Forecast(
        val forecastday: List<Forecastday>
)
data class Forecastday(
    val astro: Astro,
    val date: String,
    val date_epoch: Int,
    val day: Day,
    val hour: List<Hour>
)

data class Astro(//
    val moon_illumination: String,
    val moon_phase: String,
    val moonrise: String,
    val moonset: String,
    val sunrise: String,
    val sunset: String
)

data class Day(
    val avghumidity: Double,
    val avgtemp_c: Double,//*
    val avgtemp_f: Double,
    val avgvis_km: Double,
    val avgvis_miles: Double,
    val condition: Condition,//*
    val daily_chance_of_rain: String,
    val daily_chance_of_snow: String,
    val daily_will_it_rain: Int,
    val daily_will_it_snow: Int,
    val maxtemp_c: Double,
    val maxtemp_f: Double,
    val maxwind_kph: Double,
    val maxwind_mph: Double,
    val mintemp_c: Double,
    val mintemp_f: Double,
    val totalprecip_in: Double,
    val totalprecip_mm: Double,
    val uv: Double
)
data class Hour(
    val chance_of_rain: String,
    val chance_of_snow: String,
    val cloud: Int,
    val condition: Condition,//*
    val dewpoint_c: Double,
    val dewpoint_f: Double,
    val feelslike_c: Double,
    val feelslike_f: Double,
    val gust_kph: Double,
    val gust_mph: Double,
    val heatindex_c: Double,
    val heatindex_f: Double,
    val humidity: Int,
    val is_day: Int,
    val precip_in: Double,
    val precip_mm: Double,
    val pressure_in: Double,
    val pressure_mb: Double,
    val temp_c: Double, //*
    val temp_f: Double,
    val time: String,//*
    val time_epoch: Int,
    val vis_km: Double,
    val vis_miles: Double,
    val will_it_rain: Int,
    val will_it_snow: Int,
    val wind_degree: Int,
    val wind_dir: String,
    val wind_kph: Double,
    val wind_mph: Double,
    val windchill_c: Double,
    val windchill_f: Double
)

data class Condition(
        val code: Int,//
        val icon: String,
        val text: String
)

data class Location(
        val country: String,
        val lat: Double,
        val localtime: String,
        val localtime_epoch: Int, //
        val lon: Double,
        val name: String,
        val region: String,
        val tz_id: String
)


 */