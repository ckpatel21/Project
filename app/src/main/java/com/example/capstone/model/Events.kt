package com.example.capstone.model

import android.net.Uri
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Events (
    val eventName: String? = null,
    val eventDescription: String? = null,
    val eventStartDate: String? = null,
    val eventEndDate: String? = null,
    val eventTime : String? = null,
    val eventLocation: String? = null,
    val eventLatitude : Double? = null,
    val eventLongitude : Double? = null,
    val eventOrganizer: String? = null,
    val eventStatus : Boolean? = null,
    val eventCategory : String? = null,
    val pictures : String? = null
) : Parcelable