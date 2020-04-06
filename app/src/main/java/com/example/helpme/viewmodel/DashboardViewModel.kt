package com.example.helpme.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.helpme.model.Dependent
import com.example.helpme.business.DashboardBusiness
import com.example.helpme.repository.DependentRepository
import com.google.firebase.auth.FirebaseUser

class DashboardViewModel(business: DashboardBusiness) : ViewModel() {

    private val business = business
    private val repository = DependentRepository()
    var dependents: MutableList<Dependent> = mutableListOf()
    var isDependentloaded =false


    fun calculerteAcelerate(x: Double, y: Double, z: Double): Double{
        return business.calculateAcelerate(x,y,z)
    }
    fun checkUser(): FirebaseUser?{
        return business.checkUser()
    }

    fun configureDependents(userId: String) {
    }

    fun configureList(isDependentloaded: Boolean): Boolean {
        return isDependentloaded
    }

    fun editDependent(dependent: Dependent){
        val dependentDoc = repository.getDependent("blkjv6rFTBblHBBh6WQ3JF19onj1")
        dependentDoc.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    Log.d("exist", "DocumentSnapshot data: ${document.data}")
                } else {
                    Log.d("noexist", "No such document")
                }
            }
    }

}