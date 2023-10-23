package com.example.prototipo.database.servico

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

class ServicoRepository(private val servicoDao: ServicoDao) {

    val allServicos: Flow<List<Servico>> = servicoDao.allServicos()

    @WorkerThread
    suspend fun insertServico(servico: Servico) {
        servicoDao.insertServico(servico)
    }

    @WorkerThread
    suspend fun updateServico(servico: Servico) {
        servicoDao.updateServico(servico)
    }
}
