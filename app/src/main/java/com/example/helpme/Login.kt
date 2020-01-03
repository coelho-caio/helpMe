package com.example.helpme

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText

class Login : AppCompatActivity() {

    lateinit var btLogin: EditText

    lateinit var etName:EditText

    lateinit var etPassword:EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        initViews()
        continueButton()

    }

    private fun initViews() {
        btLogin=findViewById(R.id.bt_login_sign_in)
        etName = findViewById(R.id.et_login_name)
        etPassword = findViewById(R.id.et_login_password)
    }

    fun continueButton(){
        btLogin.setOnClickListener{
            if (etName.text.toString()=="thaleco" && etPassword.text.toString()=="passeidireto"){
                val intent = Intent(this,MainActivity::class.java)
                startActivity(intent)
            }
        }
    }
}
