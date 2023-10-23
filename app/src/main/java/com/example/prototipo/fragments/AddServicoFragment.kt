package com.example.prototipo.fragments

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.prototipo.ProtoApplication
import com.example.prototipo.R
import com.example.prototipo.database.servico.Servico
import com.example.prototipo.database.servico.ServicoModelFactory
import com.example.prototipo.database.servico.ServicoViewModel
import com.example.prototipo.databinding.FragmentAddServicoBinding
import java.time.LocalDate
import java.time.LocalTime

class AddServicoFragment(private var clienteId: Int) : Fragment() {

    private lateinit var binding: FragmentAddServicoBinding

    private val servicoViewModel: ServicoViewModel by viewModels {
        ServicoModelFactory((requireActivity().application as ProtoApplication).repositoryServicos)
    }

    private var horaMarcada: LocalTime? = null
    private var diaMarcado: LocalDate? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.saveButton.setOnClickListener {
            saveAction()
        }

        binding.timePickerButton.setOnClickListener {
            openTimePicker()
        }

        binding.dayPickerButton.setOnClickListener {
            openDayPicker()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentAddServicoBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    private fun saveAction() {
        val horaMarcadaString = if (horaMarcada == null) null else Servico.horaFormatter.format(horaMarcada)
        val diaMarcadoString = if (diaMarcado == null) null else Servico.dataFormatter.format(diaMarcado)

        val novoServico = Servico(clienteId, diaMarcadoString, horaMarcadaString)

        servicoViewModel.addServico(novoServico)
        replaceFragment(ClientesListFragment())
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
        binding.dayPickerButton.text = String.format("%02d/%02d/%02d", diaMarcado!!.dayOfMonth, diaMarcado!!.monthValue, diaMarcado!!.year)
    }

    private fun updateTimeButtonText() {
        binding.timePickerButton.text = String.format("%02d:%02d", horaMarcada!!.hour, horaMarcada!!.minute)
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = parentFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }
}
