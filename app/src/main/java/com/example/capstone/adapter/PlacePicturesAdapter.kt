package com.example.capstone.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.capstone.R

class PlacePictureAdapter(
    private val courseList: List<Uri>,
    private val context: Context
) : RecyclerView.Adapter<PlacePictureAdapter.PlacePictureViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlacePictureViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.layout_picture, parent, false)
        return PlacePictureViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlacePictureViewHolder, position: Int) {
        val currentUri = courseList[position]
        holder.bind(currentUri)
    }

    override fun getItemCount(): Int {
        return courseList.size
    }

    inner class PlacePictureViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val courseIV: ImageView = itemView.findViewById(R.id.imageView)

        fun bind(uri: Uri) {
            courseIV.setImageURI(uri)
        }
    }
}
