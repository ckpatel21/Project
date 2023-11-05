package com.example.capstone.model

import android.net.Uri

data class Place(
    val user: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val placeName: String? = null,
    val placeDescription: String? = null,
    val category: String? = null,
    //val picturesListUrl : ArrayList<Uri>? = null
    //val radius: Int? = null
)
