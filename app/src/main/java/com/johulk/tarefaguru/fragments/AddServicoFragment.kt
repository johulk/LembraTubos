package com.johulk.tarefaguru.fragments

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.navArgs
import com.johulk.tarefaguru.ProtoApplication
import com.johulk.tarefaguru.R
import com.johulk.tarefaguru.database.cliente.Cliente
import com.johulk.tarefaguru.database.servico.Servico
import com.johulk.tarefaguru.database.servico.ServicoModelFactory
import com.johulk.tarefaguru.database.servico.ServicoViewModel
import com.johulk.tarefaguru.databinding.FragmentDetalhesServicoBinding
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZoneOffset

class AddServicoFragment() : Fragment() {

    private lateinit var binding: FragmentDetalhesServicoBinding
    private val args: AddServicoFragmentArgs by navArgs()

    private val servicoViewModel: ServicoViewModel by viewModels {
        ServicoModelFactory((requireActivity().application as ProtoApplication).repositoryServicos)
    }
    private var horaMarcada: LocalTime? = null
    private var diaMarcado: LocalDate? = null
    private lateinit var navController: NavController
    private lateinit var cliente: Cliente
    private var servico: Servico? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentDetalhesServicoBinding.inflate(inflater, container, false)
        cliente = args.cliente!!
        val navHostFragment = requireActivity().supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        binding.textNome.text = cliente.nome

        if (args.servico != null) {
            servico = args.servico!!

            populateData()

            binding.buttonAtualizarCliente.setOnClickListener {
                updateAction()
            }
        } else {
            binding.buttonAtualizarCliente.text = "Criar Serviço"
            binding.buttonAtualizarCliente.setOnClickListener {
                saveAction()
            }
        }

        // Inflate the layout for this fragment

        binding.timePickerButton.setOnClickListener {
            openTimePicker()
        }

        binding.dayPickerButton.setOnClickListener {
            openDayPicker()
        }

        return binding.root
    }

    private fun populateData() {
        binding.valorTexto.setText(servico!!.valor.toString())
        binding.buttonAtualizarCliente.text = "Atualizar Serviço"
        binding.switchPago.isChecked = servico!!.pago

        val instant = Instant.ofEpochMilli(servico!!.dateTimeEpoch)
        val zonedDateTime = instant.atZone(ZoneId.systemDefault())
        horaMarcada = zonedDateTime.toLocalTime()
        diaMarcado = zonedDateTime.toLocalDate()
        updateDayButtonText()
        updateTimeButtonText()

        binding.buttonAtualizarCliente.setOnClickListener { updateAction() }
    }
    private fun updateAction() {
        servico!!.valor = binding.valorTexto.text.toString().toDouble()
        servico!!.pago = binding.switchPago.isChecked
        val dateTime = LocalDateTime.of(diaMarcado, horaMarcada)

        servico!!.dateTimeEpoch = dateTime.toEpochSecond(ZoneOffset.UTC) * 1000L

        servicoViewModel.updateServico(servico!!)
        val direction = ClientesListFragmentDirections.actionClientesListFragmentToLibraryFragment(cliente)
        navController.navigate(direction)
        // navController.navigate(R.id.servicosListFragment)
    }

    private fun saveAction() {
        if (diaMarcado != null && horaMarcada != null) {
            val dateTime = LocalDateTime.of(diaMarcado, horaMarcada)

            // Convert LocalDateTime to Servico using your existing function
            val novoServico = Servico.fromDateTime(cliente.id, dateTime, binding.switchPago.isChecked, binding.valorTexto.text.toString().toDouble())

            // Save the new Servico
            servicoViewModel.addServico(novoServico)
            val direction = ClientesListFragmentDirections.actionClientesListFragmentToLibraryFragment(cliente)
            navController.navigate(direction)
            // navController.navigate(R.id.servicosListFragment)
        }
    }

    private fun openDayPicker() {
        if (diaMarcado == null) {
            diaMarcado = LocalDate.now()
        }
        val listener = DatePickerDialog.OnDateSetListener { _, selectedYear, selectedMonth, selectedDay ->

            diaMarcado = LocalDate.of(selectedYear, selectedMonth + 1, selectedDay)
            updateDayButtonText()
        }
        val dialog = DatePickerDialog(requireContext(), listener, diaMarcado!!.year, diaMarcado!!.monthValue - 1, diaMarcado!!.dayOfMonth)
        dialog.setTitle("Dia para marcar")
        dialog.show()
    }

    private fun openTimePicker() {
        if (horaMarcada == null) {
            horaMarcada = LocalTime.now()
        }
        val listener = TimePickerDialog.OnTimeSetListener { _, selectedHour, selectedMinute ->
            horaMarcada = LocalTime.of(selectedHour, selectedMinute)
            updateTimeButtonText()
        }
        val dialog = TimePickerDialog(activity, listener, horaMarcada!!.hour, horaMarcada!!.minute, true)
        dialog.setTitle("Hora para marcar")
        dialog.show()
    }

    private fun updateDayButtonText() {
        binding.dayPickerButton.text = String.format("%02d/%02d/%02d", diaMarcado!!.dayOfMonth, diaMarcado!!.monthValue, diaMarcado!!.year) }

    private fun updateTimeButtonText() {
        binding.timePickerButton.text = String.format("%02d:%02d", horaMarcada!!.hour, horaMarcada!!.minute)
    }
}
