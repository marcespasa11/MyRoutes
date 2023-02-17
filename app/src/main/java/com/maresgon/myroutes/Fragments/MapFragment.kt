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

class MapFragment : Fragment(), OnMapReadyCallback {

    val sharedViewModel: SharedViewModel by activityViewModels()

    val routePost: Post = Post()

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
            sharedViewModel.selectedPlace.value = routePost
        }

        return v
    }

    override fun onMapReady(googleMap: GoogleMap) {
        var ETSInf = LatLng(39.4822022, -0.3482144)
        googleMap.addMarker(
            MarkerOptions()
                .position(ETSInf)
                .title("ETSInf")
        )
        /**Dibuixar linies**/
        var rutePoints : ArrayList<LatLng> = ArrayList<LatLng>()
/*
        rutePoints.add(LatLng(39.482708,-0.351380))
        rutePoints.add(LatLng(39.481234,-0.349666))
        rutePoints.add(LatLng(39.478652,-0.346002))

        val polylineOptions = PolylineOptions()
            .addAll(rutePoints)
            .geodesic(true)
        googleMap.addPolyline(polylineOptions)*/

        googleMap.moveCamera(CameraUpdateFactory.newLatLng(ETSInf))

        /**Gestionar clicks**/
        googleMap.setOnMapClickListener{ point ->
            googleMap.addMarker(MarkerOptions().position(point))
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(point))
            rutePoints.add(LatLng(point.latitude, point.longitude))

        }

        /**Crida API Directions**/
        button_drawRoute?.setOnClickListener {
            println("Botó pulsat")
            drawRoute(rutePoints, googleMap)
        }


    }

    fun drawRoute(rutePoints: ArrayList<LatLng>, googleMap: GoogleMap) {
        for (i in 0 until rutePoints.size - 1) {
            val origin = "${rutePoints[i].latitude},${rutePoints[i].longitude}"
            val destination = "${rutePoints[i + 1].latitude},${rutePoints[i + 1].longitude}"

            apiService.getDirections(origin, destination, "walking", MAPS_API_KEY).enqueue(object : Callback<DirectionsResponse> {
                override fun onResponse(call: Call<DirectionsResponse>, response: Response<DirectionsResponse>) {
                    if (response.isSuccessful) {
                        val points = response.body()?.routes?.firstOrNull()?.legs?.firstOrNull()?.steps
                            ?.flatMap { PolyUtil.decode(it.polyline?.points) }
                            ?.ifEmpty { null }
                            ?: emptyList()


                        val lineOptions = PolylineOptions()
                            .color(Color.RED)
                            .width(5f)
                            .clickable(true)

                        for (point in points) {
                            lineOptions.add(point)
                        }

                        googleMap.addPolyline(lineOptions)
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
}
