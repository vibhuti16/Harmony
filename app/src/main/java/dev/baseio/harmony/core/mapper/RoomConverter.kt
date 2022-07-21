package dev.baseio.harmony.core.mapper

import androidx.room.TypeConverter
import dev.baseio.harmony.entities.AlbumEntity
import dev.baseio.harmony.entities.GenreEntity
import dev.baseio.harmony.entities.SearchEntity
import com.google.gson.Gson

object RoomConverter {

    /* --------------------------  Decode Entity Class --------------------------------*/

    @JvmStatic
    @TypeConverter
    fun fromStringToGenreEntity(value:String): GenreEntity
            = Gson().fromJson(value, GenreEntity::class.java)


    @JvmStatic
    @TypeConverter
    fun fromStringToSearchEntity(value:String): SearchEntity
            = Gson().fromJson(value, SearchEntity::class.java)

    @JvmStatic
    @TypeConverter
    fun fromStringToAlbumEntity(value:String): AlbumEntity
            = Gson().fromJson(value, AlbumEntity::class.java)



    /* --------------------------------  Encode String ----------------------------------*/

    @JvmStatic
    @TypeConverter
    fun fromGenreEntityToString(value: GenreEntity):String
            = Gson().toJson(value)

    @JvmStatic
    @TypeConverter
    fun fromSearchEntityToString(value: SearchEntity):String
            = Gson().toJson(value)

    @JvmStatic
    @TypeConverter
    fun fromAlbumEntityToString(value: AlbumEntity):String
            = Gson().toJson(value)

}
