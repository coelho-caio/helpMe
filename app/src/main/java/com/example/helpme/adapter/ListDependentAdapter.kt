package com.example.helpme.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.example.helpme.R
import com.example.helpme.model.Dependent
import kotlinx.android.synthetic.main.item_usuario.view.*

class ListDependentAdapter(
    private val dependents: List<Dependent>,
    private val context: Context
) : BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val viewCriada = LayoutInflater.from(context)
            .inflate(R.layout.item_usuario, parent, false)

        val dependent = dependents[position]

        adicionaNome(dependent, viewCriada)
        adicionaEmail(dependent, viewCriada)
        adicionaTelefone(dependent, viewCriada)

        return viewCriada
    }

    private fun adicionaNome(dependent: Dependent, viewCriada: View) {
        viewCriada.item_usuario_nome.text = dependent.name
    }

    private fun adicionaEmail(dependent: Dependent, viewCriada: View) {
        viewCriada.item_usuario_email.text = dependent.email
    }

    private fun adicionaTelefone(dependent: Dependent, viewCriada: View) {
        viewCriada.item_usuario_telefone.text = dependent.phone
    }

    override fun getItem(position: Int): Dependent {
        return dependents[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return dependents.size
    }

}
