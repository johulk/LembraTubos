package com.example.prototipo.database.servico

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ServicoDao {
    @Insert
    suspend fun insertServico(servico: Servico?)

    @Update
    suspend fun updateServico(servico: Servico?)

    @Delete
    suspend fun deleteServico(servico: Servico?)

    @Query("SELECT * FROM servicos WHERE clienteId = :clienteId")
    fun getAllServicosByCliente(clienteId: Int): Flow<List<Servico>>

    @Query("SELECT * FROM servicos ORDER BY id ASC")
    fun allServicos(): Flow<List<Servico>>
}
