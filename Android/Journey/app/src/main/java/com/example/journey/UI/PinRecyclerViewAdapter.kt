package com.example.journey.UI

import com.example.journey.R
import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import de.hdodenhof.circleimageview.CircleImageView

class PinRecyclerViewAdapter(val names: ArrayList<String>, val imageUrls: ArrayList<String>) : RecyclerView.Adapter<PinRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):  ViewHolder{
        val view : View = LayoutInflater.from(parent.context).inflate(com.example.journey.R.layout.profile_pin_listitem, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val redRNG = (1..255).random()
        val blueRNG = (1..255).random()
        val greenRNG = (1..255).random()
        holder.pinName.text = names[position]
        holder.pinImage.setBackgroundColor(Color.rgb(redRNG,greenRNG,blueRNG))
    }

    override fun getItemCount(): Int {
        return names.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val pinImage: CircleImageView = itemView.findViewById(R.id.pin_image)
        val pinName: TextView = itemView.findViewById(R.id.pin_name)
    }
}