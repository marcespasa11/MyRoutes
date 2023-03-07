package com.maresgon.myroutes.Fragments


import android.app.Application
import android.content.ContentValues
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.maresgon.myroutes.R
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.MapsInitializer.Renderer
import com.google.android.gms.maps.OnMapsSdkInitializedCallback
import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.android.PolyUtil
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.maresgon.myroutes.Activities.LoggedActivity
import com.maresgon.myroutes.Activities.SharedViewModel
import com.maresgon.myroutes.Api_Interface.DirectionsApiService
import com.maresgon.myroutes.Api_Interface.DirectionsResponse
import com.maresgon.myroutes.BuildConfig.MAPS_API_KEY
import com.maresgon.myroutes.Classes.Post
import kotlinx.android.synthetic.main.fragment_map.*
import kotlinx.android.synthetic.main.fragment_publish.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

internal class MapRendererOptInApplication : Application(), OnMapsSdkInitializedCallback {
    override fun onCreate() {
        super.onCreate()
        MapsInitializer.initialize(applicationContext, Renderer.LATEST, this)
    }

    override fun onMapsSdkInitialized(renderer: MapsInitializer.Renderer) {
        when (renderer) {
            Renderer.LATEST -> Log.d("MapsDemo", "The latest version of the renderer is used.")
            Renderer.LEGACY -> Log.d("MapsDemo", "The legacy version of the renderer is used.")
        }
    }
}

class MapFragment(val sharedViewModel: SharedViewModel) : Fragment(), OnMapReadyCallback {



    val routePost: Post = Post()
    /**Linea de punts a firebase**/val myRouteLine = hashMapOf<Int, List<LatLng>>()
    /**Linea de punts a firebase**/val routeLine = hashMapOf<String, List<LatLng>>()
    var totalDistance: Double = 0.0
    var totalDuration: Double = 0.0


    val retrofit = Retrofit.Builder()
        .baseUrl("https://maps.googleapis.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService = retrofit.create(DirectionsApiService::class.java)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        super.onCreate(savedInstanceState)
        val v: View =  inflater.inflate(R.layout.fragment_map, container, false)

        //val drawRouteButton: Button? = view?.findViewById(R.id.drawRouteButton)
        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val addInfoButton: Button? = v.findViewById(R.id.button_addInfo)
        addInfoButton?.setOnClickListener {
            val fragment = PublishFragment()
            val transaction = fragmentManager?.beginTransaction()
            transaction?.replace(R.id.flFragment, fragment)
            transaction?.addToBackStack(null)
            transaction?.commit()
            routePost.distance = totalDistance.toString()
            routePost.duration = totalDuration.toString()
            /**Linea de punts a firebase**///routeLine.putAll(myRouteLine.mapKeys { it.key.toString() })
            /**Linea de punts a firebase**/routePost.routeLine = this.myRouteLine.toString()
            sharedViewModel.selectedPlace.value = routePost
        }

        return v
    }

    override fun onMapReady(googleMap: GoogleMap) {

        var ETSInf = LatLng(39.4822022, -0.3482144)

        /**Dibuixar linies**/
        var rutePoints : ArrayList<LatLng> = ArrayList<LatLng>()

        googleMap.moveCamera(CameraUpdateFactory.newLatLng(ETSInf))

        /**Gestionar clicks**/
        googleMap.setOnMapClickListener{ point ->
            googleMap.addMarker(MarkerOptions().position(point))
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(point))
            rutePoints.add(LatLng(point.latitude, point.longitude))

        }

        /**Crida API Directions**/
        button_drawRoute?.setOnClickListener {
            drawRoute(rutePoints, googleMap)
        }


    }

    fun drawRoute(rutePoints: ArrayList<LatLng>, googleMap: GoogleMap) {
        val origin = "${rutePoints.first().latitude},${rutePoints.first().longitude}"
        val destination = "${rutePoints.last().latitude},${rutePoints.last().longitude}"
        val waypoints = rutePoints.subList(1, rutePoints.size - 1).joinToString("|") { "via:${it.latitude},${it.longitude}" }
        val optimizedWaypoints = "optimize:true|$waypoints"

        apiService.getDirections(origin, destination, optimizedWaypoints,"walking", MAPS_API_KEY, ).enqueue(object : Callback<DirectionsResponse> {
            override fun onResponse(call: Call<DirectionsResponse>, response: Response<DirectionsResponse>) {
                if (response.isSuccessful) {
                    /**Linea de punts a firebase**/var lineCounter = 0
                    for (leg in response.body()?.routes?.firstOrNull()?.legs ?: emptyList()) {
                        val points = leg.steps
                            ?.flatMap { PolyUtil.decode(it.polyline?.points) }
                            ?.ifEmpty { null }
                            ?: emptyList()

                        totalDistance += leg.distance?.value!!.toDouble()
                        totalDuration += leg.duration?.value!!.toDouble()

                        /**Linea de punts a firebase**/myRouteLine[lineCounter] = points //añade los puntos a routeLine

                        val lineOptions = PolylineOptions()
                            .color(Color.RED)
                            .width(5f)
                            .clickable(true)

                        for (point in points) {
                            lineOptions.add(point)
                        }

                        googleMap.addPolyline(lineOptions)
                        /**Linea de punts a firebase**/lineCounter++
                    }
                } else {
                    // manejar error
                }
            }

            override fun onFailure(call: Call<DirectionsResponse>, t: Throwable) {
                // manejar error
            }
        })
    }
}
