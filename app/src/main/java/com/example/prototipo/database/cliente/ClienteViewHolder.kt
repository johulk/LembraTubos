package com.example.prototipo.database.cliente

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.example.prototipo.R
import com.example.prototipo.databinding.ClienteCellBinding

class ClienteViewHolder(
    private var context: Context,
    private var binding: ClienteCellBinding,
) : RecyclerView.ViewHolder(binding.root) {

    fun bindClienteItem(cliente: Cliente) {
        binding.name.text = cliente.nome
        binding.telemovel.text = cliente.telemovel
        binding.morada.text = cliente.morada

        if (cliente.tipo_Casa) {
            binding.imageCliente.setImageResource(R.drawable.baseline_home_24)
        } else {
            binding.imageCliente.setImageResource(R.drawable.baseline_apartment_24)
        }
    }
}
