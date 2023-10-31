package com.example.capstone.model

import android.net.Uri

data class Events(
    val pictures: Uri? = null,
    val eventName: String? = null,
    val eventDescription: String? = null,
    val eventStartDate: String? = null,
    val eventEndDate: String? = null,
    val eventTime : String? = null,
    val eventLocation: String? = null
)