package com.maresgon.myroutes.Fragments


import android.app.Application
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMapClickListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.maresgon.myroutes.R

import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.MapsInitializer.Renderer
import com.google.android.gms.maps.OnMapsSdkInitializedCallback
import com.google.android.gms.maps.model.PolylineOptions
import com.maresgon.myroutes.Api_Interface.DirectionsApiService
import com.maresgon.myroutes.Api_Interface.DirectionsResponse
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

    private lateinit var tapTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        super.onCreate(savedInstanceState)
        val v: View =  inflater.inflate(R.layout.fragment_map, container, false)

        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

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

        rutePoints.add(LatLng(39.482708,-0.351380))
        rutePoints.add(LatLng(39.481234,-0.349666))
        rutePoints.add(LatLng(39.478652,-0.346002))

        val polylineOptions = PolylineOptions()
            .addAll(rutePoints)
            .geodesic(true)
        googleMap.addPolyline(polylineOptions)

        googleMap.moveCamera(CameraUpdateFactory.newLatLng(ETSInf))

        /**Gestionar clicks**/
        googleMap.setOnMapClickListener{ point ->
            googleMap.addMarker(MarkerOptions().position(point))
        }
    }
    val retrofit = Retrofit.Builder()
        .baseUrl("https://maps.googleapis.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService = retrofit.create(DirectionsApiService::class.java)

/*
    val call = apiService.getDirections("Seattle", "San Francisco", "YOUR_API_KEY")
    call.enqueue(object : Callback<DirectionsResponse> {
        override fun onResponse(call: Call<DirectionsResponse>, response: Response<DirectionsResponse>) {
            if (response.isSuccessful) {
                val directions = response.body()
                // procesar las direcciones aquí
            } else {
                // manejar el error aquí
            }
        }

        override fun onFailure(call: Call<DirectionsResponse>, t: Throwable) {
            // manejar el error aquí
        }
    })
*/
}
