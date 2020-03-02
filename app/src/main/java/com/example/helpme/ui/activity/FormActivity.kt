package com.example.helpme.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.helpme.R
import com.example.helpme.model.Dependent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.formulario_usuario.*


class FormActivity : AppCompatActivity() {
    private lateinit var db: FirebaseFirestore
    private lateinit var dependent: Dependent

    override fun onCreate(savedInstanceState: Bundle?) {
        db = FirebaseFirestore.getInstance()

        val mAuth = FirebaseAuth.getInstance()
        var user = mAuth.currentUser

        super.onCreate(savedInstanceState)
        setContentView(R.layout.formulario_usuario)
        var hasDependent = loadDependent()



        insertOrUpdateDependent(hasDependent, user!!.uid)


    }

    private fun insertOrUpdateDependent(hasDependent: Boolean, userId: String) {
        botao_salvar_usuario.setOnClickListener {

            val email = form_usuario_email.text.toString()
            val name = form_usuario_nome.text.toString()
            val phone = form_usuario_telefone.text.toString()
            val userId = userId
            dependent = Dependent(name, email, phone, userId)

            if (hasDependent) {
                var docRef = db.collection("dependents").document("blkjv6rFTBblHBBh6WQ3JF19onj1")
                docRef.get()
                    .addOnSuccessListener { document ->
                        if (document != null) {
                            Log.d("exist", "DocumentSnapshot data: ${document.data}")
                        } else {
                            Log.d("noexist", "No such document")
                        }
                    }
            } else {
                db.collection("dependents").add(dependent)
                    .addOnSuccessListener { documentReference ->
                        Log.d(
                            "FormActivity",
                            "DocumentSnapshot added with ID: ${documentReference.id}"
                        )
                    }
            }

            val intent = Intent(this, DashboardActivity::class.java)
            startActivity(intent)
        }

    }

    private fun loadDependent(): Boolean {
        val data = intent

        if (data.hasExtra("emailDependent")) {
            title = "Editar Dependente"
            val name = data.getSerializableExtra("nameDependent") as String
            val email = data.getSerializableExtra("emailDependent") as String
            val phone = data.getSerializableExtra("phoneDependent") as String

            setData(email, name, phone)
            return true
        }
        title = "Incluir Dependente"
        return false
    }

    private fun setData(email: String, name: String, phone: String) {
        form_usuario_email.setText(email)
        form_usuario_nome.setText(name)
        form_usuario_telefone.setText(phone)

    }
}
