package com.example.capstone.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.capstone.databinding.FragmentProfileBinding
import com.example.capstone.utils.Constant.Companion.LOGIN_CREDENTIAL


class ProfileFragment : Fragment() {

    private var fragmentProfileBinding: FragmentProfileBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentProfileBinding = FragmentProfileBinding.inflate(inflater, container, false)
        return fragmentProfileBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPreference =
            requireActivity().getSharedPreferences(LOGIN_CREDENTIAL, Context.MODE_PRIVATE)
        val data = sharedPreference.getString("email", "")
        fragmentProfileBinding!!.tvEmail.text = data.toString()
    }
}