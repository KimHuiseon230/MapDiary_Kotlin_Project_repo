package com.example.mapdiary.activity

import android.support.multidex.MultiDexApplication
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MyApplication : MultiDexApplication() {
    companion object {
        // 1. 파이어베이스 인증 객체
        lateinit var firebaseAuth : FirebaseAuth

        var email : String? = null
        // 2. 파이어베이스 인증 유무
        fun checkAuth() : Boolean{
            var authFlag = false
            // 사용자 정보 가져오기
            var currentUser = firebaseAuth.currentUser
            if(currentUser != null){
                email = currentUser.email
                authFlag = currentUser.isEmailVerified
            }
            return authFlag
        }

    }
    override fun onCreate() {
        super.onCreate()
        firebaseAuth = Firebase.auth
    }
}