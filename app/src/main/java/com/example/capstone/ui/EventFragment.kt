package com.example.capstone.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.capstone.adapter.EventsAdapter
import com.example.capstone.databinding.FragmentEventBinding
import com.example.capstone.model.Events
import com.example.capstone.model.Response
import com.example.capstone.utils.Constant

class EventFragment : Fragment() {

    private var fragmentEventBinding : FragmentEventBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentEventBinding = FragmentEventBinding.inflate(inflater, container, false)
        return fragmentEventBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fetchEventDetails()
    }

    private fun fetchEventDetails() : MutableLiveData<Response> {
        val mutableLiveData = MutableLiveData<Response>()
        val data = ArrayList<Events>()
        Constant.databaseReference.child("events").get().addOnCompleteListener { task ->
            val response = Response()
            if (task.isSuccessful) {
                val result = task.result
                result?.let {
                    response.list = result.children.map { snapShot ->
                        snapShot.getValue(Events::class.java)!!
                        data.add(snapShot.getValue(Events::class.java)!!)
                    }
                }
            } else {
                response.exception = task.exception
            }
            mutableLiveData.value = response
            fragmentEventBinding!!.eventsRecyclerView.layoutManager = LinearLayoutManager(requireActivity())

            val adapter = EventsAdapter(data)
            fragmentEventBinding!!.eventsRecyclerView.adapter = adapter

        }
        return mutableLiveData
    }

}