package com.example.prototipo.database.cliente

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "clientes")
class Cliente(

    @field:ColumnInfo(name = "Nome")
    var nome: String,

    @field:ColumnInfo(name = "Telemóvel")
    var telemovel: String,

    @field:ColumnInfo(name = "Morada")
    var morada: String,

    @field:ColumnInfo(name = "Contrato")
    var contrato: Boolean,

    @field:ColumnInfo(name = "Tipo de Casa") // True = Moradia , False = Prédio / Andar
    var tipo_Casa: Boolean,

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
)
