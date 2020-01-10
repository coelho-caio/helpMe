package com.example.helpme

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.helpme.Database.LoginDataBaseHelper

class Login : AppCompatActivity() {

    lateinit var btLogin: Button

    lateinit var btRegister: Button

    lateinit var etName:EditText

    lateinit var etPassword:EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        initViews()
        setupListener()

    }

    private fun initViews() {
        btLogin=findViewById(R.id.bt_login_sign_in)
        etName = findViewById(R.id.et_login_name)
        etPassword = findViewById(R.id.et_login_password)
        btRegister= findViewById(R.id.bt_login_register)
    }

    fun setupListener(){
        btLogin.setOnClickListener {
            val db = LoginDataBaseHelper(this)
            if ( db.readEmail(etName.text.toString()) && db.readPassword(etPassword.text.toString())) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
            else{
                Toast.makeText(this,"errrrrrrou", Toast.LENGTH_LONG).show()
            }
        }

        btRegister.setOnClickListener {
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
        }
    }
}
