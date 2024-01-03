package com.johulk.tarefaguru.database.cliente

import androidx.annotation.WorkerThread
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

class ClienteRepository(val clienteDao: ClienteDao) {

    val searchQuery = MutableStateFlow("")
    val filterAtivo = MutableStateFlow(false)
    val filterContrato = MutableStateFlow(false)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val clientesFlow = combine(
        clienteDao.allClientes(""),
        searchQuery,
        filterAtivo,
        filterContrato,
    ) { clientes, query, ativo, contrato ->
        // Apply your filtering logic here
        // For simplicity, assuming Cliente has a name property
        clientes.filter { it.nome.contains(query, ignoreCase = true) && it.ativo == ativo && it.contrato == contrato }
    }
        .map { it.toSet().toList() }

    val allClientes = clientesFlow.asLiveData()

    @WorkerThread
    fun clienteById(id: Int): Cliente = clienteDao.clienteById(id)

    @WorkerThread
    suspend fun insertCliente(cliente: Cliente) {
        clienteDao.insertCliente(cliente)
    }

    @WorkerThread
    suspend fun updateCliente(cliente: Cliente) {
        clienteDao.updateCliente(cliente)
    }

    @WorkerThread
    suspend fun updateNotas(cliente: Int, observacoes: String) {
        clienteDao.updateObservations(cliente, observacoes)
    }
}
