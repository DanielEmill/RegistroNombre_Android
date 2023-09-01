package com.example.tarea1.data.dao
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.tarea1.data.models.cliente
import kotlinx.coroutines.flow.Flow

@Dao
interface ClienteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(cliente: cliente)
    @Query(
        """
        SELECT * 
        FROM clientes
        WHERE clienteId=:id  
        LIMIT 1
        """
    )
    suspend fun find(id: Int): cliente
    @Delete
    suspend fun delete(cliente: cliente)
    @Query("SELECT * FROM clientes")
    fun getAll(): Flow<List<cliente>>
}
