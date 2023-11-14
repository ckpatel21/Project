package com.example.capstone.ui

import android.R
import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.example.capstone.databinding.FragmentAddEventBinding
import com.example.capstone.model.Events
import com.example.capstone.utils.Constant
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.TimeZone


class AddEventFragment : Fragment() {

    private var fragmentAddEventBinding: FragmentAddEventBinding? = null

    private var picturesListUrl : Uri? = null

    private var firebaseStore: FirebaseStorage? = null
    private var storageReference: StorageReference? = null

    private lateinit var mGoogleMap: GoogleMap

    //Save Category
    private var category = "Select One"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentAddEventBinding = FragmentAddEventBinding.inflate(inflater, container, false)
        return fragmentAddEventBinding!!.root
    }

    @SuppressLint("SimpleDateFormat")
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
            val est: Calendar = Calendar.getInstance(TimeZone.getTimeZone("EST"))
            est.timeInMillis = it
            est.add(Calendar.DAY_OF_MONTH,1)
            val format = SimpleDateFormat("dd-MM-yyyy")
            val startDate: String = format.format(est.time)
            fragmentAddEventBinding!!.tvStartDate.text = startDate
        }
        endDatePicker.addOnPositiveButtonClickListener {
            val est: Calendar = Calendar.getInstance(TimeZone.getTimeZone("EST"))
            est.timeInMillis = it
            est.add(Calendar.DAY_OF_MONTH,1)
            val format = SimpleDateFormat("dd-MM-yyyy")
            val endDate: String = format.format(est.time)
            fragmentAddEventBinding!!.tvEndDate.text = endDate
        }

        timePicker.addOnPositiveButtonClickListener {
            time = "${timePicker.hour}:${timePicker.minute}"
            fragmentAddEventBinding!!.tvTime.text = time
        }

        //TODO
        //Add Category
        val adapter = ArrayAdapter(
            requireContext(),
            R.layout.simple_spinner_dropdown_item,
            arrayOf("Party", "Trail", "Historical")
        )
        fragmentAddEventBinding!!.categoryDropdown.setAdapter(adapter)
        fragmentAddEventBinding!!.categoryDropdown.onItemClickListener =
            AdapterView.OnItemClickListener { parent, _, position, id ->
                // do something with the available information
                category = fragmentAddEventBinding!!.categoryDropdown.text.toString()

            }

        //Select picture
        fragmentAddEventBinding!!.imageBtnUploadPhoto.setOnClickListener {
            getPhotosFromGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageAndVideo))
        }

        //Set Location
        fragmentAddEventBinding!!.etEventLocation.setOnClickListener {
            MapDialogFragment().show(childFragmentManager, "Map Fragment")
        }


        fragmentAddEventBinding!!.btnAddEvent.setOnClickListener {
            //val eventName = fragmentAddEventBinding!!.etEventName.text.toString()
            //val eventDescription = fragmentAddEventBinding!!.etEventDescription.text.toString()
            //val eventLocation = fragmentAddEventBinding!!.etEventLocation.text.toString()
            //val eventOrganizer = fragmentAddEventBinding!!.etEventOrganizer.text.toString()

            val eventOrganizer = "Test organizer"
            val eventName = "TestEvent"
            val eventDescription = "TestDescription"
            val eventLocation = "TestLocation"
            val eventCategory = category
            //Adding Key
            val keyValue = Constant.databaseReference.child("events").push()

            val eventData = Events(eventName,eventDescription,startDate,endDate,time,eventLocation,eventOrganizer,eventCategory)

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

            picturesListUrl = eventUri
            eventUri?.let { displayPictures(it) }
        }

    private fun displayPictures(pictureUri: Uri) {
        fragmentAddEventBinding!!.eventPicturePoster.setImageURI(pictureUri)
        fragmentAddEventBinding!!.txtWantToChange.visibility = View.VISIBLE
    }
}