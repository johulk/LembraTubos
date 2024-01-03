package com.johulk.tarefaguru.database.servico

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.johulk.tarefaguru.database.ClienteWithServicos
import com.johulk.tarefaguru.database.cliente.Cliente
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class ServicoViewModel(private val repository: ServicoRepository) : ViewModel() {

    private val categoryFlow = MutableStateFlow("Todas")
    private val _idVal = MutableStateFlow(0)
    private val chipFilterFlow = MutableStateFlow(false)

    val idVal: StateFlow<Int> get() = _idVal

    @OptIn(ExperimentalCoroutinesApi::class)
    val cws: Flow<List<ClienteWithServicos>> = combine(
        categoryFlow,
        chipFilterFlow,
    ) { category, isChipChecked ->
        Pair(category, isChipChecked)
    }.flatMapLatest { (category, isChipChecked) ->
        when (category) {
            "Todas" -> repository.allCWS
            "Esta Semana" -> getServicosSemana()
            else -> repository.allCWS
        }.map { clienteWithServicosList ->
            clienteWithServicosList.onEach {
                Log.d("BeforeFilter", it.toString())
            }.let { list ->
                if (isChipChecked) {
                    list.map { clienteWithServicos ->
                        // Remove servicos with pago=false
                        val filteredServicos = clienteWithServicos.servicos.filter { it.pago }
                        // Create a new ClienteWithServicos with the filtered servicos list
                        clienteWithServicos.copy(servicos = filteredServicos)
                    }.distinctBy { it.cliente.id }
                } else {
                    list
                }
            }
        }
    }.distinctUntilChanged()
    val chipFilterPago: StateFlow<Boolean> = chipFilterFlow.asStateFlow()

    // Function to update the chip state
    fun setChipFilterPago(isChecked: Boolean) {
        chipFilterFlow.value = isChecked
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val servicosByCliente: Flow<List<ClienteWithServicos>> = idVal
        .flatMapLatest { idValue ->
            repository.getAllServicosByCliente(idValue)
        }

    fun setCategory(category: String) {
        categoryFlow.value = category
    }
    private fun getServicosSemana(): Flow<List<ClienteWithServicos>> {
        val (startDate, endDate) = getWeekDateRangeEpochs()

        return repository.getServicosSemana(startDate, endDate)
            .map { clienteWithServicosList ->
                clienteWithServicosList.distinctBy { it.cliente.id }
            }
            .distinctUntilChanged()
    }

    var servicos: LiveData<List<Servico>> = repository.allServicos.asLiveData()

    var numServicos: LiveData<Int> = repository.numServicos

    private val _clienteLiveData = MutableLiveData<Cliente?>()
    val clienteLiveData: LiveData<Cliente?> get() = _clienteLiveData

    fun addServico(newServico: Servico) = viewModelScope.launch {
        repository.insertServico(newServico)
    }

    fun updateServico(newServico: Servico) = viewModelScope.launch {
        repository.updateServico(newServico)
    }
    fun setClientId(newClientId: Int) {
        _idVal.value = newClientId
    }
    fun observeAllCWS() {
        // Already observing all CWS, nothing to change
    }

    fun getCliente(servico: Servico): Cliente? {
        return runBlocking {
            try {
                repository.getCliente(servico)
            } catch (e: Exception) {
                Log.e("ServicoViewModel", "Error fetching Cliente", e)
                null
            }
        }
    }

    fun getServicoFromId(id: Int) = viewModelScope.launch {
        repository.getServicoId(id)
    }
    fun getCWSFromId(id: Int) = viewModelScope.launch {
        repository.getCWSId(id)
    }

    private fun getMonthDateRangeStrings(): Pair<String, String> {
        val dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd")

        val today = LocalDate.now()
        val firstDayOfMonth = today.withDayOfMonth(1)
        val lastDayOfMonth = today.withDayOfMonth(today.lengthOfMonth())

        val startDateString = firstDayOfMonth.format(dateFormat)
        val endDateString = lastDayOfMonth.format(dateFormat)

        return Pair(startDateString, endDateString)
    }
    private fun getWeekDateRangeEpochs(): Pair<Long, Long> {
        val today = LocalDate.now()
        val endOfWeek = today.plusDays(6 - today.dayOfWeek.value.toLong()) // Assuming Sunday is the end of the week

        val startEpoch = today.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
        val endEpoch = endOfWeek.atStartOfDay(ZoneId.systemDefault()).plusDays(1).toInstant().toEpochMilli() - 1

        return Pair(startEpoch, endEpoch)
    }
}

class ServicoModelFactory(private val repository: ServicoRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ServicoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ServicoViewModel(repository) as T
        }

        throw java.lang.IllegalArgumentException("Unknown Class for View Model")
    }
}
