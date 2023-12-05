package com.example.capstone.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.capstone.R
import com.example.capstone.adapter.ImageViewPagerAdapter
import com.example.capstone.adapter.ShowReviewAdapter
import com.example.capstone.databinding.FragmentPlaceDetailsBinding
import com.example.capstone.model.RatingReview
import com.example.capstone.utils.Constant
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class PlaceDetailsFragment : Fragment() {

    private val database = Firebase.database
    private val myRef = database.getReference("capstone").child("place")

    private lateinit var fragmentPlaceDetailsBinding: FragmentPlaceDetailsBinding

    val ratingReviewList = ArrayList<RatingReview>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentPlaceDetailsBinding = FragmentPlaceDetailsBinding.inflate(inflater, container, false)
        return fragmentPlaceDetailsBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Fetch Email
        val sharedPreference =
            requireActivity().getSharedPreferences(Constant.LOGIN_CREDENTIAL, Context.MODE_PRIVATE)
        val emailData = sharedPreference.getString("email", "")


        val key = arguments?.getString("geofenceId")

        if (key != null) {
            fetchPlaceDetails(key)
        }

        //Start - Add Ratings and Review

        fragmentPlaceDetailsBinding.btnSubmit.setOnClickListener {
            val ratings = fragmentPlaceDetailsBinding.ratingBar.rating
            val reviews = fragmentPlaceDetailsBinding.etReview.text.toString()
            //Add Rating and Review
            val dataRatingReview = RatingReview(key,ratings,emailData,reviews)
            Constant.databaseReference.child("ratings_reviews").push().setValue(dataRatingReview).addOnSuccessListener {
                Toast.makeText(requireActivity(),"Successfully added!", Toast.LENGTH_LONG).show()
                //Clear Data
                fragmentPlaceDetailsBinding.etReview.text.clear()
                fragmentPlaceDetailsBinding.ratingBar.rating = 0.0f
            }
        }
        //End - Add Ratings and Review

        //Start - Display Reviews
        val query = FirebaseDatabase.getInstance().getReference("capstone").child("ratings_reviews").orderByChild("id").equalTo(key)

        query.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                ratingReviewList.clear()
                if(snapshot.exists()){
                    for(data in snapshot.children) {
                        val reviewData = data.getValue(RatingReview::class.java)
                        if(reviewData?.id.equals(key)){
                            reviewData?.let { ratingReviewList.add(it) }
                        }
                    }
                }
                fragmentPlaceDetailsBinding.rcvReview.layoutManager =
                    LinearLayoutManager(requireActivity())
                val adapter = ShowReviewAdapter(ratingReviewList)
                fragmentPlaceDetailsBinding.rcvReview.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })

        //End - Display Reviews
    }

    private fun fetchPlaceDetails(geofenceId: String) {
        myRef.child(geofenceId).get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val dataSnapShot = task.result
                if (dataSnapShot != null && dataSnapShot.exists()) {
                    // Retrieve data from dataSnapshot and update UI
                    val title = dataSnapShot.child("placeName").getValue(String::class.java)
                    val description = dataSnapShot.child("placeDescription").getValue(String::class.java)
                    val category = dataSnapShot.child("category").getValue(String::class.java) ?: ""
                    val pic0 = dataSnapShot.child("pictures_0").getValue(String::class.java) ?: ""
                    val pic1 = dataSnapShot.child("pictures_1").getValue(String::class.java) ?: ""
                    val pic2 = dataSnapShot.child("pictures_2").getValue(String::class.java) ?: ""

                    // Update UI elements with the retrieved data
                    fragmentPlaceDetailsBinding.textViewTitle.text = title
                    fragmentPlaceDetailsBinding.placeDescription.text = description
                    fragmentPlaceDetailsBinding.textViewCategory.text = category
                    fragmentPlaceDetailsBinding.imageViewCategoryIcon.setImageResource(getCategoryIconRes(category))

                    val viewPager: ViewPager2 = fragmentPlaceDetailsBinding.imageViewer

// Create a list of image resources (replace with your actual image resources)
                    val imageList = ArrayList<String>()
                    if(pic0 !=""){
                        imageList.add(pic0)
                    }
                    if(pic1 !=""){
                        imageList.add(pic1)
                    }
                    if(pic2 != ""){
                        imageList.add(pic2)
                    }


// Create an adapter and set it to the ViewPager2
                    val imagePagerAdapter = ImageViewPagerAdapter(requireActivity(), imageList)
                    viewPager.adapter = imagePagerAdapter

                    /*if(pic0 != ""){
                        Picasso.get().load(pic0).into(fragmentPlaceDetailsBinding.placeImage0)
                    }
                    if(pic1 != ""){
                        Picasso.get().load(pic1).into(fragmentPlaceDetailsBinding.placeImage1)
                    }
                    if(pic2 != ""){
                        Picasso.get().load(pic2).into(fragmentPlaceDetailsBinding.placeImage2)
                    }*/
                }
            } else {
                // Handle error
                println("Error fetching place details: ${task.exception?.message}")
            }
        }
    }

    private fun getCategoryIconRes(category: String): Int {
        return when (category.lowercase()) {
            "fun & games" -> R.drawable.video_game
            "hiking trails & parks" -> R.drawable.hiking
            "point of interest & landmark" -> R.drawable.intresting_place_poi
            "food & drinks" -> R.drawable.food_drinnks
            "shopping malls & antique shops" -> R.drawable.online_shopping
            // Add more categories as needed
            else -> R.drawable.placeholder_600x400
        }
    }

}