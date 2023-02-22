package com.maresgon.myroutes.Classes


import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.IgnoreExtraProperties
import com.maresgon.myroutes.Api_Interface.Route

@IgnoreExtraProperties

class Post {
    var id:String ?= null
    var name:String ?= null
    var distance:String ?= null
    var difficulty:String ?= null
    var duration:String ?= null
    var description:String ?= null
    var location:String ?= null
    var kindOfActivity:String ?= null
    var accesibility:Boolean ?= false
    var liked:Boolean ?= false
    var path: String ?= null
    /**Linea de punts a firebase**/var routeLine: String ?= null//HashMap<String, List<LatLng>>? = null
    var user: String ?= null
}
