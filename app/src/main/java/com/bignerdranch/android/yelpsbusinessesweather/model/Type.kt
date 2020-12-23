package com.bignerdranch.android.yelpsbusinessesweather.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class Type(
//    @StringRes val stringResourceId: Int,
    val type : String,
    @DrawableRes val imageResourceId: Int
)

