package com.example.capstone.ui

import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.capstone.R
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.Date


class AddEventFragment : Fragment() {

    //Firebase initialization
    private val firebaseDatabase = Firebase.database
    private val databaseReference = firebaseDatabase.getReference("capstone")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_event, container, false)
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

        val startDateTv = view.findViewById<TextView>(R.id.tvStartDate)
        val endDateTv = view.findViewById<TextView>(R.id.tvEndDate)
        val tvTime = view.findViewById<TextView>(R.id.tvTime)

        val eventNameEt = view.findViewById<EditText>(R.id.etEventName)
        val eventDescriptionEt = view.findViewById<EditText>(R.id.etEventDescription)

        tvTime.setOnClickListener {
            activity?.supportFragmentManager?.let {timePicker.show(it,"time") }
        }
        startDateTv.setOnClickListener {
            activity?.supportFragmentManager?.let { startDatePicker.show(it, "startDatePicker") }
        }
        endDateTv.setOnClickListener {
            activity?.supportFragmentManager?.let { endDatePicker.show(it,"endDatePicker") }
        }
        startDatePicker.addOnPositiveButtonClickListener {
            val date = Date(it)
            val dateFormat: java.text.DateFormat? = DateFormat.getDateFormat(view.context)
            startDate = dateFormat?.format(date).toString()
        }
        endDatePicker.addOnPositiveButtonClickListener {
            val date = Date(it)
            val dateFormat: java.text.DateFormat? = DateFormat.getDateFormat(view.context)
            endDate = dateFormat?.format(date).toString()
        }

        timePicker.addOnPositiveButtonClickListener {
            time = "${timePicker.hour}:${timePicker.minute}"
        }

        val submit = view.findViewById<Button>(R.id.btnSubmit)

        //Adding Key
        val key = databaseReference.child("events").push()

        submit.setOnClickListener {
            val eventName = eventNameEt.text.toString()
            val eventDescription = eventDescriptionEt.text.toString()

            //Adding values in Firebase
            key.child("addedBy").setValue("Email")
            key.child("eventName").setValue(eventName)
            key.child("eventDescription").setValue(eventDescription)
            key.child("eventStartDate").setValue(startDate)
            key.child("eventEndDate").setValue(endDate)
            key.child("eventTime").setValue(time)
            key.child("eventStatus").setValue(false)


        }




    }

}