package dev.baseio.harmony.domain.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dev.baseio.harmony.core.Env
import dev.baseio.harmony.core.mapper.RoomConverter
import dev.baseio.harmony.entities.AlbumEntity
import dev.baseio.harmony.entities.GenreEntity
import dev.baseio.harmony.entities.SearchEntity

@Database(
    entities = [GenreEntity::class, SearchEntity::class, AlbumEntity::class],
    version = Env.DATABASE_VERSION, exportSchema = false
)
@TypeConverters(value = [RoomConverter::class])
abstract class HarmonyDatabase : RoomDatabase(){
    abstract fun harmonyDao() : HarmonyDao
}
