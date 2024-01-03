package com.johulk.tarefaguru.database.cliente

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.johulk.tarefaguru.databinding.ClienteCellBinding
import com.johulk.tarefaguru.fragments.ClientesListFragmentDirections

class ClienteAdapter(
    private val clientes: List<Cliente>,
    private val context: Context,
    private val parentFragment: Fragment,
) : RecyclerView.Adapter<ClienteViewHolder>() {

    private lateinit var navController: NavController

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClienteViewHolder {
        val from = LayoutInflater.from(parent.context)
        val binding = ClienteCellBinding.inflate(from, parent, false)
        return ClienteViewHolder(parent.context, binding)
    }

    override fun getItemCount(): Int = clientes.size

    override fun onBindViewHolder(holder: ClienteViewHolder, position: Int) {
        holder.bindClienteItem(clientes[position])

        holder.itemView.setOnClickListener {
            navController = parentFragment.findNavController()
            val direction = ClientesListFragmentDirections.actionClientesListFragmentToLibraryFragment(clientes[position])
            navController.navigate(direction)
        }
    }
}
