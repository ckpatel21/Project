package com.example.capstone.utils

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class Constant {

    companion object {

        val RC_SIGN_IN: Int = 9001
        const val LOGIN_CREDENTIAL = "Login_credential"
        const val radius : Int = 6000 //Meters

        //Firebase initialization
        private val firebaseDatabase = Firebase.database
        val databaseReference = firebaseDatabase.getReference("capstone")

        val storageRef = Firebase.storage.getReference("capstone")
    }

}