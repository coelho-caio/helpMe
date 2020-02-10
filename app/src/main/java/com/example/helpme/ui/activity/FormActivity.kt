package com.example.helpme.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.helpme.database.DependentDatabase
import com.example.helpme.R
import com.example.helpme.model.Dependent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.formulario_usuario.*

class FormActivity : AppCompatActivity() {
    private val dependents: MutableList<Dependent> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        var db = FirebaseFirestore.getInstance()

        val mAuth = FirebaseAuth.getInstance()
        var user = mAuth.currentUser

        super.onCreate(savedInstanceState)
        setContentView(R.layout.formulario_usuario)

        botao_salvar_usuario.setOnClickListener {
            val email = form_usuario_email.text.toString()
            val name = form_usuario_nome.text.toString()
            val phone = form_usuario_telefone.text.toString()
            val userId = user!!.uid
            val dependent = Dependent(name, email, phone, userId)
            db.collection("dependents").add(dependent).addOnSuccessListener { documentReference ->

                Log.d("FormActivity", "DocumentSnapshot added with ID: ${documentReference.id}")
            }
////            val dbDependent = DependentDatabase(this)
////            dbDependent.insert(dependent)
//
            val intent = Intent(this, DashboardActivity::class.java)
            startActivity(intent)
        }
    }
}
