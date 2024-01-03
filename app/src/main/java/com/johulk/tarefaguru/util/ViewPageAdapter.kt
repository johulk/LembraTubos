package com.johulk.tarefaguru.util

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.johulk.tarefaguru.documentsHandler.DocumentsFragment
import com.johulk.tarefaguru.documentsHandler.NotasFragment
import com.johulk.tarefaguru.fragments.ServicosClienteList

class ViewPageAdapter(
    fm: FragmentManager,
    lifecycle: Lifecycle,
    idcliente: Int,
    notas: String,
) : FragmentStateAdapter(fm, lifecycle) {

    private var client = idcliente
    private var notas = notas
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> return NotasFragment(notas, client)

            1 -> return DocumentsFragment(client.toString())

            2 -> return ServicosClienteList(client)
        }
        return NotasFragment(notas, client)
    }
}
