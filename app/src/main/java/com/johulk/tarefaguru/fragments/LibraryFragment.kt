package com.johulk.tarefaguru.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.johulk.tarefaguru.database.cliente.Cliente
import com.johulk.tarefaguru.databinding.FragmentLibraryBinding
import com.johulk.tarefaguru.util.ViewPageAdapter
import com.google.android.material.tabs.TabLayout

class LibraryFragment : Fragment() {
    // TODO

    private val args: LibraryFragmentArgs by navArgs()
    private lateinit var cliente: Cliente
    private lateinit var tabLayout: TabLayout
    private lateinit var binding: FragmentLibraryBinding
    private lateinit var viewPager2: ViewPager2
    private lateinit var adapter: ViewPageAdapter
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Initialize the binding object
        binding = FragmentLibraryBinding.inflate(inflater, container, false)
        navController = requireParentFragment().findNavController()

        cliente = args.Cliente

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Assign references from the binding object
        tabLayout = binding.tabLayout
        viewPager2 = binding.viewPager
        adapter = ViewPageAdapter(activity?.supportFragmentManager!!, lifecycle, cliente.id, cliente.notas)

        tabLayout.addTab(tabLayout.newTab().setText("Notas"))
        tabLayout.addTab(tabLayout.newTab().setText("Documentos e Fotos"))
        tabLayout.addTab(tabLayout.newTab().setText("Histórico"))

        viewPager2.adapter = adapter
        viewPager2.isUserInputEnabled = false

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab != null) {
                    viewPager2.currentItem = tab.position
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })

        viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                tabLayout.selectTab(tabLayout.getTabAt(position))
            }
        })

        // Retrieve cliente from args and populate data
        populateData(cliente)
    }

    private fun populateData(cliente: Cliente) {
        binding.textNome.text = cliente.nome
        binding.textMorada.text = cliente.morada
        binding.textTelemovel.text = cliente.telemovel

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
            val direction = LibraryFragmentDirections.actionLibraryFragmentToAddServicoFragment(cliente,null)
            navController.navigate(direction)
        }

        binding.buttonAtualizarCliente.setOnClickListener {
            val direction = LibraryFragmentDirections.actionLibraryFragmentToAddClienteFragment2(cliente)
            navController.navigate(direction)
        }
    }
}
