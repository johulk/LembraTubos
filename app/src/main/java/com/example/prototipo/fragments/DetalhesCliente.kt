package com.example.prototipo.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.prototipo.R
import com.example.prototipo.database.cliente.Cliente
import com.example.prototipo.databinding.FragmentDetalhesClienteBinding

class DetalhesCliente(private val cliente: Cliente) : Fragment() {

    private lateinit var binding: FragmentDetalhesClienteBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDetalhesClienteBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        binding.textNome.text = cliente.nome
        binding.textMorada.text = cliente.morada
        binding.textTelemovel.text = cliente.telemovel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonLigar.setOnClickListener {
// else block means user has already accepted.And make your phone call here.
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + cliente.telemovel))
            startActivity(intent)
        }

        binding.buttonMapa.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("google.navigation:q=" + cliente.morada))
            startActivity(intent)
        }

        binding.buttonAdicionarServico.setOnClickListener {
            replaceFragment(AddServicoFragment(cliente.id))
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = parentFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }
}
