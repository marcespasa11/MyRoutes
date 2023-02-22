package com.maresgon.myroutes

//import com.maresgon.myroutes.R.layout.card_layout

import android.app.Application
import com.maresgon.myroutes.R.layout.card_layout
//C:\Users\Marc\Desktop\Uni Wroclaw\TFG\app\src\main\res\layout\card_layout.xml
//card_layout.view.*

import android.graphics.Color
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
import com.google.firebase.firestore.FirebaseFirestore
import com.maresgon.myroutes.Classes.Post
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.card_layout.view.*


class RecyclerAdapter (options: FirestoreRecyclerOptions<Post>) :
    FirestoreRecyclerAdapter<Post, RecyclerAdapter.PostAdapterVH>(options) {
// : RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    /*private var titles = arrayOf("Route One", "Route Two", "Route Three", "Route Four", "Route Five", "Route Six", "Route Seven", "Route Eight")

    private var locations = arrayOf("loc 1", "loc 2", "loc 3", "loc 4", "loc 5", "loc 6", "loc 7", "loc 8")

    private var descriptions = arrayOf("desc 1", "desc 2", "desc 3", " desc 4", "desc 5", "desc 6", "desc 7", "desc 8")

    private var images = intArrayOf(R.drawable.ruta4, R.drawable.ruta4, R.drawable.ruta4, R.drawable.ruta4,
        R.drawable.ruta4, R.drawable.ruta4, R.drawable.ruta4, R.drawable.ruta4)

     */
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

        holder.mapView.onCreate(null)
        holder.mapView.onResume()
        holder.mapView.getMapAsync { googleMap ->
            this.googleMap = googleMap
            val location = ETSInf
            googleMap.addMarker(MarkerOptions().position(location).title(model.name))
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 13.0f))
        }

        //foto
        var path = model.path
        if (path == "")
            path = model.kindOfActivity.toString().lowercase() + ".png"

        /**En cas de insertar image*/
        /*
        Picasso
            .get()
            .load("https://firebasestorage.googleapis.com/v0/b/shelted-d5576.appspot.com/o/${path}?alt=media&token=f95e312c-97ac-468c-a281-5f0eea32b5a7")
            .resize(1000, 1000)
            .centerCrop()
            .into(holder.itemImage)

         */

        holder.itemView.setOnClickListener(object: View.OnClickListener {
            override fun onClick(v: View?) {
                val bundle = Bundle()
                bundle.putString("name", model.name)
                //bundle.putString("duration", model.duration.toString())
                //bundle.putString("kindOfActivity", model.kindOfActivity)
                bundle.putString("location", model.location)
                bundle.putString("description", model.description)

                //Future implementation: Cambiar al fragment post al pulsar en un post

                /*val activity=v!!.context as AppCompatActivity
                val postFragment = postFragment()
                postFragment.arguments = bundle
                val fragmentManager: FragmentManager = activity.supportFragmentManager
                val transaction = fragmentManager.beginTransaction()
                transaction.replace(R.id.logged_activity,postFragment)
                transaction.addToBackStack(null)
                transaction.commit()*/
            }
        })


        //Future implementation: favourites
        /*val states = arrayOf(
            intArrayOf(android.R.attr.state_checked),
            intArrayOf(-android.R.attr.state_checked))

        val colors = intArrayOf(
            Color.parseColor("#FFFF00"),
            Color.parseColor("#FFFFFF"))

        holder.checkBoxFavourite.buttonTintList = ColorStateList(states, colors)

        holder.checkBoxFavourite.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {

            }
            else {

            }*/
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

        //var checkBoxFavourite = itemView.checkBox_Favourite   //Future implementation
    }



    /*override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapter.ViewHolder {

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
    }*/
}
