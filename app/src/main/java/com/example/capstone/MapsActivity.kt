package com.example.capstone

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map_fragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        // Add a marker in Sydney and move the camera
        val current = LatLng(43.450120, -80.516240)
        mMap.addMarker(
            MarkerOptions()
            .position(current)
            .title("Current location")
        )

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(current, 16.0f))

        mMap.addMarker(
            MarkerOptions()
                .position(LatLng(43.452969,-80.495064))
                .title("Kitchener"),
        )
        mMap.setOnMarkerClickListener {
            // code inside onMarkerClick function
            if(it.title == "Current location"){
                Toast.makeText(applicationContext,"Current loc", Toast.LENGTH_LONG).show()
            }else if(it.title == "Kitchener"){
                Toast.makeText(applicationContext,"Kitchener", Toast.LENGTH_LONG).show()
            }
            true // return value of onMarkerClick function
        }

    }
}