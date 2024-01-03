package com.johulk.tarefaguru.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.johulk.tarefaguru.ProtoApplication
import com.johulk.tarefaguru.R
import com.johulk.tarefaguru.database.servico.ServicoListaAdapter
import com.johulk.tarefaguru.database.servico.ServicoModelFactory
import com.johulk.tarefaguru.database.servico.ServicoViewModel
import com.johulk.tarefaguru.databinding.FragmentServicosListBinding
import kotlinx.coroutines.launch

class ServicosListFragment : Fragment() {

    private lateinit var viewBinding: FragmentServicosListBinding
    private val servicoViewModel: ServicoViewModel by viewModels {
        ServicoModelFactory((requireActivity().application as ProtoApplication).repositoryServicos)
    }
    private val categories = listOf("Todas", "Esta Semana", "Este Mês")

    private lateinit var servicoAdapter: ServicoListaAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        viewBinding = FragmentServicosListBinding.inflate(inflater, container, false)
        setRecyclerView()
        setupCategorySpinner()
        setupCategoryChip()
        return viewBinding.root
    }

    private fun setupCategorySpinner() {
        viewBinding.materialToolbar.inflateMenu(R.menu.menu_fragment_servicos)
        val categoryItem = viewBinding.materialToolbar.menu.findItem(R.id.menu_category)
        val categorySpinner =
            viewBinding.materialToolbar.menu.findItem(R.id.menu_category)?.actionView?.findViewById<Spinner>(
                R.id.categorySpinner,
            )

        val adapter = ArrayAdapter(requireContext(), R.layout.spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        if (categorySpinner != null) {
            categorySpinner.adapter = adapter
        }

        categorySpinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long,
            ) {
                servicoViewModel.setCategory(categories[position])
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle case where nothing is selected (optional)
            }
        }
    }

    private fun setupCategoryChip() {
        viewBinding.chipFilterPago.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                viewBinding.chipFilterPago.text = "Pago"
            } else {
                viewBinding.chipFilterPago.text = "Todos"
            }
            servicoViewModel.setChipFilterPago(isChecked)
        }
    }

    private fun setRecyclerView() {
        viewLifecycleOwner.lifecycleScope.launch {
            servicoViewModel.cws.collect { cws ->
                Log.d("List", cws.toString())
                viewBinding.todoListRecyclerView.apply {
                    layoutManager = LinearLayoutManager(activity)

                    // Initialize the adapter and assign it to the property
                    servicoAdapter = ServicoListaAdapter(cws, this.context, this@ServicosListFragment)
                    adapter = servicoAdapter
                }
            }
        }
    }
}
