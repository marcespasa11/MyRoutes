package com.maresgon.myroutes.Fragments

class CurrentSession private constructor() {
    private var userId: String? = null
    private var userName: String? = null
    private var userBirth: String? = null

    companion object {
        private var instance: CurrentSession? = null

        fun getInstance(): CurrentSession {
            if (instance == null) {
                instance = CurrentSession()
            }
            return instance as CurrentSession
        }
    }

    fun login(userId: String, userName: String, userBirth: String) {
        this.userId = userId
        this.userName = userName
        this.userBirth = userBirth
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
