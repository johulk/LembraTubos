package com.johulk.tarefaguru.database.cliente
// <>
import androidx.lifecycle.* // ktlint-disable no-wildcard-imports
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class ClienteViewModel(private val repository: ClienteRepository) : ViewModel() {

    private val _clientes: MutableStateFlow<List<Cliente>> = MutableStateFlow(emptyList())

    val clientes = _clientes.asLiveData()

    init {
        combine(
            repository.clienteDao.allClientes(""),
            repository.searchQuery,
            repository.filterAtivo,
            repository.filterContrato,
        ) { clientes, query, ativo, contrato ->
            clientes.filter { it.nome.contains(query, ignoreCase = true) && it.ativo == ativo && it.contrato == contrato }
        }
            .distinctUntilChanged() // Apply distinctUntilChanged to the entire flow
            .onEach { _clientes.value = it }
            .launchIn(viewModelScope)
    }

    fun updateSearchQuery(query: String) {
        repository.searchQuery.value = query
    }

    fun updateFilterAtivo(ativo: Boolean) {
        repository.filterAtivo.value = ativo
    }

    fun updateFilterContrato(contrato: Boolean) {
        repository.filterContrato.value = contrato
    }

    fun addCliente(novoCliente: Cliente) = viewModelScope.launch {
        repository.insertCliente(novoCliente)
    }

    fun clienteById(id: Int) = repository.clienteById(id)

    fun searchClientes(query: String) {
        repository.searchQuery.value = query
    }

    fun updateCliente(novoCliente: Cliente) = viewModelScope.launch {
        repository.updateCliente(novoCliente)
    }

    suspend fun updateNotas(cliente: Int, observacoes: String) {
        repository.updateNotas(cliente, observacoes)
    }
}

class ClienteModelFactory(private val repository: ClienteRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ClienteViewModel::class.java)) {
            return ClienteViewModel(repository) as T
        }

        throw IllegalArgumentException("Unknown Class for View Model")
    }
}
