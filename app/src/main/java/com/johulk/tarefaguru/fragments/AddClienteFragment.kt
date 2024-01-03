package com.johulk.tarefaguru.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.johulk.tarefaguru.ProtoApplication
import com.johulk.tarefaguru.R
import com.johulk.tarefaguru.database.cliente.Cliente
import com.johulk.tarefaguru.database.cliente.ClienteModelFactory
import com.johulk.tarefaguru.database.cliente.ClienteViewModel
import com.johulk.tarefaguru.databinding.FragmentAddClienteBinding

class AddClienteFragment : Fragment() {

    private val args: AddClienteFragmentArgs by this.navArgs()
    private lateinit var cliente: Cliente
    private lateinit var binding: FragmentAddClienteBinding
    private lateinit var navController: NavController

    private val clienteViewModel: ClienteViewModel by viewModels {
        ClienteModelFactory((requireActivity().application as ProtoApplication).repositoryClientes)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Initialize the binding object
        binding = FragmentAddClienteBinding.inflate(inflater, container, false)
        navController = requireParentFragment().findNavController()

        if (args.cliente != null) {
            cliente = args.cliente!!
            populateData(cliente)

            binding.buttonAtualizarCliente.setOnClickListener {
                updateAction()
            }
        } else {
            binding.buttonAtualizarCliente.setOnClickListener {
                saveAction()
            }
        }

        return binding.root
    }

    private fun populateData(cliente: Cliente) {
        binding.textNome.setText(cliente.nome)
        binding.textMorada.setText(cliente.morada)
        binding.textTelemovel.setText(cliente.telemovel)
        binding.checkBoxAtivo.isChecked = cliente.ativo
        binding.checkBoxContrato.isChecked = cliente.contrato
    }

    private fun updateAction() {
        cliente.nome = binding.textNome.text.toString()
        cliente.telemovel = binding.textTelemovel.text.toString()
        cliente.morada = binding.textMorada.text.toString()
        cliente.ativo = binding.checkBoxAtivo.isChecked
        cliente.contrato = binding.checkBoxContrato.isChecked

        clienteViewModel.updateCliente(cliente)
        val direction = AddClienteFragmentDirections.actionAddClienteFragment2ToLibraryFragment(cliente)
        navController.navigate(direction)
    }

    private fun saveAction() {
        val nome = binding.textNome.text.toString()
        val telemovel = binding.textTelemovel.text.toString()
        val morada = binding.textMorada.text.toString()
        val activo = binding.checkBoxAtivo.isChecked
        val contrato = binding.checkBoxContrato.isChecked
        val novoCliente = Cliente(nome, telemovel, morada, contrato, activo, "")
        clienteViewModel.addCliente(novoCliente)
        navController.navigate(R.id.clientesListFragment)
    }
}
