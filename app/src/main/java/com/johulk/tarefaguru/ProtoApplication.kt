package com.johulk.tarefaguru

import android.app.Application
import com.johulk.tarefaguru.database.AppDatabase
import com.johulk.tarefaguru.database.cliente.ClienteRepository
import com.johulk.tarefaguru.database.servico.ServicoRepository

class ProtoApplication : Application() {

    private val database by lazy { AppDatabase.getInstance(this) }
    val repositoryClientes by lazy { ClienteRepository(database.clienteDao()) }
    val repositoryServicos by lazy { ServicoRepository(database.servicoDao()) }
}
