package com.johulk.tarefaguru.database

import android.content.Context
import androidx.room.Database
import androidx.room.Embedded
import androidx.room.Relation
import androidx.room.Room
import androidx.room.RoomDatabase
import com.johulk.tarefaguru.database.cliente.Cliente
import com.johulk.tarefaguru.database.cliente.ClienteDao
import com.johulk.tarefaguru.database.servico.Servico
import com.johulk.tarefaguru.database.servico.ServicoDao

@Database(entities = [Cliente::class, Servico::class], version = 10, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun clienteDao(): ClienteDao
    abstract fun servicoDao(): ServicoDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "AppDatabase",
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}

data class ClienteWithServicos(
    @Embedded val cliente: Cliente,
    @Relation(parentColumn = "id", entityColumn = "clienteId")
    val servicos: List<Servico>,
)
