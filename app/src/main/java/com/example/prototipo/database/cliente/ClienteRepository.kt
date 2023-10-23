package com.example.prototipo.database.cliente

import androidx.annotation.WorkerThread
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest

class ClienteRepository(private val clienteDao: ClienteDao) {

    val searchQuery = MutableStateFlow("")

    @OptIn(ExperimentalCoroutinesApi::class)
    private val clientesFlow = searchQuery.flatMapLatest {
        clienteDao.allClientes(it)
    }
    val allClientes = clientesFlow.asLiveData()

    @WorkerThread
    suspend fun insertCliente(cliente: Cliente) {
        clienteDao.insertCliente(cliente)
    }

    @WorkerThread
    suspend fun updateCliente(cliente: Cliente) {
        clienteDao.updateCliente(cliente)
    }
}
