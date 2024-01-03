package com.johulk.tarefaguru.database.servico

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.johulk.tarefaguru.database.ClienteWithServicos
import com.johulk.tarefaguru.database.cliente.Cliente
import kotlinx.coroutines.flow.Flow

class ServicoRepository(private val servicoDao: ServicoDao) {

    val allServicos: Flow<List<Servico>> = servicoDao.allServicos()
    var allCWS: Flow<List<ClienteWithServicos>> = servicoDao.getAllClienteWServicos()
    val numServicos: LiveData<Int> = servicoDao.numServicos()

    @WorkerThread
    suspend fun insertServico(servico: Servico) {
        servicoDao.insertServico(servico)
    }

    @WorkerThread
    suspend fun updateServico(servico: Servico) {
        servicoDao.updateServico(servico)
    }

    @WorkerThread
    fun getCliente(servico: Servico): Cliente {
        return servicoDao.getClienteFromServico(servico.clienteId)
    }

    @WorkerThread
    fun getAllServicosByCliente(cliente: Int): Flow<List<ClienteWithServicos>> {
        return servicoDao.getAllServicosByCliente(cliente)
    }

    @WorkerThread
    fun getServicosSemana(startDate: Long, endDate: Long): Flow<List<ClienteWithServicos>> {
        return servicoDao.getServicosSemana(startDate, endDate)
    }

    @WorkerThread
    fun getServicoId(id: Int) {
        servicoDao.getServicoFromId(id)
    }

    @WorkerThread
    fun getCWSId(id: Int) {
        servicoDao.getClienteWServicoFromId(id)
    }
}
