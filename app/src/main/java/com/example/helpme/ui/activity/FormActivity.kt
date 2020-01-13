package com.example.helpme.ui.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.example.helpme.R
import com.example.helpme.model.Dependent
import kotlinx.android.synthetic.main.formulario_usuario.*

class FormActivity : AppCompatActivity(){
    private val dependents :MutableList<Dependent> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.formulario_usuario)


        botao_salvar_usuario.setOnClickListener {

            val email = form_usuario_email.text.toString()
            val name = form_usuario_nome.text.toString()
            val phone = form_usuario_telefone.text.toString()
            val dependent = Dependent(name, email, phone, 1)
            dependents.add(dependent)
            Toast.makeText(this, email, Toast.LENGTH_SHORT).show()
        }
    }
}
