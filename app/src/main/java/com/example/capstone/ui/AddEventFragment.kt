package com.example.capstone.ui

import android.net.Uri
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.example.capstone.databinding.FragmentAddEventBinding
import com.example.capstone.model.Events
import com.example.capstone.utils.Constant
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import java.util.Date


class AddEventFragment : Fragment() {

    private var fragmentAddEventBinding: FragmentAddEventBinding? = null

    private var picturesListUrl : Uri? = null

    private var firebaseStore: FirebaseStorage? = null
    private var storageReference: StorageReference? = null


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

        firebaseStore = FirebaseStorage.getInstance()
        storageReference = FirebaseStorage.getInstance().reference


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

        //Select picture
        fragmentAddEventBinding!!.imageBtnUploadPhoto.setOnClickListener {
            getPhotosFromGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageAndVideo))
        }


        fragmentAddEventBinding!!.btnAddEvent.setOnClickListener {
            //val eventName = fragmentAddEventBinding!!.etEventName.text.toString()
            //val eventDescription = fragmentAddEventBinding!!.etEventDescription.text.toString()
            //val eventLocation = fragmentAddEventBinding!!.etEventLocation.text.toString()

            val eventName = "TestEvent"
            val eventDescription = "TestDescription"
            val eventLocation = "TestLocation"

            //Adding Key
            val keyValue = Constant.databaseReference.child("events").push()

            val eventData = Events(eventName,eventDescription,startDate,endDate,time,eventLocation)

            //Adding pictures
            val ref = storageReference?.child("event")
            if(picturesListUrl?.equals("") == false){
                picturesListUrl?.let {
                        it1 -> ref?.putFile(it1)?.addOnSuccessListener( OnSuccessListener<UploadTask.TaskSnapshot>{
                    ref.downloadUrl.addOnSuccessListener {
                        keyValue.child("pictures").setValue(it.toString())
                    }
                    })
                }

                //Adding values in Firebase
                keyValue.setValue(eventData).addOnSuccessListener {
                    Toast.makeText(requireActivity(),"Successfully added!",Toast.LENGTH_LONG).show()
                }

            }else{
                Toast.makeText(requireActivity(),"Please select poster for the event!",Toast.LENGTH_LONG).show()
            }


            //keyValue.child("pictures").setValue(picturesListUrl.toString())


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
        fragmentAddEventBinding!!.txtWantToChange.setOnClickListener {
            getPhotosFromGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageAndVideo))
        }
    }
    private val getPhotosFromGallery =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { eventUri: Uri? ->

            picturesListUrl = eventUri!!
            displayPictures(eventUri)
        }

    private fun displayPictures(pictureUri: Uri) {
        fragmentAddEventBinding!!.eventPicturePoster.setImageURI(pictureUri)
        fragmentAddEventBinding!!.txtWantToChange.visibility = View.VISIBLE
    }


}