package com.johulk.tarefaguru.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.johulk.tarefaguru.ProtoApplication
import com.johulk.tarefaguru.R
import com.johulk.tarefaguru.database.cliente.ClienteAdapter
import com.johulk.tarefaguru.database.cliente.ClienteModelFactory
import com.johulk.tarefaguru.database.cliente.ClienteViewModel
import com.johulk.tarefaguru.databinding.FragmentClientesListBinding
import com.johulk.tarefaguru.util.onQueryTextChanged

class ClientesListFragment : Fragment() {

    private lateinit var binding: FragmentClientesListBinding
    private val clienteViewModel: ClienteViewModel by viewModels {
        ClienteModelFactory((requireActivity().application as ProtoApplication).repositoryClientes)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentClientesListBinding.inflate(inflater, container, false)
        setRecyclerView()
        setHasOptionsMenu(true)
        setupMenu()
        return binding.root
    }
    private fun setupMenu() {
        with(binding.materialToolbar) {
            inflateMenu(R.menu.menu_fragment_clientes)
            setTitle("Procurar")
            elevation = 10f

            val searchItem = menu.findItem(R.id.search_bar_clientes)
            val searchView = searchItem.actionView as? SearchView

            val contextFromToolbar = binding.materialToolbar.context

            searchView?.findViewById<EditText>(androidx.appcompat.R.id.search_src_text)?.setTextColor(ContextCompat.getColor(contextFromToolbar, R.color.black))
            searchView?.findViewById<EditText>(androidx.appcompat.R.id.search_src_text)?.setHintTextColor(ContextCompat.getColor(contextFromToolbar, com.google.android.material.R.color.m3_ref_palette_black))

            searchView!!.onQueryTextChanged { query: String ->
                clienteViewModel.updateSearchQuery(query)
            }

            val chipFilterAtivo = binding.chipFilterAtivo
            val chipFilterContrato = binding.chipFilterContrato

            clienteViewModel.updateFilterAtivo(chipFilterAtivo.isChecked)
            clienteViewModel.updateFilterContrato(chipFilterContrato.isChecked)

            chipFilterAtivo.setOnCheckedChangeListener { _, isChecked ->
                clienteViewModel.updateFilterAtivo(isChecked)
            }

            chipFilterContrato.setOnCheckedChangeListener { _, isChecked ->
                clienteViewModel.updateFilterContrato(isChecked)
            }
        }
    }

    private fun setRecyclerView() {
        clienteViewModel.clientes.observe(viewLifecycleOwner) {
            binding.todoListRecyclerView.apply {
                layoutManager = LinearLayoutManager(activity)
                adapter = ClienteAdapter(it, this.context, this@ClientesListFragment)
            }
        }
    }
}
