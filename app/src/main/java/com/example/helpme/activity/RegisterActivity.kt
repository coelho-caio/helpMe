package com.example.helpme.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.helpme.R
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {

    lateinit var btRegister: Button
    lateinit var etEmail: EditText
    lateinit var etPassword: EditText
    lateinit var etPasswordConfirmation: EditText

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
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
            val isSuccess = validateFields(email, password, passwordConfirmation)
            if (isSuccess) {
                auth
                    .createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            val userId = auth.currentUser
                            val intent = Intent(this, DashboardActivity::class.java)
                            startActivity(intent)
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(
                                this, "Problemas para registrar",
                                Toast.LENGTH_SHORT
                            ).show()
                            Log.w("RegisterActivity", "createUserWithEmail:failure", task.exception)
                        }
                    }
            }
        }
    }

    private fun validateFields(
        email: String,
        password: String,
        passwordConfirmation: String
    ): Boolean {
        if (email.equals("") || password.equals("") || passwordConfirmation.equals("")) {
            Toast.makeText(this, "Existem campos vazios", Toast.LENGTH_LONG).show()
            return false
        }
        if (password != passwordConfirmation) {
            Toast.makeText(this, "As senhas são diferentes", Toast.LENGTH_LONG).show()
            return false
        }
        return true
    }
}
