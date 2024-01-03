package com.johulk.tarefaguru.database.cliente

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.johulk.tarefaguru.databinding.ClienteCellBinding

class ClienteViewHolder(
    private var context: Context,
    private var binding: ClienteCellBinding,
) : RecyclerView.ViewHolder(binding.root) {

    fun bindClienteItem(cliente: Cliente) {
        binding.name.text = cliente.nome
        binding.telemovel.text = cliente.telemovel
        binding.morada.text = cliente.morada

        if (!cliente.ativo) {
            binding.singleChip.setChipBackgroundColorResource(android.R.color.holo_red_dark)
        }
    }
}
