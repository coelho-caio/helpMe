package com.example.helpme.ui.repository

import android.util.Log
import com.example.helpme.model.Dependent
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

class DashboardRepository {

    fun configuraDataBase(
        dependents: MutableList<Dependent>,
        user: FirebaseUser?
    ): MutableList<Dependent> {

//        val db = FirebaseFirestore.getInstance()
//
//        val documents = db.collection("dependents")
//            .whereEqualTo("userId", user!!.uid).get()
//
//       documents.addOnSuccessListener { result ->
//            for (document in result) {
//                val dependent = document.toObject(Dependent::class.java)
//                dependents.add(dependent)
//                Log.d("DashboardRepository", "${document.id} => ${document.data}")
//            }
//        }

            return dependents
    }
}