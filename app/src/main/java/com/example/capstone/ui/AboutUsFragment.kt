package com.example.capstone.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.capstone.R
import com.example.capstone.databinding.FragmentAboutUsBinding
import com.example.capstone.databinding.FragmentAddEventBinding


class AboutUsFragment : Fragment() {

    private lateinit var fragmentAboutUsBinding : FragmentAboutUsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentAboutUsBinding = FragmentAboutUsBinding.inflate(inflater, container, false)
        return fragmentAboutUsBinding.root
    }

}