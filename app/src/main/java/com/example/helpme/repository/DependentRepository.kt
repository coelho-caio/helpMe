package com.example.helpme.repository

import android.util.Log
import com.example.helpme.model.Dependent
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.gson.Gson
import com.google.gson.JsonObject

class DependentRepository {
    private val db = FirebaseFirestore.getInstance()
    val gson = Gson()
    fun getAll(userId: String): Task<QuerySnapshot> {


    return db.collection("dependents").whereEqualTo("userId",userId).get().addOnSuccessListener { result ->
            for (dependent in result) {
                Log.w("${dependent.id}", "${dependent.data}")
                /*val dependentString = dependent.data.toString()
                val jsonObject = gson.fromJson(dependentString,Dependent::class.java)*/
            }
        }
    }

        fun getDependent(dependentId: String): DocumentReference {

            return db.collection("dependents").document(dependentId)
//        docRef.get()
//            .addOnSuccessListener { document ->
//                if (document != null) {
//                    Log.d("exist", "DocumentSnapshot data: ${document.data}")
//                } else {
//                    Log.d("noexist", "No such document")
//                }
//            }
        }
    }