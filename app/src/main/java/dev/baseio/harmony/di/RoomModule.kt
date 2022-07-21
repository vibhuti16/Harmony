package dev.baseio.harmony.di

import android.app.Application
import androidx.room.Room
import dev.baseio.harmony.core.Env
import dev.baseio.harmony.domain.local.HarmonyDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule{

    @Provides
    @Singleton
    fun providesHarmonyDatabase(application: Application)
        = Room.databaseBuilder(application, HarmonyDatabase::class.java, Env.DATABASE_NAME)
        .fallbackToDestructiveMigration()
        .build()

    @Provides
    @Singleton
    fun providesHarmonyDao(harmonyDatabase: HarmonyDatabase)
        = harmonyDatabase.harmonyDao()
}
