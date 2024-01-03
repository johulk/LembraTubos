package com.johulk.tarefaguru.documentsHandler

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.johulk.tarefaguru.ProtoApplication
import com.johulk.tarefaguru.database.cliente.ClienteModelFactory
import com.johulk.tarefaguru.database.cliente.ClienteViewModel
import com.johulk.tarefaguru.databinding.FragmentNotasBinding
import kotlinx.coroutines.launch

class NotasFragment(private val notas: String, private val id: Int) : Fragment() {

    private lateinit var binding: FragmentNotasBinding

    private val clienteViewModel: ClienteViewModel by viewModels {
        ClienteModelFactory((requireActivity().application as ProtoApplication).repositoryClientes)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentNotasBinding.inflate(inflater, container, false)
        setText()

        return binding.root
    }

    private fun setText() {
        binding.editTextObservations.setText(notas)

        binding.buttonSalvarNotas.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                val observations = binding.editTextObservations.text.toString()

                try {
                    clienteViewModel.updateNotas(id, observations)
                } catch (e: Exception) {
                    // Log or handle the exception
                    Log.e("TAG", "Error updating notas", e)
                }
            }
        }
    }
}
