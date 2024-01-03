package com.johulk.tarefaguru.database.servico

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.johulk.tarefaguru.database.cliente.Cliente
import com.johulk.tarefaguru.databinding.ServicoCellBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

class ServicoViewHolder(
    private var context: Context,
    private var binding: ServicoCellBinding,
) : RecyclerView.ViewHolder(binding.root) {

    fun bindServicoItem(servico: Servico, cliente: Cliente) {
        val dateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        val timeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")

        val formattedText = "${getDayOfWeekFromDate(servico.dateTimeMarcado())} - ${servico.dateTimeMarcado().format(dateFormatter)}"

        binding.name.text = cliente.nome
        binding.chipHoras.text = servico.dateTimeMarcado().format(timeFormatter)
        binding.chipDia.text = formattedText
    }

    private fun getDayOfWeekFromDate(date: LocalDateTime): String {
        return date.dayOfWeek.getDisplayName(TextStyle.FULL, Locale("pt"))
    }
}
