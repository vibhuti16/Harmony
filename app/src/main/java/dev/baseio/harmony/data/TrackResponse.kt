package dev.baseio.harmony.data

import com.google.gson.annotations.SerializedName

data class TrackResponse (

    @SerializedName("data"     ) var data     : ArrayList<Track> = arrayListOf(),
    @SerializedName("checksum" ) var checksum : String?         = null,
    @SerializedName("total"    ) var total    : Int?            = null

)

data class Track (

    @SerializedName("id"                      ) var id                    : Int?     = null,
    @SerializedName("readable"                ) var readable              : Boolean? = null,
    @SerializedName("title"                   ) var title                 : String?  = null,
    @SerializedName("title_short"             ) var titleShort            : String?  = null,
    @SerializedName("title_version"           ) var titleVersion          : String?  = null,
    @SerializedName("link"                    ) var link                  : String?  = null,
    @SerializedName("duration"                ) var duration              : Int?     = null,
    @SerializedName("rank"                    ) var rank                  : Int?     = null,
    @SerializedName("explicit_lyrics"         ) var explicitLyrics        : Boolean? = null,
    @SerializedName("explicit_content_lyrics" ) var explicitContentLyrics : Int?     = null,
    @SerializedName("explicit_content_cover"  ) var explicitContentCover  : Int?     = null,
    @SerializedName("preview"                 ) var preview               : String?  = null,
    @SerializedName("md5_image"               ) var md5Image              : String?  = null,
    @SerializedName("time_add"                ) var timeAdd               : Int?     = null,
    @SerializedName("artist"                  ) var artist                : ArtistTrack?  = ArtistTrack(),
    @SerializedName("album"                   ) var album                 : Album?   = Album(),
    @SerializedName("type"                    ) var type                  : String?  = null

)

data class Album (

    @SerializedName("id"           ) var id          : Int?    = null,
    @SerializedName("title"        ) var title       : String? = null,
    @SerializedName("cover"        ) var cover       : String? = null,
    @SerializedName("cover_small"  ) var coverSmall  : String? = null,
    @SerializedName("cover_medium" ) var coverMedium : String? = null,
    @SerializedName("cover_big"    ) var coverBig    : String? = null,
    @SerializedName("cover_xl"     ) var coverXl     : String? = null,
    @SerializedName("md5_image"    ) var md5Image    : String? = null,
    @SerializedName("tracklist"    ) var tracklist   : String? = null,
    @SerializedName("type"         ) var type        : String? = null

)

data class ArtistTrack (

    @SerializedName("id"             ) var id            : Int?    = null,
    @SerializedName("name"           ) var name          : String? = null,
    @SerializedName("link"           ) var link          : String? = null,
    @SerializedName("picture"        ) var picture       : String? = null,
    @SerializedName("picture_small"  ) var pictureSmall  : String? = null,
    @SerializedName("picture_medium" ) var pictureMedium : String? = null,
    @SerializedName("picture_big"    ) var pictureBig    : String? = null,
    @SerializedName("picture_xl"     ) var pictureXl     : String? = null,
    @SerializedName("tracklist"      ) var tracklist     : String? = null,
    @SerializedName("type"           ) var type          : String? = null

)
