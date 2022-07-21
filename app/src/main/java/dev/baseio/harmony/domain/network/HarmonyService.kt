package dev.baseio.harmony.domain.network

import dev.baseio.harmony.data.*
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface HarmonyService{

    @GET(Endpoints.GENRE)
    suspend fun fetchGenreList()
            : Response<GenreResponse>

    @GET(Endpoints.ARTISTS)
    suspend fun fetchArtistList(@Path("genreId") genreId:String)
            : Response<ArtistsResponse>

    @GET(Endpoints.ARTIST)
    suspend fun fetchArtistDetails(@Path("artistId") artistID:String)
            : Response<ArtistDetailResponse>

    @GET(Endpoints.ALBUMS)
    suspend fun fetchArtistAlbums(@Path("artistId") artistID: String)
            : Response<ArtistAlbumResponse>

    @GET(Endpoints.RELATED)
    suspend fun fetchArtistRelated(@Path("artistId") artistID: String)
            : Response<ArtistRelatedResponse>

    @GET(Endpoints.ALBUM)
    suspend fun fetchAlbumDetails(@Path("albumId") albumId:String)
            : Response<AlbumDetailsResponse>

    @GET(Endpoints.SEARCH)
    suspend fun fetchSearchAlbum(@Query("q") q:String)
            : Response<SearchResponse>

    @GET(Endpoints.EDITORIAL)
    suspend fun fetchEditorials()
    :Response<MainResponse>

    @GET(Endpoints.TRACKS)
    suspend fun fetchTracksByAlbum(@Query("q") q: String): Response<TrackResponse>

    @GET(Endpoints.TRACKS)
    suspend fun fetchTracksByArtist(@Path("artistId") albumId:String): Response<TrackResponse>

}
