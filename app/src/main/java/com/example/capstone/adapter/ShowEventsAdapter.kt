package com.example.capstone.adapter

import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.capstone.R
import com.example.capstone.model.Events
import com.squareup.picasso.Picasso
import java.io.InputStream
import java.net.URL


class ShowEventsAdapter(private val eventList: List<Events>, val shareBtnClickListener: ShareBtnClickListener, val layoutBtnClickListener : LayoutBtnClickListener) :
    RecyclerView.Adapter<ShowEventsAdapter.ViewHolder>() {

    companion object {
        var shareClickListener: ShareBtnClickListener? = null
        var layoutClickListener: LayoutBtnClickListener? = null
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_events, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val eventViewModel = eventList[position]

        Picasso.get().load(eventViewModel.pictures).into(holder.eventPicture)

        //holder.eventPicture.setIm(loadImageFromWebOperations(eventViewModel.pictures))
        holder.eventName.text = eventViewModel.eventName
        holder.eventDescription.text = eventViewModel.eventDescription
        holder.eventDate.text = eventViewModel.eventStartDate

        shareClickListener = shareBtnClickListener
        layoutClickListener = layoutBtnClickListener

        holder.shareBtn.setOnClickListener {
            if (shareClickListener != null){
                shareClickListener?.onShareBtnClick(position, eventViewModel.eventName, eventViewModel.eventStartDate)
            }
        }
        holder.eventLayout.setOnClickListener {
            if (layoutClickListener != null){
                layoutClickListener?.onLayoutClick(position, eventViewModel)
            }
        }
    }
    private fun loadImageFromWebOperations(url: String?): Drawable? {
        return try {
            val ins = URL(url).content as InputStream
            Drawable.createFromStream(ins, "src name")
        } catch (e: Exception) {
            null
        }
    }

    override fun getItemCount(): Int {
        return eventList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val eventLayout : CardView = this.itemView.findViewById(R.id.eventLayout)
        val eventPicture: ImageView = this.itemView.findViewById(R.id.eventPicture)
        val eventName: TextView = this.itemView.findViewById(R.id.eventName)
        val eventDescription: TextView = this.itemView.findViewById(R.id.eventDescription)
        val eventDate: TextView = this.itemView.findViewById(R.id.eventDate)
        val shareBtn : ImageView = this.itemView.findViewById(R.id.ivShareBtn)
    }

    open interface ShareBtnClickListener {
        fun onShareBtnClick(position: Int, eventName: String?, eventStartDate: String?)
    }
    open interface  LayoutBtnClickListener {
        fun onLayoutClick(position: Int, eventList : Events )
    }
}