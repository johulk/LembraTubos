package com.example.prototipo.fragments

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.prototipo.ProtoApplication
import com.example.prototipo.R
import com.example.prototipo.database.cliente.ClienteAdapter
import com.example.prototipo.database.cliente.ClienteModelFactory
import com.example.prototipo.database.cliente.ClienteViewModel
import com.example.prototipo.databinding.FragmentClientesListBinding
import com.example.prototipo.util.onQueryTextChanged

class ClientesListFragment : Fragment() {

    private lateinit var binding: FragmentClientesListBinding
    private val clienteViewModel: ClienteViewModel by viewModels {
        ClienteModelFactory((requireActivity().application as ProtoApplication).repositoryClientes)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentClientesListBinding.inflate(inflater, container, false)
        setRecyclerView()
        setHasOptionsMenu(true)
        setupMenu()
        return binding.root
    }

    private fun setupMenu() {
        (requireActivity() as MenuHost).addMenuProvider(
            object : MenuProvider {
                override fun onPrepareMenu(menu: Menu) {
                    // Handle for example visibility of menu items
                }

                override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                    menuInflater.inflate(R.menu.menu_fragment_clientes, menu)
                    val searchItem = menu.findItem(R.id.search_bar_clientes)
                    val searchView = searchItem.actionView as SearchView

                    searchView.onQueryTextChanged {
                        clienteViewModel.searchClientes(it)
                    }
                }

                override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                    // Validate and handle the selected menu item
                    return true
                }
            },
            viewLifecycleOwner,
            Lifecycle.State.RESUMED,
        )
    }

    private fun setRecyclerView() {
        clienteViewModel.clientes.observe(viewLifecycleOwner) {
            binding.todoListRecyclerView.apply {
                layoutManager = LinearLayoutManager(activity)
                adapter = ClienteAdapter(it, this.context)
            }
        }
    }
}
