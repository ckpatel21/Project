package com.example.capstone.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.capstone.databinding.FragmentPlaceDetailsBinding

//Todo - Place details after clicking on specific place
class PlaceDetailsFragment : Fragment() {


    private var fragmentPlaceDetailsBinding: FragmentPlaceDetailsBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentPlaceDetailsBinding =
            FragmentPlaceDetailsBinding.inflate(inflater, container, false)
        return fragmentPlaceDetailsBinding!!.root
    }

}