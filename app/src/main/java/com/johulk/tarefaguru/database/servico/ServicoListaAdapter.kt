package com.johulk.tarefaguru.database.servico

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.johulk.tarefaguru.database.ClienteWithServicos
import com.johulk.tarefaguru.databinding.ServicoCellBinding

class ServicoListaAdapter(
    private var servico: List<ClienteWithServicos>,
    private val context: Context,
    private val parentFragment: Fragment,
) : RecyclerView.Adapter<ServicoViewHolder>() {

    private lateinit var navController: NavController

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServicoViewHolder {
        val from = LayoutInflater.from(parent.context)
        val binding = ServicoCellBinding.inflate(from, parent, false)
        return ServicoViewHolder(parent.context, binding)
    }

    override fun getItemCount(): Int = servico.sumOf { it.servicos.size }

    fun updateData(newData: List<ClienteWithServicos>) {
        servico = newData
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ServicoViewHolder, position: Int) {
        val (clienteWithServicos, servicoIndex) = getClienteWithServicosAndIndex(position)
        holder.bindServicoItem(
            clienteWithServicos.servicos[servicoIndex],
            clienteWithServicos.cliente,
        )

        holder.itemView.setOnClickListener {
            navController = parentFragment.findNavController()
        }
    }

    private fun getClienteWithServicosAndIndex(position: Int): Pair<ClienteWithServicos, Int> {
        var count = 0
        for (clienteWithServicos in servico) {
            if (position < count + clienteWithServicos.servicos.size) {
                val servicoIndex = position - count
                return Pair(clienteWithServicos, servicoIndex)
            }
            count += clienteWithServicos.servicos.size
        }
        // This should not happen if the position is within the valid range
        throw IllegalArgumentException("Invalid position: $position")
    }
}
