package com.maresgon.myroutes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecyclerAdapter : RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    private var titles = arrayOf("Route One", "Route Two", "Route Three", "Route Four", "Route Five", "Route Six", "Route Seven", "Route Eight")

    private var locations = arrayOf("loc 1", "loc 2", "loc 3", "loc 4", "loc 5", "loc 6", "loc 7", "loc 8")

    private var descriptions = arrayOf("desc 1", "desc 2", "desc 3", " desc 4", "desc 5", "desc 6", "desc 7", "desc 8")

    private var images = intArrayOf(R.drawable.ruta4, R.drawable.ruta4, R.drawable.ruta4, R.drawable.ruta4,
        R.drawable.ruta4, R.drawable.ruta4, R.drawable.ruta4, R.drawable.ruta4)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.card_layout, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: RecyclerAdapter.ViewHolder, position: Int) {
        holder.itemTitle.text = titles[position]
        holder.itemLocation.text = locations[position]
        holder.itemDescription.text = descriptions[position]
        holder.itemImage.setImageResource(images[position])
    }

    override fun getItemCount(): Int {
        return titles.size
    }

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        var itemImage: ImageView
        var itemTitle: TextView
        var itemLocation: TextView
        var itemDescription: TextView

        init {
            itemImage = itemView.findViewById(R.id.item_image)
            itemTitle = itemView.findViewById(R.id.item_title)
            itemLocation = itemView.findViewById(R.id.item_location)
            itemDescription = itemView.findViewById(R.id.item_description)
        }
    }
}