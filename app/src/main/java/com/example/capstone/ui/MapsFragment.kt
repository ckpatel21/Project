package com.example.capstone.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import com.example.capstone.R
import com.example.capstone.model.Place
import com.example.capstone.model.Response
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MapsFragment : Fragment() {

    //Firebase initialization
    val database = Firebase.database
    val myRef = database.getReference("capstone")

    val mutableLiveData = MutableLiveData<Response>()

    private val callback = OnMapReadyCallback { mMap ->


        /* for(i in 0 until mutableLiveData.value?.list!!.size){
             val markerData = mutableLiveData.value?.list!![i] // Replace with your marker data

             val markerOptions = MarkerOptions()
                 .position(LatLng(markerData.latitude!!, markerData.longitude!!))
                 .title(markerData.placeName)
                 .snippet(markerData.placeDescription)

             mMap.addMarker(markerOptions)
     }*/

        //mMap.moveCamera(CameraUpdateFactory.new( 16.0f))


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

        getResponseFromRealtimeDatabaseUsingLiveData()

        //(activity as? AppCompatActivity)?.supportActionBar?.setDisplayHomeAsUpEnabled(false)
        /*val fab = requireActivity().findViewById<FloatingActionButton>(R.id.floatingActionButton)
        fab.setOnClickListener {
            childFragmentManager.beginTransaction()
                .replace(R.id.fragment_mapss, AddPlaceFragment())
                .addToBackStack(tag)

                .commit()
        }*/
    }

    private fun getResponseFromRealtimeDatabaseUsingLiveData(): MutableLiveData<Response> {

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
            val mapFragment = childFragmentManager.findFragmentById(R.id.explorePlaces) as SupportMapFragment?
            mapFragment?.getMapAsync(callback)

        }
        return mutableLiveData
    }
}