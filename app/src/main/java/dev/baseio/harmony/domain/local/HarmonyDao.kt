package dev.baseio.harmony.domain.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.baseio.harmony.entities.AlbumEntity
import dev.baseio.harmony.entities.GenreEntity
import dev.baseio.harmony.entities.SearchEntity

@Dao
interface HarmonyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGenreList(genreList:List<GenreEntity>)

    @Query("SELECT * FROM GenreEntity")
    suspend fun getGenreList():List<GenreEntity>?

    /* insert to query */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuery(q: SearchEntity)

    /* recent search */
    @Query("SELECT * FROM SearchEntity")
    suspend fun getQueryList():List<SearchEntity>

    /* insert to track */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(albumData: AlbumEntity)

    /* recent favorites */
    @Query("SELECT * FROM AlbumEntity")
    suspend fun getFavorites():List<AlbumEntity>?

}
