package com.example.prototipo.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.prototipo.R
import com.example.prototipo.database.servico.Servico
import com.example.prototipo.databinding.FragmentDetalhesClienteBinding
import com.example.prototipo.databinding.FragmentDetalhesServicoBinding

class DetalhesServico(private val servico: Servico) : Fragment() {

    private lateinit var binding: FragmentDetalhesServicoBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDetalhesServicoBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
       // binding.textNome.text = servico.clienteId
       // binding.textMorada.text = servico.morada
       // binding.textTelemovel.text = servico.telemovel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



      //  binding.buttonAdicionarServico.setOnClickListener {
      //      replaceFragment(AddServicoFragment(cliente.id))
       // }
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = parentFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

}
