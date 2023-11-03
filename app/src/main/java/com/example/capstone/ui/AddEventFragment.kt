package com.example.capstone.ui

import android.os.Bundle
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.capstone.R
import com.example.capstone.databinding.FragmentAddEventBinding
import com.example.capstone.model.Events
import com.example.capstone.utils.Constant
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.util.Date


class AddEventFragment : Fragment() {

    private var fragmentAddEventBinding: FragmentAddEventBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentAddEventBinding = FragmentAddEventBinding.inflate(inflater, container, false)
        return fragmentAddEventBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var startDate = ""
        var endDate = ""
        var time = ""

        val startDatePicker = MaterialDatePicker.Builder.datePicker()
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds()).setTitleText("Start Date")
            .build()
        val endDatePicker = MaterialDatePicker.Builder.datePicker()
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds()).setTitleText("End Date")
            .build()

        val timePicker = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_12H)
            .setHour(12)
            .setMinute(10)
            .setTitleText("Select Date")
            .build()

        fragmentAddEventBinding!!.tvTime.setOnClickListener {
            activity?.supportFragmentManager?.let { timePicker.show(it, "time") }
        }
        fragmentAddEventBinding!!.tvStartDate.setOnClickListener {
            activity?.supportFragmentManager?.let { startDatePicker.show(it, "startDatePicker") }
        }
        fragmentAddEventBinding!!.tvEndDate.setOnClickListener {
            activity?.supportFragmentManager?.let { endDatePicker.show(it, "endDatePicker") }
        }
        startDatePicker.addOnPositiveButtonClickListener {
            val date = Date(it)
            val dateFormat: java.text.DateFormat? = DateFormat.getDateFormat(view.context)
            startDate = dateFormat?.format(date).toString()
            fragmentAddEventBinding!!.tvStartDate.text = startDate
        }
        endDatePicker.addOnPositiveButtonClickListener {
            val date = Date(it)
            val dateFormat: java.text.DateFormat? = DateFormat.getDateFormat(view.context)
            endDate = dateFormat?.format(date).toString()
            fragmentAddEventBinding!!.tvEndDate.text = endDate
        }

        timePicker.addOnPositiveButtonClickListener {
            time = "${timePicker.hour}:${timePicker.minute}"
            fragmentAddEventBinding!!.tvTime.text = time
        }


        fragmentAddEventBinding!!.btnAddEvent.setOnClickListener {
            val eventName = fragmentAddEventBinding!!.etEventName.text.toString()
            val eventDescription = fragmentAddEventBinding!!.etEventDescription.text.toString()
            val eventLocation = fragmentAddEventBinding!!.etEventLocation.text.toString()

            //Adding Key
            val key = Constant.databaseReference.child("events").push()

            val eventData = Events(null,eventName,eventDescription,startDate,endDate,time,eventLocation)

            //Adding values in Firebase
            key.setValue(eventData).addOnSuccessListener {
                Toast.makeText(requireActivity(),"Successfully added!",Toast.LENGTH_LONG).show()
            }

/*
            key.child("addedBy").setValue("Email")
            key.child("eventName").setValue(eventName)
            key.child("eventDescription").setValue(eventDescription)
            key.child("eventStartDate").setValue(startDate)
            key.child("eventEndDate").setValue(endDate)
            key.child("eventTime").setValue(time)
            key.child("eventLocation").setValue(eventLocation)
            key.child("eventStatus").setValue(false)
*/


        }


    }

}