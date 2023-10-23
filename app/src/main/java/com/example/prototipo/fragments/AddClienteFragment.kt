package com.example.prototipo.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.prototipo.ProtoApplication
import com.example.prototipo.R
import com.example.prototipo.database.cliente.Cliente
import com.example.prototipo.database.cliente.ClienteModelFactory
import com.example.prototipo.database.cliente.ClienteViewModel
import com.example.prototipo.databinding.FragmentAddClienteBinding

class AddClienteFragment() : Fragment() {

    private lateinit var binding: FragmentAddClienteBinding
    private lateinit var navController: NavController

    private val clienteViewModel: ClienteViewModel by viewModels {
        ClienteModelFactory((requireActivity().application as ProtoApplication).repositoryClientes)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.saveButton.setOnClickListener {
            val navHostFragment = activity?.supportFragmentManager?.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
            navController = navHostFragment.navController
            saveAction()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentAddClienteBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    private fun saveAction() {
        val nome = binding.addClienteNome.text.toString()
        val telemovel = binding.addClienteTelemovel.text.toString()
        val morada = binding.addClienteMorada.text.toString()
        val tipo_Casa = true
        val contrato = true
        val novoCliente = Cliente(nome, telemovel, morada, tipo_Casa, contrato)
        clienteViewModel.addCliente(novoCliente)
        navController.navigate(R.id.clientesListFragment)
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = parentFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }
}
