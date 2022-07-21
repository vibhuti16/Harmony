package dev.baseio.harmony.domain.network

import javax.inject.Inject

class HarmonyClient @Inject constructor(
        private val harmonyService: HarmonyService
){

    suspend fun fetchGenreList()
            = harmonyService.fetchGenreList()

    suspend fun fetchArtistList(genreId:String)
            = harmonyService.fetchArtistList(genreId)

    suspend fun fetchArtistDetails(artistID: String)
            = harmonyService.fetchArtistDetails(artistID)

    suspend fun fetchArtistAlbums(artistID: String)
            = harmonyService.fetchArtistAlbums(artistID)

    suspend fun fetchArtistRelated(artistID: String)
            = harmonyService.fetchArtistRelated(artistID)

    suspend fun fetchAlbumDetails(albumID:String)
            = harmonyService.fetchAlbumDetails(albumID)

    suspend fun fetchSearchAlbum(q:String)
            = harmonyService.fetchSearchAlbum(q)

    suspend fun fetchEditorial()
            = harmonyService.fetchEditorials()

    suspend fun fetchTracksByArtist(artistID: String)
            = harmonyService.fetchTracksByArtist(artistID)

    suspend fun fetchTracksByAlbum(albumID:String)
            = harmonyService.fetchTracksByAlbum(albumID)
}
