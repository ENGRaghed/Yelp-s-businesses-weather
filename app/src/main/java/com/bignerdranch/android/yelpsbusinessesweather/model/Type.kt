package com.bignerdranch.android.yelpsbusinessesweather.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class Type(
    val type : String,
    @DrawableRes val imageResourceId: Int
)

