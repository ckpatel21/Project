package com.example.capstone.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //(requireActivity() as AppCompatActivity?)!!.supportActionBar!!.setDisplayHomeAsUpEnabled(true)

    }
}