package com.johulk.tarefaguru.database.cliente

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "clientes")
data class Cliente(

    @field:ColumnInfo(name = "Nome")
    var nome: String,

    @field:ColumnInfo(name = "Telemóvel")
    var telemovel: String,

    @field:ColumnInfo(name = "Morada")
    var morada: String,

    @field:ColumnInfo(name = "Contrato")
    var contrato: Boolean,

    @field:ColumnInfo(name = "Ativo")
    var ativo: Boolean,

    @ColumnInfo(name = "Notas")
    var notas: String,

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
) : Serializable
