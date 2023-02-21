package com.maresgon.myroutes.Fragments

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.navigation.Navigation
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.maresgon.myroutes.Activities.LoggedActivity
import com.maresgon.myroutes.Activities.SharedViewModel
import com.maresgon.myroutes.Classes.Post
import com.maresgon.myroutes.R
import kotlinx.android.synthetic.main.fragment_publish.*
import java.text.DecimalFormat


class PublishFragment : Fragment() {

    val decimalFormat = DecimalFormat("#.##")
    val sharedViewModel: SharedViewModel by activityViewModels()
    val db =  Firebase.firestore
    var newpost:Post = Post()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        super.onCreate(savedInstanceState)
        val v: View = inflater.inflate(R.layout.fragment_publish, container, false)

        val publish_button: TextView = v.findViewById(R.id.button_publish)

        sharedViewModel.selectedPlace.observe(
            viewLifecycleOwner,
        ) { routePost ->
            val distanceInMeters = routePost.distance
            val durationInSeconds = routePost.duration

            // Convertimos la distancia a kilómetros y la duración a horas
            val distanceInKilometers = decimalFormat.format(distanceInMeters!!.toDouble() / 1000)
            val durationInHours = decimalFormat.format(durationInSeconds!!.toDouble() / 3600)


            // Actualizamos los valores de la nueva publicación
            newpost.distance = "$distanceInKilometers km"
            newpost.duration = "$durationInHours h"
            v.findViewById<TextView>(R.id.item_distance).text = newpost.distance.toString()
            v.findViewById<TextView>(R.id.item_duration).text = newpost.duration.toString()
            /**Linea de punts a firebase**///newpost.routeLine = routePost.routeLine
        }

        publish_button.setOnClickListener {

            //newpost.id =
            newpost.name = item_titlePost.text.toString().trim()
            newpost.difficulty = item_difficulty.selectedItem.toString()
            newpost.description = item_descPost.text.toString().trim()
            newpost.location = item_loc.text.toString()
            newpost.kindOfActivity = item_kindOfAct.selectedItem.toString()
            newpost.accesibility = item_acces.isChecked




            //val newpost = Post()

            /*var post = hashMapOf(
                "description" to "Alan",
                "difficulty" to "High",
                "distance" to "10Km",
                "duration" to 30,
                "kindOfActivity" to "biking",
                "location" to "Cullera",
                "name" to "Muntanyeta de cullera",
                "path" to ""
            )*/

// Add a new document with a generated ID
            db.collection("posts")
                .add(newpost)
                .addOnSuccessListener { documentReference ->
                    Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error adding document", e)
                }
            startActivity(Intent(activity, LoggedActivity::class.java))





        }

        //uploadPost()
        return v
    }

    /*private fun uploadPost(){


    }*/
}
