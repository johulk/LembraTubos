package com.example.prototipo.database.cliente
// <>
import androidx.lifecycle.*
import kotlinx.coroutines.launch

class ClienteViewModel(private val repository: ClienteRepository) : ViewModel() {

    var clientes: LiveData<List<Cliente>> = repository.allClientes

    fun addCliente(novoCliente: Cliente) = viewModelScope.launch {
        repository.insertCliente(novoCliente)
    }

    fun searchClientes(query: String) {
        repository.searchQuery.value = query
    }

    fun updateCliente(novoCliente: Cliente) = viewModelScope.launch {
        repository.updateCliente(novoCliente)
    }
}

class ClienteModelFactory(private val repository: ClienteRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ClienteViewModel::class.java)) {
            return ClienteViewModel(repository) as T
        }

        throw java.lang.IllegalArgumentException("Unknown Class for View Model")
    }
}
