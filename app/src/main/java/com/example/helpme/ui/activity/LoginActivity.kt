package com.example.helpme.ui.activity

import UserDatabase
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.helpme.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity() {

    lateinit var btLogin: Button
    lateinit var btRegister: Button
    lateinit var etEmailLogin: EditText
    lateinit var etPassword: EditText
    private lateinit var auth : FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        setContentView(R.layout.activity_login)
        initViews()
        setupListener()

    }

    private fun initViews() {
        btLogin = bt_login_sign_in
        etEmailLogin = et_login_email
        etPassword = et_login_password
        btRegister = bt_login_register
    }

    fun setupListener() {
        btLogin.setOnClickListener {
            val email = etEmailLogin.text.toString()
            val password = etPassword.text.toString()

            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, OnCompleteListener<AuthResult> { task ->
                if(task.isSuccessful){
                    var intent = Intent(this, DashboardActivity::class.java)
                    startActivity(intent)

                }else{
                    Toast.makeText(this, "Não foi possivel fazer Login", Toast.LENGTH_LONG).show()

                }
            })
        }

        btRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}
