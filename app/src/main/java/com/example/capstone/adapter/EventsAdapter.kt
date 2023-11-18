package com.example.capstone.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.capstone.R
import com.example.capstone.model.Events

class EventsAdapter(private val eventList: List<Events>) : RecyclerView.Adapter<EventsAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_events, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val eventViewModel = eventList[position]

        holder.eventPicture.setImageURI(eventViewModel.pictures)
        holder.eventName.text = eventViewModel.eventName
        holder.eventDescription.text = eventViewModel.eventDescription
        holder.eventDate.text = eventViewModel.eventStartDate

    }

    override fun getItemCount(): Int {
        return eventList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val eventPicture: ImageView = this.itemView.findViewById(R.id.eventPicture)
        val eventName: TextView = this.itemView.findViewById(R.id.eventName)
        val eventDescription: TextView = this.itemView.findViewById(R.id.eventDescription)
        val eventDate: TextView = this.itemView.findViewById(R.id.eventDate)

    }
}