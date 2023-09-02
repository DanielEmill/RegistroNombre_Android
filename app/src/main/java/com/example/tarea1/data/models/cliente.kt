package com.example.tarea1.data.models
import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "clientes")
data class cliente(
    @PrimaryKey
    val clienteId: Int? = null,
    var nombre: String = ""
)