package dev.baseio.harmony.data

import com.google.gson.annotations.SerializedName


data class MainResponse (

  @SerializedName("tracks"    ) var tracks    : Tracks?    = Tracks(),
  @SerializedName("albums"    ) var albums    : Albums?    = Albums(),
  @SerializedName("artists"   ) var artists   : Artists?   = Artists(),
  @SerializedName("playlists" ) var playlists : Playlists? = Playlists(),
  @SerializedName("podcasts"  ) var podcasts  : Podcasts?  = Podcasts()

)

data class Albums (

  @SerializedName("data"  ) var data  : ArrayList<User> = arrayListOf(),
  @SerializedName("total" ) var total : Int?              = null

)

data class Artists (

  @SerializedName("data"  ) var data  : ArrayList<User> = arrayListOf(),
  @SerializedName("total" ) var total : Int?            = null

)

data class Playlists (

  @SerializedName("data"  ) var data  : ArrayList<PlayList> = arrayListOf(),
  @SerializedName("total" ) var total : Int?            = null

)

data class Podcasts (

  @SerializedName("data"  ) var data  : ArrayList<PlayList> = arrayListOf(),
  @SerializedName("total" ) var total : Int?            = null

)



data class Tracks (

  @SerializedName("data"  ) var data  : ArrayList<PlayList> = arrayListOf(),
  @SerializedName("total" ) var total : Int?              = null

)

data class PlayList (

  @SerializedName("id"             ) var id            : Long?     = null,
  @SerializedName("title"          ) var title         : String?  = null,
  @SerializedName("description"    ) var description   : String?  = null,
  @SerializedName("available"      ) var available     : Boolean? = null,
  @SerializedName("fans"           ) var fans          : Int?     = null,
  @SerializedName("link"           ) var link          : String?  = null,
  @SerializedName("share"          ) var share         : String?  = null,
  @SerializedName("picture"        ) var picture       : String?  = null,
  @SerializedName("picture_small"  ) var pictureSmall  : String?  = null,
  @SerializedName("picture_medium" ) var pictureMedium : String?  = null,
  @SerializedName("picture_big"    ) var pictureBig    : String?  = null,
  @SerializedName("picture_xl"     ) var pictureXl     : String?  = null,
  @SerializedName("type"           ) var type          : String?  = null

)

data class User (

  @SerializedName("id"        ) var id        : Int?    = null,
  @SerializedName("name"      ) var name      : String? = null,
  @SerializedName("tracklist" ) var tracklist : String? = null,
  @SerializedName("type"      ) var type      : String? = null,
  @SerializedName("picture_medium" ) var pictureMedium : String?  = null,

)