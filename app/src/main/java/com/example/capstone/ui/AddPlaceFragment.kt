package com.example.capstone.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.example.capstone.R
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class AddPlaceFragment : Fragment() {

    //Firebase initialization
    val database = Firebase.database
    val myRef = database.getReference("capstone")


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_place, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Variable Declaration
        val submitBtn = view.findViewById<Button>(R.id.btnSubmit)
        val placeNameEt = view.findViewById<EditText>(R.id.etPlaceName)
        val placeDescriptionEt = view.findViewById<EditText>(R.id.etPlaceDescription)

        //LatLng
        //Category
        //AddedBy
        //Pictures

        val key = myRef.child("places").push()
        //Firebase database initialization
        submitBtn.setOnClickListener {
            val placeName = placeNameEt.text.toString()
            val placeDescription = placeDescriptionEt.text.toString()

            key.child("PlaceName").setValue(placeName)
            key.child("PlaceDescription").setValue(placeDescription)
        }
    }
}