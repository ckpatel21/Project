package com.example.capstone.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.capstone.R
import com.example.capstone.adapter.ShowEventsAdapter
import com.example.capstone.databinding.FragmentEventBinding
import com.example.capstone.model.Events
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class EventFragment : Fragment() {

    private lateinit var fragmentEventBinding: FragmentEventBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentEventBinding = FragmentEventBinding.inflate(inflater, container, false)
        return fragmentEventBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fetchEventDetails()
    }

    private fun fetchEventDetails(): MutableLiveData<DataSnapshot> {
        val mutableLiveData = MutableLiveData<DataSnapshot>()
        val eventsArrayList = ArrayList<Events>()

        val query = FirebaseDatabase.getInstance().getReference("capstone").child("events").orderByChild("eventStatus").equalTo(true)

        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                if(snapshot.exists()){
                    eventsArrayList.clear()
                    for(data in snapshot.children){
                        val eventData = data.getValue(Events::class.java)
                        eventData?.let { eventsArrayList.add(it) }
                    }
                }
                if(activity != null){
                    mutableLiveData.value = snapshot
                    fragmentEventBinding.eventsRecyclerView.layoutManager =
                        LinearLayoutManager(requireActivity())
                    val adapter = ShowEventsAdapter(eventsArrayList , object : ShowEventsAdapter.ShareBtnClickListener {
                        override fun onShareBtnClick(
                            position: Int,
                            eventName: String?,
                            eventStartDate: String?,
                            pictures: ImageView,
                            eventDescription: String?,
                            eventLatitude: Double?,
                            eventLongitude: Double?
                        ) {

                            //val bmpUri: Uri? = getLocalBitmapUri(pictures)
                            val intent= Intent()
                            intent.action=Intent.ACTION_SEND
                            //TODO Share event details
                            val url = "http://www.google.com/maps/place/$eventLatitude,$eventLongitude"
                            val wavingHandEmoji = "\uD83D\uDC4B"
                            val locationEmoji = "\uD83C\uDF0D"
                            val dateEmoji = "\uD83D\uDDD3"
                            intent.putExtra(Intent.EXTRA_TEXT,"Hey guys $wavingHandEmoji, check this event - $eventName. \n\n$dateEmoji Date - $eventStartDate. \n\nHere is the brief about the event, $eventDescription" +
                                    "\n\n$locationEmoji Location - \n${Uri.parse(url)} ")

                            intent.type="text/plain"
                            //intent.putExtra(Intent.EXTRA_STREAM, bmpUri)
                            //intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            //intent.type = "image/*"
                            startActivity(Intent.createChooser(intent,"Share To:"))
                        }

                    }, object : ShowEventsAdapter.LayoutBtnClickListener {
                        override fun onLayoutClick(
                            position: Int,
                            eventList: Events) {
                            loadFragment(EventDetailsFragment(), eventList)
                        }
                    })
                    fragmentEventBinding.eventsRecyclerView.adapter = adapter

                }

            }

            override fun onCancelled(error: DatabaseError) {
            }


        })

        /*query.get().addOnCompleteListener { task ->
            val response = Response()
            if (task.isSuccessful) {
                val result = task.result
                result?.let {
                    response.list = result.children.map { snapShot ->

                        snapShot.getValue(Events::class.java)!!
                        eventsArrayList.add(snapShot.getValue(Events::class.java)!!)
                    }
                }
                mutableLiveData.value = response
                fragmentEventBinding!!.eventsRecyclerView.layoutManager =
                    LinearLayoutManager(requireActivity())

                val adapter = ShowEventsAdapter(eventsArrayList , object : ShowEventsAdapter.ShareBtnClickListener {
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
                }, object : ShowEventsAdapter.LayoutBtnClickListener {
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

        }*/
        return mutableLiveData
    }

    /*fun getLocalBitmapUri(imageView: ImageView): Uri? {
        // Extract Bitmap from ImageView drawable
        val drawable = imageView.drawable
        var bmp: Bitmap? = null
        bmp = if (drawable is BitmapDrawable) {
            (imageView.drawable as BitmapDrawable).bitmap
        } else {
            return null
        }
        // Store image to default external storage directory
        var bmpUri: Uri? = null
        try {
            val file = File(
                requireActivity().filesDir, "share_image_" + System.currentTimeMillis() + ".png"
            )
            file.parentFile.mkdirs()
            val out = FileOutputStream(file)
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out)
            out.close()
            val bmpUri = FileProvider.getUriForFile(
                requireActivity(),
                requireActivity().applicationContext.packageName + ".provider",
                file
            )
            //bmpUri = Uri.fromFile(file)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return bmpUri
    }*/


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