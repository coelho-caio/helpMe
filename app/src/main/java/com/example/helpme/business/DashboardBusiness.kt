package com.example.helpme.business

import com.example.helpme.business.BaseBusiness
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class DashboardBusiness: BaseBusiness {
    private lateinit var mAuth: FirebaseAuth

    fun calculateAcelerate(x: Double, y: Double, z: Double) : Double {
        val eixox=Math.pow(x,2.0)
        val eixoy=Math.pow(y,2.0)
        val eixoz=Math.pow(z,2.0)

        return Math.sqrt(eixox+eixoy+eixoz)
    }

    fun checkUser(): FirebaseUser? {
        mAuth = FirebaseAuth.getInstance()
        val user = mAuth.currentUser
        return user
    }
}