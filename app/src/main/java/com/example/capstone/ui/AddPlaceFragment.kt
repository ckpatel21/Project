package com.example.capstone.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import com.example.capstone.R
import com.example.capstone.model.Place
import com.example.capstone.model.Response
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage


class AddPlaceFragment : Fragment() {

    //Firebase initialization
    val database = Firebase.database
    val myRef = database.getReference("capstone")
    val storage = Firebase.storage.getReference("capstone")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_place, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Fetch Data
        getResponseFromRealtimeDatabaseUsingLiveData()

        //Variable Declaration
        val submitBtn = view.findViewById<Button>(R.id.btnSubmit)
        val uploadBtn = view.findViewById<ImageButton>(R.id.btnUpload)
        val placeNameEt = view.findViewById<EditText>(R.id.etPlaceName)
        val placeDescriptionEt = view.findViewById<EditText>(R.id.etPlaceDescription)

        //LatLng
        //Category
        //AddedBy
        //Pictures

        val key = myRef.child("places").push()

        uploadBtn.setOnClickListener {

        }
        submitBtn.setOnClickListener {
            val placeName = placeNameEt.text.toString()
            val placeDescription = placeDescriptionEt.text.toString()

            //Adding values in Firebase

            key.child("PlaceName").setValue(placeName)
            key.child("PlaceDescription").setValue(placeDescription)
        }

    }
    //Fetch Values from Firebase
    fun getResponseFromRealtimeDatabaseUsingLiveData() : MutableLiveData<Response> {
        val mutableLiveData = MutableLiveData<Response>()
        myRef.child("places").get().addOnCompleteListener { task ->
            val response = Response()
            if (task.isSuccessful) {
                val result = task.result
                result?.let {
                    response.list = result.children.map { snapShot ->
                        snapShot.getValue(Place::class.java)!!
                    }
                }
            } else {
                response.exception = task.exception
            }
            mutableLiveData.value = response
            Log.d("Data",mutableLiveData.value.toString())
        }
        return mutableLiveData
    }
}