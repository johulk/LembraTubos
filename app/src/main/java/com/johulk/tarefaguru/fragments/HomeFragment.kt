package com.johulk.tarefaguru.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.johulk.tarefaguru.ProtoApplication
import com.johulk.tarefaguru.database.servico.Servico
import com.johulk.tarefaguru.database.servico.ServicoModelFactory
import com.johulk.tarefaguru.database.servico.ServicoViewModel
import com.johulk.tarefaguru.databinding.FragmentHomeBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.Instant
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    private val cwsViewModel: ServicoViewModel by viewModels {
        ServicoModelFactory((requireActivity().application as ProtoApplication).repositoryServicos)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        setupUI()
        return binding.root
    }

    private fun setupUI() {
        setMenu()
    }

    private fun setMenu() {
        observeNumServicos()
        observeValorReceber()
        observeLatestCliente()
        updateUIWithLatestService()
    }

    private fun observeNumServicos() {
        // Assuming you have a LiveData<List<Servico>> to get all Servicos
        cwsViewModel.servicos.observe(viewLifecycleOwner) { servicosList ->
            val currentTime = System.currentTimeMillis()
            val filteredServices = servicosList.filter { it.dateTimeEpoch > currentTime }
            binding.numServicos.text = filteredServices.size.toString()
        }
    }

    private fun observeValorReceber(){
        cwsViewModel.servicos.observe(viewLifecycleOwner) { servicosList ->

            // Filter unpaid services
            val unpaidServices = servicosList.filter { !it.pago }

            // Calculate the sum of unpaid service values
            val totalUnpaidAmount = unpaidServices.sumOf { it.valor }

            val texto = totalUnpaidAmount.toString() + "€"
            // Update UI
            binding.valorFalta.text = texto
        }
    }

    private fun observeLatestCliente() {
        cwsViewModel.clienteLiveData.observe(viewLifecycleOwner) { cliente ->
            binding.nomeProxCliente.text = cliente?.nome ?: "N/A"
        }
    }

    private fun updateUIWithLatestService() {
        val dateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        val timeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")

        cwsViewModel.servicos.observe(viewLifecycleOwner) { servicos ->
            lifecycleScope.launch {
                try {
                    // Move database operations to a background thread
                    val latestServico = withContext(Dispatchers.IO) {
                        getLatest(servicos)
                    }

                    val latestCliente = withContext(Dispatchers.IO) {
                        cwsViewModel.getCliente(latestServico)
                    }

                    // Update UI with the latest service information
                    withContext(Dispatchers.Main) {
                        binding.nomeProxCliente.text = latestCliente?.nome ?: "Não tens serviços marcados"
                        if (latestServico.dateTimeEpoch > 0) {
                            val formattedText = "${getDayOfWeekFromDate(latestServico.dateTimeMarcado())} - ${latestServico.dateTimeMarcado().format(dateFormatter)}"

                            binding.proxHoras.text = latestServico.dateTimeMarcado().format(timeFormatter) ?: "Sem Horas"
                            binding.proxHoras.visibility = View.VISIBLE
                            binding.chipDia.text = formattedText
                        } else {
                            binding.proxHoras.visibility = View.INVISIBLE
                            binding.chipDia.visibility = View.INVISIBLE
                        }
                    }
                } catch (e: Exception) {
                    Log.e("DebugIgor", "Error in setMenu", e)
                }
            }
        }
    }

    private fun getLatest(servicos: List<Servico>): Servico {
        val currentEpoch = Instant.now().toEpochMilli()

        var servicoLatest: Servico? = null
        var closestDifference: Long = Long.MAX_VALUE

        servicos.forEach { servico ->
            val servicoEpoch = servico.dateTimeEpoch

            if (servicoEpoch < currentEpoch) {
                return@forEach
            }

            val diff = servicoEpoch - currentEpoch

            if (diff < closestDifference) {
                closestDifference = diff
                servicoLatest = servico
            }
        }

        return servicoLatest ?: getDefaultServico()
    }

    private fun getDefaultServico(): Servico {
        // Provide default information for the Servico
        return Servico(
            clienteId = 0,
            dateTimeEpoch = 0,
            servcoid = 0,
            pago = false,
            valor = 0.0,
        )
    }
    private fun getDayOfWeekFromDate(date: LocalDateTime): String {
        return date.dayOfWeek.getDisplayName(TextStyle.FULL, Locale("pt"))
    }
}
