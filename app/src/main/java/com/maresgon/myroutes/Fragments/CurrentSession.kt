package com.maresgon.myroutes.Fragments

object CurrentSession {
    private var userId: String? = null
    private var userName: String? = null
    private var userBirth: String? = null

    fun login(userId: String, userName: String, userBirth: String) {
        CurrentSession.userId = userId
        CurrentSession.userName = userName
        CurrentSession.userBirth = userBirth
    }

    fun getUserId(): String? {
        return userId
    }

    fun getUserName(): String? {
        return userName
    }

    fun getUserBirth(): String? {
        return userBirth
    }
}
