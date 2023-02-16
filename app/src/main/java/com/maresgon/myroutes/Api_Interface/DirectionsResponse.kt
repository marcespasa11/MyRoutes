package com.maresgon.myroutes.Api_Interface

import com.google.android.gms.maps.model.LatLng

data class DirectionsResponse(
    //val geocoded_waypoints: List<GeocodedWaypoint>,
    val status: String?,
    val routes: List<Route>?
)

data class Route(
    val summary: String?,
    val legs: List<Leg>?
)

data class Leg(
    val steps: List<Step>?

)

data class Step(
    val distance: Distance?,
    val duration: Duration?,
    val endLocation: CustomeLatLng?,
    val startLocation: CustomeLatLng?,
    val htmlInstructions: String?,
    val travelMode: String?,
    val polyline: Polyline?
)

data class Distance(
    val text: String?,
    val value: Int?
)

data class Duration(
    val text: String?,
    val value: Int?
)
data class Polyline(
    val points: String
)

data class CustomeLatLng(
    val lat: Double?,
    val lng: Double?
)

