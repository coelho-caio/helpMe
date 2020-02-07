package com.example.helpme

import UserDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.helpme.model.User

class Register : AppCompatActivity() {

    lateinit var btRegister: Button

    lateinit var etEmail: EditText

    lateinit var etPassword: EditText

    lateinit var etPasswordConfirmation: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        setupViews()
        setUpListener()
    }

    private fun setupViews() {
        btRegister = findViewById(R.id.bt_register)
        etEmail = findViewById(R.id.et_register_email)
        etPassword = findViewById(R.id.et_register_password)
        etPasswordConfirmation = findViewById(R.id.et_register_password_confirmation)

    }

    private fun setUpListener() {
        btRegister.setOnClickListener {
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()
            val passwordConfirmation = etPasswordConfirmation.text.toString()
            validateFields(email , password, passwordConfirmation)
        }
    }

    private fun validateFields(email: String, password: String, passwordConfirmation: String) {
        if (email.equals("") || password.equals("") || passwordConfirmation.equals("")) {
            Toast.makeText(this, "Existem campos vazios", Toast.LENGTH_LONG).show()
        } else {
            if (password != passwordConfirmation) {
                Toast.makeText(this, "As senhas são diferentes", Toast.LENGTH_LONG).show()
            } else {
                val dbHelper = UserDatabase(this)
                if (dbHelper.readEmail(email)){
                    Toast.makeText(this, "Email existente", Toast.LENGTH_LONG).show()
                }else{
                    val user = User(email,password)
                    val insert = dbHelper.insert(user)
                    if (insert){
                        Toast.makeText(this, "Registro realizado com sucesso", Toast.LENGTH_LONG).show()
                    }

                }
            }
        }
    }
}
