package com.example.tarea1.data
import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.tarea1.data.dao.ClienteDao
import com.example.tarea1.data.models.cliente

@Database(
    entities = [cliente::class],
    version = 1
)
abstract class ClienteDb: RoomDatabase(){
    abstract val ClienteDao: ClienteDao
}