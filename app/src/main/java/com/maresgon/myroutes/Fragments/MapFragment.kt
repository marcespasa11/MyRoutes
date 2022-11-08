package com.maresgon.myroutes.Fragments


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
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(ETSInf))
        //googleMap.setOnMapClickListener(OnMapClickListener)
    }
    /**Gestionar clicks**/
    /*
    fun OnMapClickListener(point: LatLng) {
        tapTextView.text = "tapped, point=$point"
    }
     */

}
