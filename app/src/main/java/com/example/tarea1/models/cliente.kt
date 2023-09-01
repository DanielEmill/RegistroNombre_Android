package com.example.tarea1.models
import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "cliente")
data class cliente (
    @PrimaryKey
    val clienteId: Int?=null ,
    var nombre: String = "",
)