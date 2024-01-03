package com.johulk.tarefaguru.database.servico

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.johulk.tarefaguru.database.ClienteWithServicos
import com.johulk.tarefaguru.database.cliente.Cliente
import kotlinx.coroutines.flow.Flow

@Dao
interface ServicoDao {
    @Insert
    suspend fun insertServico(servico: Servico)

    @Update
    suspend fun updateServico(servico: Servico)

    @Delete
    suspend fun deleteServico(servico: Servico)

    @Transaction
    @Query("SELECT *  FROM clientes where id = :clienteId")
    fun getAllServicosByCliente(clienteId: Int): Flow<List<ClienteWithServicos>>

    @Transaction
    @Query("SELECT * FROM clientes where id = :clienteId")
    fun getClienteFromServico(clienteId: Int): Cliente

    @Query("SELECT * FROM servicos where servcoid = :id")
    fun getServicoFromId(id: Int): Servico

    @Query("SELECT * FROM clientes where id = :id")
    fun getClienteWServicoFromId(id: Int): ClienteWithServicos

    @Query("SELECT * FROM clientes")
    fun getAllClienteWServicos(): Flow<List<ClienteWithServicos>>

    @Transaction
    @Query("SELECT CLIENTES.*, SERVICOS.* FROM CLIENTES JOIN SERVICOS ON CLIENTES.id = SERVICOS.clienteId WHERE CLIENTES.id IN (SELECT DISTINCT clienteId FROM SERVICOS WHERE DateTime BETWEEN :startDate AND :endDate)")
    fun getServicosSemana(startDate: Long, endDate: Long): Flow<List<ClienteWithServicos>>

    @Query("SELECT * FROM servicos ORDER BY servcoid ASC")
    fun allServicos(): Flow<List<Servico>>

    @Query("SELECT COUNT(servcoid) from servicos")
    fun numServicos(): LiveData<Int>
}
