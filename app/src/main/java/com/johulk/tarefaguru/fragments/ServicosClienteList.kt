package com.johulk.tarefaguru.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.johulk.tarefaguru.ProtoApplication
import com.johulk.tarefaguru.database.servico.ServicoAdapter
import com.johulk.tarefaguru.database.servico.ServicoModelFactory
import com.johulk.tarefaguru.database.servico.ServicoViewModel
import com.johulk.tarefaguru.databinding.FragmentServicosClienteListBinding
import kotlinx.coroutines.launch

class ServicosClienteList(val idcliente: Int) : Fragment() {

    private lateinit var viewBinding: FragmentServicosClienteListBinding
    private val servicoViewModel: ServicoViewModel by viewModels {
        ServicoModelFactory((requireActivity().application as ProtoApplication).repositoryServicos)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        viewBinding = FragmentServicosClienteListBinding.inflate(inflater, container, false)
        servicoViewModel.setClientId(idcliente)
        setRecyclerView()
        return viewBinding.root
    }

    private fun setRecyclerView() {
        viewLifecycleOwner.lifecycleScope.launch {
            servicoViewModel.servicosByCliente.collect { servicosByCliente ->

                viewBinding.servicosclienteRecyclerView.apply {
                    layoutManager = LinearLayoutManager(activity)
                    adapter = ServicoAdapter(servicosByCliente, this.context, this@ServicosClienteList)
                }
            }
        }
    }
}
