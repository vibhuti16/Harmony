package dev.baseio.harmony.domain.network

object Endpoints {
    const val GENRE = "/genre"
    const val ARTISTS = "genre/{genreId}/artists"
    const val ARTIST = "artist/{artistId}"
    const val ALBUMS = "artist/{artistId}/albums"
    const val RELATED = "artist/{artistId}/related"
    const val ALBUM = "album/{albumId}/tracks"
    const val SEARCH = "search/album"
    const val EDITORIAL = "editorial/0/charts"
    const val TRACKS = "playlist/{artistId}/tracks"

}
