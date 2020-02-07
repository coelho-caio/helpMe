package com.example.helpme

import UserDatabase
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.helpme.ui.activity.DashboardActivity
import kotlinx.android.synthetic.main.activity_login.*

class Login : AppCompatActivity() {

    lateinit var btLogin: Button
    lateinit var btRegister: Button
    lateinit var etEmailLogin: EditText
    lateinit var etPassword: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
            val db = UserDatabase(this)
            if (db.readEmail(etEmailLogin.text.toString()) && db.readPassword(etPassword.text.toString())) {
                val intent = Intent(this, DashboardActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "errrrrrrou", Toast.LENGTH_LONG).show()
            }
        }

        btRegister.setOnClickListener {
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
        }
    }
}
