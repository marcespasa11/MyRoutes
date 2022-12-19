package com.maresgon.myroutes.Api_Interface

data class DirectionsResponse(
    val status: String,
    val routes: List<Route>
)

data class Route(
    val summary: String,
    val legs: List<Leg>
)

data class Leg(
    val distance: Distance,
    val duration: Duration,
    val endAddress: String,
    val startAddress: String,
    val steps: List<Step>
)

data class Step(
    val distance: Distance,
    val duration: Duration,
    val endLocation: LatLng,
    val startLocation: LatLng,
    val htmlInstructions: String,
    val travelMode: String
)

data class Distance(
    val text: String,
    val value: Int
)

data class Duration(
    val text: String,
    val value: Int
)

data class LatLng(
    val lat: Double,
    val lng: Double
)

