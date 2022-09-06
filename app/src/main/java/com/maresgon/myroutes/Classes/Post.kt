package com.maresgon.myroutes.Classes


import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties

class Post {
    var id:String ?= null
    var name:String ?= null
    var distance:String ?= null
    var difficulty:String ?= null
    var duration:Int ?= null
    var description:String ?= null
    var location:String ?= null
    var kindOfActivity:String ?= null
    var path: String ?= null
    var user: String ?= null
}