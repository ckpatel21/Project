package com.example.capstone.ui

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.capstone.R
import com.example.capstone.utils.Constant.Companion.LOGIN_CREDENTIAL


class ProfileFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPreference =  requireActivity().getSharedPreferences(LOGIN_CREDENTIAL, Context.MODE_PRIVATE)
        val data = sharedPreference.getString("email","")
        val emailTv = view.findViewById<TextView>(R.id.tvEmail)
        emailTv.text = data.toString()
    }
}