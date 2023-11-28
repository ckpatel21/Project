package com.example.capstone.ui

import android.annotation.SuppressLint
import android.app.Dialog
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.example.capstone.R
import com.example.capstone.databinding.FragmentAddEventBinding
import com.example.capstone.model.EventCategory
import com.example.capstone.model.Events
import com.example.capstone.utils.Constant
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone


class AddEventFragment : Fragment() {

    private lateinit var fragmentAddEventBinding: FragmentAddEventBinding

    private var picturesListUrl: Uri? = null

    private var firebaseStore: FirebaseStorage? = null
    private var storageReference: StorageReference? = null

    private lateinit var mGoogleMap: GoogleMap

    lateinit var geocoder: Geocoder
    var addresses: List<Address>? = null

    private var latitude : Double = 0.0
    private var longitude : Double = 0.0

    //  val latLng = LatLng(lat!!.toDouble(),lng!!.toDouble())
    private lateinit var address : String

    val REQUEST_CODE = 1

    //Save Category
    private var category = "Select One"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentAddEventBinding = FragmentAddEventBinding.inflate(inflater, container, false)
        return fragmentAddEventBinding.root

    }

    @SuppressLint("SimpleDateFormat")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)

        //Declaring Geo coder
        geocoder = Geocoder(requireActivity(), Locale.getDefault())

        firebaseStore = FirebaseStorage.getInstance()
        storageReference = FirebaseStorage.getInstance().reference


        val startDate = ""
        val endDate = ""
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

        fragmentAddEventBinding.tvTime.setOnClickListener {
            activity?.supportFragmentManager?.let { timePicker.show(it, "time") }
        }
        fragmentAddEventBinding.tvStartDate.setOnClickListener {
            activity?.supportFragmentManager?.let { startDatePicker.show(it, "startDatePicker") }
        }
        fragmentAddEventBinding.tvEndDate.setOnClickListener {
            activity?.supportFragmentManager?.let { endDatePicker.show(it, "endDatePicker") }
        }
        startDatePicker.addOnPositiveButtonClickListener {
            val est: Calendar = Calendar.getInstance(TimeZone.getTimeZone("EST"))
            est.timeInMillis = it
            est.add(Calendar.DAY_OF_MONTH, 1)
            val format = SimpleDateFormat("dd-MM-yyyy")
            val startDate: String = format.format(est.time)
            fragmentAddEventBinding.tvStartDate.text = startDate
        }
        endDatePicker.addOnPositiveButtonClickListener {
            val est: Calendar = Calendar.getInstance(TimeZone.getTimeZone("EST"))
            est.timeInMillis = it
            est.add(Calendar.DAY_OF_MONTH, 1)
            val format = SimpleDateFormat("dd-MM-yyyy")
            val endDate: String = format.format(est.time)
            fragmentAddEventBinding.tvEndDate.text = endDate
        }

        timePicker.addOnPositiveButtonClickListener {
            time = "${timePicker.hour}:${timePicker.minute}"
            fragmentAddEventBinding.tvTime.text = time
        }

        //TODO
        val eventCategoryArrayList = ArrayList<String>()

        Constant.databaseReference.child("event_categories").orderByKey().addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for(data in snapshot.children){
                        val eventCategory = data.getValue(EventCategory::class.java)
                        eventCategory?.categoryName?.let { eventCategoryArrayList.add(it) }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
        //Add Category
        val adapter = ArrayAdapter(
            requireContext(),
            //R.layout.simple_spinner_dropdown_item,
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
            //arrayOf("Party", "Trail", "Historical")
            eventCategoryArrayList
        )
        fragmentAddEventBinding.categoryDropdown.setAdapter(adapter)
        fragmentAddEventBinding.categoryDropdown.onItemClickListener =
            AdapterView.OnItemClickListener { parent, _, position, id ->
                // do something with the available information
                category = fragmentAddEventBinding.categoryDropdown.text.toString()

            }

        //End - Add Category
        //Select picture
        fragmentAddEventBinding.imageBtnUploadPhoto.setOnClickListener {
            getPhotosFromGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageAndVideo))
        }

        //Replace Picture
        fragmentAddEventBinding.txtWantToChange.setOnClickListener {
            getPhotosFromGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageAndVideo))
        }

        //Set Location
        fragmentAddEventBinding.etEventLocation.setOnClickListener {
            val mapFragment = MapDialogFragment()
            mapFragment.isCancelable = false
            mapFragment.show(childFragmentManager, null)
        }

        //
        childFragmentManager.setFragmentResultListener("requestKey", this) { requestKey, bundle ->
            // We use a String here, but any type that can be put in a Bundle is supported.

            latitude = bundle.getDouble("latitude")
            longitude = bundle.getDouble("longitude")

            //  val latLng = LatLng(lat!!.toDouble(),lng!!.toDouble())
            address = getAddress(LatLng(latitude, longitude))

            // Do something with the result.
            fragmentAddEventBinding.etEventLocation.text = address
        }



        fragmentAddEventBinding.btnAddEvent.setOnClickListener {
            //val eventName = fragmentAddEventBinding!!.etEventName.text.toString()
            //val eventDescription = fragmentAddEventBinding!!.etEventDescription.text.toString()
            //val eventLocation = fragmentAddEventBinding!!.etEventLocation.text.toString()
            //val eventOrganizer = fragmentAddEventBinding!!.etEventOrganizer.text.toString()

            val eventOrganizer = "Test organizer"
            val eventName = "TestEvent"
            val eventDescription = "TestDescription"
            val eventLocation = address
            val eventCategory = category
            //Adding Key
            val keyValue = Constant.databaseReference.child("events").push()

            val eventData = Events(
                eventName,
                eventDescription,
                startDate,
                endDate,
                time,
                eventLocation,
                latitude,
                longitude,
                eventOrganizer,
                false,
                eventCategory
            )

            //Adding pictures
            val ref = storageReference?.child("event")?.child(keyValue.key.toString())
            if (picturesListUrl?.equals("") == false) {
                picturesListUrl?.let { it1 ->
                    ref?.putFile(it1)?.addOnSuccessListener {
                        ref.downloadUrl.addOnSuccessListener {
                            keyValue.child("pictures").setValue(it.toString())
                        }
                    }
                }

                //Adding values in Firebase
                keyValue.setValue(eventData).addOnSuccessListener {
                    Toast.makeText(requireActivity(), "Successfully added!", Toast.LENGTH_LONG)
                        .show()
                    fragmentAddEventBinding.etEventName.text?.clear()
                    fragmentAddEventBinding.etEventDescription.text?.clear()
                    fragmentAddEventBinding.etEventOrganizer.text?.clear()
                }

            } else {
                Toast.makeText(
                    requireActivity(),
                    "Please select poster for the event!",
                    Toast.LENGTH_LONG
                ).show()
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

    }

    private fun getAddress(latLng: LatLng): String {
        addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)

        val address = addresses?.get(0)?.getAddressLine(0)
        return address!!
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_info_icon,menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.infoDialog -> {
                val dialog = Dialog(requireActivity())
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                dialog.setCancelable(true)
                dialog.setContentView(R.layout.layout_info_dialog)

                val body = dialog.findViewById(R.id.tvInfo) as TextView
                val closeButton = dialog.findViewById(R.id.iv_close_map) as ImageView

                body.text = "Add event information. Please provide the details of the event, including the event name, date, time, location, and any other relevant information you would like to include in description. For the location, once you will click on Event location, Map dialog will pop up, where you can select any location by long press and you will see address details, click on Add button once you identify the location. Once you have added the details, Select the appropriate poster for your event and click the button Add Event!"

                closeButton.setOnClickListener {
                    dialog.dismiss()
                }
                dialog.show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private val getPhotosFromGallery =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { eventUri: Uri? ->

            /*picturesListUrl = eventUri
            //Compress picture
            compressPicture(picturesListUrl)
            eventUri?.let { displayPictures() }*/

            /*val bitmap =
                eventUri?.let {
                    ImageUtils.decodeSampledBitmapFromUri(requireActivity(),
                        it,300,300)
                }
            val uri = bitmap?.let { BitmapUtils.bitmapToUri(requireActivity(), it) }
            */
            picturesListUrl = eventUri
            eventUri?.let { displayPictures(it) }
        }

   /* private fun fileFromContentUri(context: Context, contentUri: Uri): File {
        // Preparing Temp file name
        val fileExtension = getFileExtension(context, contentUri)
        val fileName = "temp_file" + if (fileExtension != null) ".$fileExtension" else ""

        // Creating Temp file
        val tempFile = File(context.cacheDir, fileName)
        tempFile.createNewFile()

        try {
            val oStream = FileOutputStream(tempFile)
            val inputStream = context.contentResolver.openInputStream(contentUri)

            inputStream?.let {
                copy(inputStream, oStream)
            }

            oStream.flush()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return tempFile
    }

    private fun getFileExtension(context: Context, uri: Uri): String? {
        val fileType: String? = context.contentResolver.getType(uri)
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(fileType)
    }

    private fun copy(source: InputStream, target: OutputStream) {
        val buf = ByteArray(8192)
        var length: Int
        while (source.read(buf).also { length = it } > 0) {
            target.write(buf, 0, length)
        }
    }

    private fun compressPicture(picturesListUrl: Uri?) {

        val file = picturesListUrl?.let { fileFromContentUri(requireActivity(), it) }
        lifecycleScope.launch {
            val compressedImageFile = file?.let {
                Compressor.compress(requireActivity(), it) {
                    resolution(1280, 720)
                    quality(80)
                    format(Bitmap.CompressFormat.WEBP)
                    size(2_097_152) // 2 MB
                }
            }

            if (compressedImageFile != null) {
                fragmentAddEventBinding.eventPicturePoster.setImageURI(compressedImageFile.toUri())
            }

        }
    }
    */

    private fun displayPictures(pictureUri: Uri) {
        fragmentAddEventBinding.eventPicturePoster.setImageURI(pictureUri)
        fragmentAddEventBinding.txtWantToChange.visibility = View.VISIBLE
    }
}