package com.example.capstone.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.capstone.R
import com.example.capstone.adapter.EventsAdapter
import com.example.capstone.databinding.FragmentEventBinding
import com.example.capstone.model.Events
import com.example.capstone.model.Response
import com.example.capstone.utils.Constant

class EventFragment : Fragment() {

    private var fragmentEventBinding: FragmentEventBinding? = null
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

    private fun fetchEventDetails(): MutableLiveData<Response> {
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
                mutableLiveData.value = response
                fragmentEventBinding!!.eventsRecyclerView.layoutManager =
                    LinearLayoutManager(requireActivity())

                val adapter = EventsAdapter(data , object : EventsAdapter.ShareBtnClickListener {
                    override fun onShareBtnClick(
                        position: Int,
                        eventName: String?,
                        eventStartDate: String?
                    ) {
                        val intent= Intent()
                        intent.action=Intent.ACTION_SEND
                        //TODO Share event details
                        intent.putExtra(Intent.EXTRA_TEXT,"Hey guys, check this event - $eventName on $eventStartDate")
                        intent.type="text/plain"
                        startActivity(Intent.createChooser(intent,"Share To:"))
                    }
                }, object : EventsAdapter.LayoutBtnClickListener {
                    override fun onLayoutClick(
                        position: Int,
                        eventList: Events) {
                        loadFragment(EventDetailsFragment(), eventList)
                    }
                })
                fragmentEventBinding!!.eventsRecyclerView.adapter = adapter

            } else {
                //TODO
                //No data found
                response.exception = task.exception
            }

        }
        return mutableLiveData
    }

    private fun loadFragment(fragment: Fragment, eventList: Events) {
        val bundle = Bundle()
        bundle.putParcelable("event", eventList)
        fragment.arguments = bundle
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.addToBackStack("tag")
        transaction.commit()
    }
}