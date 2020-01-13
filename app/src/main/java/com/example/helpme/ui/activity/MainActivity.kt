package com.example.helpme.ui.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.helpme.Database.DatabaseHelpMe.DBHelpMe.COLUMN_EMAIL
import com.example.helpme.Database.DatabaseHelpMe.DBHelpMe.COLUMN_NAME
import com.example.helpme.Database.DatabaseHelpMe.DBHelpMe.COLUMN_PHONE
import com.example.helpme.Database.DatabaseHelpMe.DBHelpMe.COLUMN_USER
import com.example.helpme.Database.DependentDatabase
import com.example.helpme.R
import com.example.helpme.adapter.ListDependentAdapter
import com.example.helpme.model.Dependent
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {


    private val dependents: MutableList<Dependent> = mutableListOf()

    private val dependent: Dependent = Dependent("Thales", "thales@gmail.com", "11977973346", 1)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        configuraBotaoAdicionar()

        val dbDependent = DependentDatabase(this)
        val cursor = dbDependent.getAllDependent()

        dbDependent.insert(dependent)
        if (cursor != null) {
            val name = (cursor.getString(cursor.getColumnIndex(COLUMN_NAME)))
            val email = (cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL)))
            val phone = (cursor.getString(cursor.getColumnIndex(COLUMN_PHONE)))
            val userId = (cursor.getInt(cursor.getColumnIndex(COLUMN_USER)))
            dependents.add(Dependent(name, email, phone, userId))
        }
        cursor!!.moveToFirst()
        while (cursor.moveToNext()) {

            val name = (cursor.getString(cursor.getColumnIndex(COLUMN_NAME)))
            val email = (cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL)))
            val phone = (cursor.getString(cursor.getColumnIndex(COLUMN_PHONE)))
            val userId = (cursor.getInt(cursor.getColumnIndex(COLUMN_USER)))
            dependents.add(Dependent(name, email, phone, userId))
        }
        cursor.close()
        configuraLista(dependents)
    }

    private fun configuraBotaoAdicionar() {
        botao_novo_usuario.setOnClickListener {
            val intent = Intent(this, FormActivity::class.java)
            startActivity(intent)
        }
    }

    private fun configuraLista(dependents: List<Dependent>) {
//        lista_usuario_recyclerView.layoutManager = LinearLayoutManager(this)
//        lista_usuario_recyclerView.adapter = RecyclerAdapter(usuarios)
        lista_usuarios_listview.adapter = ListDependentAdapter(dependents, this)

    }
}
