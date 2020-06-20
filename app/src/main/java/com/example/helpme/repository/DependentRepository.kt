package com.example.helpme.repository

import android.util.Log
import com.example.helpme.activity.DashboardActivity
import com.example.helpme.model.Dependent
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.gson.Gson
import com.google.gson.JsonObject

class DependentRepository {
    val db = FirebaseFirestore.getInstance().collection("dependents")

    fun getAll(userId: String): Task<QuerySnapshot> {

        return db.whereEqualTo("userId", userId).get().addOnSuccessListener { result ->
            for (dependent in result) {
                Log.w("${dependent.id}", "${dependent.data}")

            }
        }
    }

    fun getDependent(dependentId: String): DocumentReference {
        return db.document(dependentId)

        }
    }