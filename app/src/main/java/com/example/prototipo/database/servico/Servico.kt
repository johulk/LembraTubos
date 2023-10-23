package com.example.prototipo.database.servico

import androidx.room.*
import androidx.room.ForeignKey.Companion.CASCADE
import com.example.prototipo.database.cliente.Cliente
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Entity(
    tableName = "servicos",
    foreignKeys = [
        ForeignKey(
            entity = Cliente::class,
            parentColumns = ["id"],
            childColumns = ["clienteId"],
            onDelete = CASCADE,
        ),
    ],
)
class Servico(

    @field:ColumnInfo(name = "clienteId") var clienteId: Int,

    @field:ColumnInfo(name = "Dia") var diaString: String?,

    @field:ColumnInfo(name = "Hora") var horaString: String?,

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
) {
    fun horaMarcada(): LocalTime? = if (horaString == null) {
        null
    } else {
        (LocalTime.parse(horaString, horaFormatter))
    }

    fun diaMarcado(): LocalDate? = if (diaString == null) {
        null
    } else {
        (LocalDate.parse(diaString, dataFormatter))
    }

    companion object {
        val horaFormatter: DateTimeFormatter = DateTimeFormatter.ISO_TIME
        val dataFormatter: DateTimeFormatter = DateTimeFormatter.ISO_DATE
    }
}
