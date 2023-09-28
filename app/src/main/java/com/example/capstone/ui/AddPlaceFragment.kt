package com.example.capstone.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.*
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import com.example.capstone.R
import com.example.capstone.model.*
import com.example.capstone.utils.Constant
import com.google.android.gms.location.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage

class AddPlaceFragment : Fragment() {

    //Firebase initialization
    val database = Firebase.database
    val myRef = database.getReference("capstone")
    val storageRef = Firebase.storage.getReference("capstone")

    //Get location
    val PERMISSION_ID = 42
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    var lat = 0.0
    var lng = 0.0

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

        //Get location
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        getLastLocation()

        //Category
        //Pictures

        val key = myRef.child("places").push()

        uploadBtn.setOnClickListener {

        }
        submitBtn.setOnClickListener {
            val placeName = placeNameEt.text.toString()
            val placeDescription = placeDescriptionEt.text.toString()
            val latitude = lat
            val longitude = lng

            //Adding values in Firebase
            key.child("user").setValue("Email")
            key.child("placeName").setValue(placeName)
            key.child("placeDescription").setValue(placeDescription)
            key.child("latitude").setValue(latitude)
            key.child("longitude").setValue(longitude)
            key.child("radius").setValue(Constant.radius)
            key.child("points").setValue(1)
        }
    }

    //Fetch Values from Firebase
    private fun getResponseFromRealtimeDatabaseUsingLiveData() : MutableLiveData<Response> {
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

    private fun checkPermissions(): Boolean {
        if (
            ActivityCompat.checkSelfPermission(requireActivity(),Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            return true
        }
        return false
    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {

                fusedLocationClient.lastLocation.addOnCompleteListener(requireActivity()) { task ->
                    val location: Location? = task.result
                    if (location != null) {
                        lat = location.latitude
                        lng = location.longitude
                    }
                }
            } else {
                Toast.makeText(requireActivity(), "Turn on location", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermissions()
        }
    }
    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION),
            PERMISSION_ID
        )
    }
    private fun isLocationEnabled(): Boolean {
        val locationManager : LocationManager = context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }


}