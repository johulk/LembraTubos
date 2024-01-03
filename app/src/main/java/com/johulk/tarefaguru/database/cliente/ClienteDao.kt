package com.johulk.tarefaguru.database.cliente

import androidx.room.* // ktlint-disable no-wildcard-imports
import kotlinx.coroutines.flow.Flow

@Dao
interface ClienteDao {

    @Query("SELECT * FROM clientes WHERE nome LIKE '%' || :searchQuery || '%' ")
    fun allClientes(searchQuery: String): Flow<List<Cliente>>

    @Query("SELECT * FROM clientes WHERE id = :id")
    fun clienteById(id: Int): Cliente

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCliente(cliente: Cliente)

    @Update
    suspend fun updateCliente(cliente: Cliente)

    @Query("UPDATE clientes SET notas = :observacoes WHERE id = :id")
    suspend fun updateObservations(id: Int, observacoes: String)

    @Delete
    suspend fun deleteCliente(cliente: Cliente)
}
