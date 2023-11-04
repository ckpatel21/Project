package com.example.capstone.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.*
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.example.capstone.adapter.PlacePictureAdapter
import com.example.capstone.R
import com.example.capstone.databinding.FragmentAddPlaceBinding
import com.example.capstone.model.*
import com.example.capstone.utils.Constant
import com.google.android.gms.location.*
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.ByteArrayOutputStream
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.Executors


class AddPlaceFragment : Fragment() {


    private var firebaseStore: FirebaseStorage? = null
    private var storageReference: StorageReference? = null


    //Get location
    private val permissionId = 42
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var lat = 0.0
    private var lng = 0.0

    //Save Category
    private var category = "Select One"

    private lateinit var keyValue : DatabaseReference
    private val executor = Executors.newSingleThreadExecutor()

    private var fragmentAddPlaceBinding : FragmentAddPlaceBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentAddPlaceBinding = FragmentAddPlaceBinding.inflate(inflater, container, false)
        return fragmentAddPlaceBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        firebaseStore = FirebaseStorage.getInstance()
        storageReference = FirebaseStorage.getInstance().reference

        //Fetch Data
        getResponseFromRealtimeDatabaseUsingLiveData()

        //Variable Declaration
        val uploadPictureBtn = view.findViewById<ImageView>(R.id.imageBtnUploadPhoto)
        val takePictureBtn = view.findViewById<ImageView>(R.id.imageBtnTakePhoto)

        val categoryList = view.findViewById<AutoCompleteTextView>(R.id.categoryDropdown)
        val categorySpinner = view.findViewById<TextInputLayout>(R.id.categorySpinner)
        //Get location
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        getLastLocation()

        val key = Constant.databaseReference.child("places").push()
        keyValue = key
        //TODO
        //Add Category
        val adapter = ArrayAdapter(
            requireContext(), android.R.layout.simple_spinner_dropdown_item, arrayOf("Park", "Trail", "Historical", "Innovative")
        )
        categoryList.setAdapter(adapter)
        categoryList.onItemClickListener =
            AdapterView.OnItemClickListener { parent, _, position, id->
                // do something with the available information
                 category = categoryList.text.toString()

            }

        //Select picture
        uploadPictureBtn.setOnClickListener {
            getPhotosFromGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageAndVideo))
        }

        //Take picture
        takePictureBtn.setOnClickListener {

        }


        fragmentAddPlaceBinding!!.btnSubmit.setOnClickListener {
            val placeName = fragmentAddPlaceBinding!!.etPlaceName.text.toString()
            val placeDescription = fragmentAddPlaceBinding!!.etPlaceDescription.text.toString()
            val latitude = lat
            val longitude = lng

            //Adding values in Firebase
            key.child("addedBy").setValue("Email")
            key.child("placeName").setValue(placeName)
            key.child("placeDescription").setValue(placeDescription)
            key.child("latitude").setValue(latitude)
            key.child("category").setValue(category)
            key.child("longitude").setValue(longitude)
            key.child("status").setValue(true)


        }
    }

    private val getPhotosFromGallery = registerForActivityResult(ActivityResultContracts.PickMultipleVisualMedia(3)) { pictureUri : List<Uri> ->

            displayPictures(pictureUri)

        val ref = storageReference?.child("places")?.child(keyValue.key.toString())
        for(i in pictureUri.indices){
           // storageRef.child("placeName${pictureUri.first()}").putFile(pictureUri.first())
            val uploadTask = ref?.child(i.toString())?.putFile(pictureUri[i])

        }
    }

    private fun displayPictures(pictureUri: List<Uri>) {

        val adapter = PlacePictureAdapter(courseList = pictureUri, requireActivity())

        val gridView = view?.findViewById<GridView>(R.id.gridPictures)

        gridView?.adapter =adapter


    }

    //Fetch Values from Firebase
    private fun getResponseFromRealtimeDatabaseUsingLiveData() : MutableLiveData<Response> {
        val mutableLiveData = MutableLiveData<Response>()
        Constant.databaseReference.child("places").get().addOnCompleteListener { task ->
            val response = Response()
            if (task.isSuccessful) {
                val result = task.result
                result?.let {
                    response.list = result.children.map { snapShot ->
                        snapShot.getValue(Place::class.java)!!
                    }
                }
            } else {
                response.exception = task.exception
            }
            mutableLiveData.value = response
            Log.d("Data",mutableLiveData.value.toString())
        }
        return mutableLiveData
    }

    private fun checkPermissions(): Boolean {
        if (
            ActivityCompat.checkSelfPermission(requireActivity(),Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            return true
        }
        return false
    }


    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {

                fusedLocationClient.lastLocation.addOnCompleteListener(requireActivity()) { task ->
                    val location: Location? = task.result
                    if (location != null) {
                        lat = location.latitude
                        lng = location.longitude
                    }
                }
            } else {
                Toast.makeText(requireActivity(), "Turn on location", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermissions()
        }
    }
    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION),
            permissionId
        )
    }
    private fun isLocationEnabled(): Boolean {
        val locationManager : LocationManager = context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }


}