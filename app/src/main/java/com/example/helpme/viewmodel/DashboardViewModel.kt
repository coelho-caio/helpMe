package com.example.helpme.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.helpme.model.Dependent
import com.example.helpme.business.DashboardBusiness
import com.example.helpme.repository.DependentRepository
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.QuerySnapshot

class DashboardViewModel(business: DashboardBusiness) : ViewModel() {

    private val business = business
    private val repository = DependentRepository()
    var dependents: MutableList<Dependent> = mutableListOf()


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

    fun deleteDependent(dependenteId: String?): Task<Void>? {
        if (dependenteId!=null) {
            return repository.getDependent(dependenteId).delete()
        }
        return null
    }

    fun getDependents(userId: String): Task<QuerySnapshot> {
       return repository.getAll(userId)

    }


}