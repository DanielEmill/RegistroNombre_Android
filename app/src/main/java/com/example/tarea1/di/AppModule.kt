package com.example.tarea1.di
import android.content.Context
import androidx.room.Room
import com.example.tarea1.data.ClienteDb
import com.example.tarea1.data.dao.ClienteDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn( SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideTicketDatabase(@ApplicationContext appContext: Context): ClienteDb =
        Room.databaseBuilder(
            appContext,
            ClienteDb::class.java,
            "Cliente.db")
            .fallbackToDestructiveMigration()
            .build()
}
