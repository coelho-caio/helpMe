package com.example.helpme.adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.helpme.R
import com.example.helpme.model.Dependent
import kotlinx.android.synthetic.main.item_usuario.view.*


class RecyclerAdapter(private var dependents: MutableList<Dependent>) : RecyclerView.Adapter<CustomViewHolder>() {

    override fun getItemCount(): Int {
        return dependents.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {

        val layoutInflater = LayoutInflater.from(parent?.context)
        val cellForRow = layoutInflater.inflate(R.layout.item_usuario, parent, false)
//        for (i in 0..2){
//            val usuario = usuarios[i]
//
//            adicionaNome(usuario, viewCriada)
//            adicionaEmail(usuario, viewCriada)
//            adicionaTelefone(usuario, viewCriada)
//        }
        return CustomViewHolder(cellForRow)


    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val usuario = dependents.get(position)
        holder.view.item_usuario_nome.text = usuario.name
        holder.view.item_usuario_email.text = usuario.email
        holder.view.item_usuario_telefone.text = usuario.phone

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

}

class CustomViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

}

