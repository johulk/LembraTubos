package com.johulk.tarefaguru.database.servico

import androidx.room.* // ktlint-disable no-wildcard-imports
import androidx.room.ForeignKey.Companion.CASCADE
import com.johulk.tarefaguru.database.cliente.Cliente
import java.io.Serializable
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

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
data class Servico(

    @field:ColumnInfo(name = "clienteId") var clienteId: Int,

    @field:ColumnInfo(name = "DateTime") var dateTimeEpoch: Long,

    @field:ColumnInfo(name = "valor") var valor: Double,

    @field:ColumnInfo(name = "pago") var pago: Boolean,

    @PrimaryKey(autoGenerate = true)
    var servcoid: Int = 0,
) : Serializable {
    fun dateTimeMarcado(): LocalDateTime = Instant.ofEpochMilli(dateTimeEpoch).atZone(ZoneId.systemDefault()).toLocalDateTime()

    companion object {
        fun fromDateTime(clienteId: Int, dateTime: LocalDateTime, pago: Boolean, valor: Double): Servico {
            return Servico(
                clienteId = clienteId,
                dateTimeEpoch = dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(),
                pago = pago,
                valor = valor,
            )
        }
    }
}
