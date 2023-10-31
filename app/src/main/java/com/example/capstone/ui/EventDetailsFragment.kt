package com.example.capstone.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.capstone.databinding.FragmentEventDetailsBinding

//Todo - Events details after clicking on specific event
class EventDetailsFragment : Fragment() {

    private var fragmentEventDetailsBinding: FragmentEventDetailsBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentEventDetailsBinding =
            FragmentEventDetailsBinding.inflate(inflater, container, false)
        return fragmentEventDetailsBinding!!.root
    }
}