package com.example.capstone.ui

import android.Manifest
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import com.example.capstone.R
import com.example.capstone.model.Place
import com.example.capstone.model.Response
import com.example.capstone.services.GeofenceService
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import android.content.Intent
import android.content.BroadcastReceiver
import androidx.core.content.ContextCompat.RECEIVER_EXPORTED
import androidx.core.content.ContextCompat.registerReceiver
import com.example.capstone.recievers.GeofenceBroadcastReceiver

import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.karumi.dexter.Dexter
import com.karumi.dexter.listener.multi.MultiplePermissionsListener

class MapsFragment : Fragment() {

    //Firebase initialization
    private val database = Firebase.database
    private val myRef = database.getReference("capstone")

    private val mutableLiveData = MutableLiveData<Response>()

    private lateinit var geofenceReceiver: GeofenceBroadcastReceiver

    lateinit var mGoogleMap: GoogleMap
    private var mFusedLocationClient: FusedLocationProviderClient? = null

    private var mLocationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val locationList = locationResult.locations
            if (locationList.isNotEmpty()) {
                val location = locationList.last()
                    // Create a LatLng object for the current location
                    val currentLatLng = LatLng(location!!.latitude, location!!.longitude)
                    Toast.makeText(getActivity(),currentLatLng.toString(), Toast.LENGTH_SHORT).show();
                mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f))
                mFusedLocationClient?.removeLocationUpdates(this)
                    // Create a marker for the current location
//                    mGoogleMap.addMarker(
//                        MarkerOptions()
//                            .position(currentLatLng)
//                            .title("My Location")
//                    )
                    // Move the camera to the current location
            }
        }
    }

    private val callback = OnMapReadyCallback { mMap ->

//       /* for(i in 0 until mutableLiveData.value?.list!!.size){
//            val markerData = mutableLiveData.value?.list!![i] // Replace with your marker data

//            val markerOptions = MarkerOptions()
//                .position(LatLng(43.47412575636098, -80.5332843170499))
//                .title("Home")
//                .snippet("this is my home")
//
//            mMap.addMarker(markerOptions)
//    }*/

        //mMap.moveCamera(CameraUpdateFactory.new( 16.0f))

        mGoogleMap = mMap
        mGoogleMap.mapType = GoogleMap.MAP_TYPE_HYBRID
        enableMyLocation()

    }

    private fun enableMyLocation() {
        if (checkPermissions()) {
            val locationRequest = LocationRequest.create()
                .setPriority(Priority.PRIORITY_HIGH_ACCURACY)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(
                        requireActivity(),
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    mFusedLocationClient?.requestLocationUpdates(
                        locationRequest,
                        mLocationCallback,
                        Looper.myLooper()
                    )
                    mGoogleMap.isMyLocationEnabled = true
                    requireActivity().startService(Intent(requireContext(), GeofenceService::class.java))
                } else {
                    ActivityCompat.requestPermissions(
                        requireActivity(),
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        PERMISSIONS_REQUEST_LOCATION
                    )
                }
            } else {
                mFusedLocationClient?.requestLocationUpdates(
                    locationRequest,
                    mLocationCallback,
                    Looper.myLooper()
                )
                mGoogleMap.isMyLocationEnabled = true
                requireActivity().startService(Intent(requireContext(), GeofenceService::class.java))
            }
        } else {
            // Handle permission denied
            // You can display a message or request permission again here
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        getResponseFromRealtimeDatabaseUsingLiveData()

        // Register the receiver to listen for geofence events
        val filter = IntentFilter("GEOFENCE_EVENT")
//        registerReceiver(requireActivity(),geofenceReceiver, filter, RECEIVER_EXPORTED)

        //(activity as? AppCompatActivity)?.supportActionBar?.setDisplayHomeAsUpEnabled(false)
        /*val fab = requireActivity().findViewById<FloatingActionButton>(R.id.floatingActionButton)
        fab.setOnClickListener {
            childFragmentManager.beginTransaction()
                .replace(R.id.fragment_mapss, AddPlaceFragment())
                .addToBackStack(tag)

                .commit()
        }*/
    }

    private fun getResponseFromRealtimeDatabaseUsingLiveData() : MutableLiveData<Response> {

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
            val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
            mapFragment?.getMapAsync(callback)

        }
        return mutableLiveData
    }
    companion object {
        private const val PERMISSIONS_REQUEST_LOCATION = 123
    }

    private fun checkPermissions(): Boolean {
        return (ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
    }
}