package com.example.capstone.ui

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.capstone.R
import com.example.capstone.databinding.FragmentEventDetailsBinding
import com.example.capstone.model.Events
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.squareup.picasso.Picasso
import java.lang.String
import java.util.Locale


//Todo - Events details after clicking on specific event
class EventDetailsFragment : Fragment() {

    private lateinit var fragmentEventDetailsBinding: FragmentEventDetailsBinding

    private var latitude : Double = 0.0

    private var longitude : Double = 0.0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentEventDetailsBinding = FragmentEventDetailsBinding.inflate(inflater, container, false)
        Log.d("LifeCycle","onCreate")
        return fragmentEventDetailsBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        Log.d("OnReady","onViewCreated")
        val mapFragment = childFragmentManager.findFragmentById(R.id.mapEventLocation) as SupportMapFragment?

        val data = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable("event", Events::class.java)
        } else {
            arguments?.getParcelable<Events>("event")
        }
        Picasso.get().load(data?.pictures).into(fragmentEventDetailsBinding.eventPoster)
        fragmentEventDetailsBinding.eventTitle.text = data?.eventName
        fragmentEventDetailsBinding.eventDate.text = data?.eventStartDate
        fragmentEventDetailsBinding.eventTime.text = data?.eventTime
        fragmentEventDetailsBinding.eventLocation.text = data?.eventLocation
        fragmentEventDetailsBinding.eventDescription.text = data?.eventDescription
        latitude = data?.eventLatitude!!
        longitude = data.eventLongitude!!
        mapFragment?.getMapAsync(callback)

    }

    private val callback = OnMapReadyCallback { mMap ->
        Log.d("OnReady","OnMapReadyCallback")
        val eventLocation = LatLng(latitude, longitude)
        mMap.addMarker(
            MarkerOptions()
                .position(eventLocation)
                .title("Event Location")
        )
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(43.45018919889245, -80.57727018835895),15f))

        /*mMap.setOnMapClickListener {
            val uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?q=loc:\" ${it.latitude} ${it.longitude}")
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
            intent.setPackage("com.google.android.apps.maps")
            requireActivity().startActivity(intent)
        }*/
    }
}