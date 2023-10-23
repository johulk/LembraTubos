package com.example.prototipo.database.cliente

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.example.prototipo.databinding.ClienteCellBinding
import com.example.prototipo.fragments.DetalhesCliente

class ClienteAdapter(
    private val clientes: List<Cliente>,
    private val context: Context,
) : RecyclerView.Adapter<ClienteViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClienteViewHolder {
        val from = LayoutInflater.from(parent.context)
        val binding = ClienteCellBinding.inflate(from, parent, false)
        return ClienteViewHolder(parent.context, binding)
    }

    override fun getItemCount(): Int = clientes.size

    override fun onBindViewHolder(holder: ClienteViewHolder, position: Int) {
        holder.bindClienteItem(clientes[position])

        holder.itemView.setOnClickListener {
            replaceFragment(DetalhesCliente(clientes[position]), context)
        }
    }

    private fun replaceFragment(fragment: Fragment, context: Context) {
        val transaction: FragmentTransaction = (context as FragmentActivity).supportFragmentManager.beginTransaction()
        transaction.replace(com.example.prototipo.R.id.frame_layout, fragment)
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}
