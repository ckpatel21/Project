package com.example.capstone.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.capstone.R
import com.example.capstone.model.RatingReview

class ShowReviewAdapter(private val ratingReviewList: ArrayList<RatingReview>)  :
    RecyclerView.Adapter<ShowReviewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_review, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ratingData = ratingReviewList[position]

        holder.placeRating.text = ratingData.rating.toString()
        holder.placeReview.text = ratingData.review
        holder.ratingAddedBy.text = ratingData.userEmail
    }


    override fun getItemCount(): Int {
        return ratingReviewList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val placeRating: TextView = this.itemView.findViewById(R.id.tvRating)
        val placeReview: TextView = this.itemView.findViewById(R.id.tvReview)
        val ratingAddedBy: TextView = this.itemView.findViewById(R.id.tvUser)
    }

}