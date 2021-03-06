package com.example.helpme.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.helpme.R
import com.example.helpme.model.Dependent
import com.example.helpme.model.DependentFromFirebase
import kotlinx.android.synthetic.main.item_usuario.view.*

class RecyclerAdapter(var dependents: MutableList<DependentFromFirebase>, private val itemClickListener : OnItemClickListener) :
    RecyclerView.Adapter<DependentsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): DependentsViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.item_usuario, parent, false)
        return DependentsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dependents.size
    }

    override fun onBindViewHolder(holder: DependentsViewHolder, position: Int) {
        val dependent = dependents[position]
        holder.bind(dependent,itemClickListener)
    }
}
interface OnItemClickListener{
    fun onItemClicked(id: String?)


}
class DependentsViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    val name: TextView = view.item_usuario_nome
    /*val email: TextView = view.item_usuario_email*/
    val phone: TextView = view.item_usuario_telefone

    fun bind(dependentFromFirebase: DependentFromFirebase,clickListener: OnItemClickListener)
    {
        name.text = dependentFromFirebase.dependent?.name
        /*email.text = dependent.email*/
        phone.text = dependentFromFirebase.dependent?.phone

        view.bt_dashboard_delete.setOnClickListener {
            clickListener.onItemClicked(dependentFromFirebase.key)
        }
    }
//    private var btn_remove: Button = view.remove
//    private var btn_edit: Button = view.edit

}
