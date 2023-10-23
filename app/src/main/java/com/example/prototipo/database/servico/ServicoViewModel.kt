package com.example.prototipo.database.servico

import androidx.lifecycle.*
import kotlinx.coroutines.launch

class ServicoViewModel(private val repository: ServicoRepository) : ViewModel() {
    var servicos: LiveData<List<Servico>> = repository.allServicos.asLiveData()

    fun addServico(newServico: Servico) = viewModelScope.launch {
        repository.insertServico(newServico)
    }

    fun updateServico(newServico: Servico) = viewModelScope.launch {
        repository.updateServico(newServico)
    }
}

class ServicoModelFactory(private val repository: ServicoRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ServicoViewModel::class.java)) {
            return ServicoViewModel(repository) as T
        }

        throw java.lang.IllegalArgumentException("Unknown Class for View Model")
    }
}
