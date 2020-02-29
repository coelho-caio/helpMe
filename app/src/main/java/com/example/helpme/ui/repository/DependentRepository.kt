package com.example.helpme.ui.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

class DependentRepository {
    private val db = FirebaseFirestore.getInstance()
    fun getAll(userId: String): Task<QuerySnapshot> {

        return db.collection("dependents")
            .whereEqualTo("userId", userId).get()
    }
}