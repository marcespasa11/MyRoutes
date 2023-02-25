package com.maresgon.myroutes

import android.graphics.Color
import android.graphics.PointF.length
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.maresgon.myroutes.Classes.Post
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.card_layout.view.*


class RecyclerAdapter (options: FirestoreRecyclerOptions<Post>) :
    FirestoreRecyclerAdapter<Post, RecyclerAdapter.PostAdapterVH>(options) {

    lateinit var parent: ViewGroup
    val db = FirebaseFirestore.getInstance()
    private lateinit var googleMap: GoogleMap
    private lateinit var mapView : MapView



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostAdapterVH {

        return PostAdapterVH(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.card_layout, parent, false)

        )
    }

    fun obtenerPuntosDeLinea(stringDeCoordenadas: String?): List<LatLng> {
        val puntosDeLinea = mutableListOf<LatLng>()

        val elementosDelMapa = stringDeCoordenadas?.removeSurrounding("{0=[", "]}")?.split(", ")
        if (elementosDelMapa != null) {
            for (coordenada in elementosDelMapa) {
                val (latitud, longitud) = coordenada.split(": ")[1].removeSurrounding("(", ")").split(",")
                        val punto = LatLng(latitud.toDouble(), longitud.toDouble())
                        puntosDeLinea.add(punto)
            }
        }

        return puntosDeLinea
    }

    override fun onBindViewHolder(
        holder: RecyclerAdapter.PostAdapterVH,
        position: Int,
        model: Post
    ) {
        val ETSInf = LatLng(39.4822022, -0.3482144)
        holder.itemTitle.text = model.name
        holder.itemDescription.text = model.description
        holder.itemLocation.text = model.location //"${model.city}, ${model.country}"
        holder.itemDistance.text = model.distance
        holder.itemDuration.text = model.duration
        holder.itemDifficulty.text = model.difficulty
        holder.itemKindOfActivity.text = model.kindOfActivity


        when(model.difficulty) {
            "Easy" -> holder.itemView.setBackgroundColor((Color.parseColor("#98fb98")))
            "Medium" -> holder.itemView.setBackgroundColor((Color.parseColor("#ffcb94")))
            "Hard" -> holder.itemView.setBackgroundColor((Color.parseColor("#FFC0CB")))//"#ff94ff")))
        }

        when(model.kindOfActivity) {
            "Walking" -> holder.itemImageActivity.setImageResource(R.drawable.walking)
            "Running" -> holder.itemImageActivity.setImageResource(R.drawable.running)
            "Cycling" -> holder.itemImageActivity.setImageResource(R.drawable.cycling)
        }

        if (model.accesibility == true) {
            holder.itemAccesibility.setImageResource(R.drawable.accesibilidad_universal)
            holder.itemTextAccesibility.setText("Accesible")
        }

        if (model.liked == true) {
            holder.itemLikeButton.setImageResource(R.drawable.button_like_clicked)
        }

        holder.itemLikeButton.setOnClickListener {
            if (model.liked == false) {
                // Actualiza la apariencia del botón
                holder.itemLikeButton.setImageResource(R.drawable.button_like_clicked)

                // Guarda la información de "Me gusta" en tu modelo de datos
                model.liked = true

                // Actualiza el documento correspondiente en la base de datos
                val documentId = snapshots.getSnapshot(holder.adapterPosition).id
                db.collection("posts").document(documentId).update("liked", true)
            } else {
                // Actualiza la apariencia del botón
                holder.itemLikeButton.setImageResource(R.drawable.button_disliked)

                // Guarda la información de "Me gusta" en tu modelo de datos
                model.liked = true

                // Actualiza el documento correspondiente en la base de datos
                val documentId = snapshots.getSnapshot(holder.adapterPosition).id
                db.collection("posts").document(documentId).update("liked", false)
            }
        }

        var points = obtenerPuntosDeLinea(model.routeLine)

        holder.mapView.onCreate(null)
        holder.mapView.onResume()
        holder.mapView.getMapAsync { googleMap ->
            this.googleMap = googleMap
            var location = ETSInf

            val lineOptions = PolylineOptions()
                .color(Color.RED)
                .width(5f)
                .clickable(true)

            if (points != null) {
                for (point in points) {
                    lineOptions.add(point)
                    location = point
                }
            }

            googleMap.addPolyline(lineOptions)

            //googleMap.addMarker(MarkerOptions().position(location).title(model.name))
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 12.5f))
        }

        //foto
        var path = model.path
        if (path == "")
            path = model.kindOfActivity.toString().lowercase() + ".png"

        holder.itemView.setOnClickListener(object: View.OnClickListener {
            override fun onClick(v: View?) {
                val bundle = Bundle()
                bundle.putString("name", model.name)
                //bundle.putString("duration", model.duration.toString())
                //bundle.putString("kindOfActivity", model.kindOfActivity)
                bundle.putString("location", model.location)
                bundle.putString("description", model.description)
            }
        })

    }


    class PostAdapterVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var itemTitle = itemView.item_title
        var itemDescription = itemView.item_description
        var itemLocation = itemView.item_location
        var itemDuration = itemView.item_duration
        var itemDistance = itemView.item_distance
        var itemDifficulty = itemView.item_difficulty
        var itemKindOfActivity = itemView.item_kindOfActivity
        var itemImageActivity = itemView.imageView
        var itemAccesibility = itemView.imageAccesibility
        var itemTextAccesibility = itemView.item_accesibility
        var itemLikeButton = itemView.item_likeButton
        var mapView: MapView = itemView.findViewById(R.id.mapView)

        /**Image**///var itemImage = itemView.item_image
    }
}
