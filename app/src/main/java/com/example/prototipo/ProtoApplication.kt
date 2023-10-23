package com.example.prototipo

import android.app.Application
import com.example.prototipo.database.AppDatabase
import com.example.prototipo.database.cliente.ClienteRepository
import com.example.prototipo.database.servico.ServicoRepository

class ProtoApplication : Application() {

    private val database by lazy { AppDatabase.getInstance(this) }
    val repositoryClientes by lazy { ClienteRepository(database.clienteDao()) }
    val repositoryServicos by lazy { ServicoRepository(database.servicoDao()) }
}
