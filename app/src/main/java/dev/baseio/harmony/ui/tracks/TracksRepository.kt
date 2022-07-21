package dev.baseio.harmony.ui.tracks

import dev.baseio.harmony.core.data.ApiResult
import dev.baseio.harmony.core.data.DataSource
import dev.baseio.harmony.core.extensions.getResult
import dev.baseio.harmony.core.extensions.isSuccessAndNotNull
import dev.baseio.harmony.core.extensions.letOnFalseOnSuspend
import dev.baseio.harmony.core.extensions.letOnTrueOnSuspend
import dev.baseio.harmony.data.AlbumData
import dev.baseio.harmony.data.Track
import dev.baseio.harmony.data.TrackResponse
import dev.baseio.harmony.data.Tracks
import dev.baseio.harmony.di.IODispatcher
import dev.baseio.harmony.domain.network.HarmonyClient
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class TracksRepository @Inject constructor(private val harmonyClient: HarmonyClient,@IODispatcher private val ioDispatcher: CoroutineDispatcher): TracksRepositoryImpl,DataSource(){
    override fun fetchTracksByArtist(artistId: String): Flow<ApiResult<List<Track>>> = flow {
        emit(ApiResult.Loading)
        networkCall {
            harmonyClient.fetchTracksByArtist(artistId)
        }.let {
            apiResult ->  apiResult.isSuccessAndNotNull().letOnTrueOnSuspend {
                val response = (apiResult.getResult() as TrackResponse).data
            emit(ApiResult.Success(response))
        }.letOnFalseOnSuspend {
            emit(ApiResult.Error(Exception("Unexpected error.")))
        }
        }
    }.flowOn(ioDispatcher)

        override fun fetchTracksByAlbum(albumId: String): Flow<ApiResult<List<Track>>> = flow<ApiResult<List<Track>>> {
            emit(ApiResult.Loading)
            networkCall {
                harmonyClient.fetchTracksByAlbum(albumId)
            }.let {
                apiResult ->  apiResult.isSuccessAndNotNull().letOnTrueOnSuspend {
                    val response = (apiResult.getResult() as TrackResponse).data
                emit(ApiResult.Success(response))
            }.letOnFalseOnSuspend {
                emit(ApiResult.Error(Exception("Unexpected error.")))
            }
            }
        }.flowOn(ioDispatcher)

}

interface TracksRepositoryImpl{
    fun fetchTracksByArtist(artistId: String): Flow<ApiResult<List<Track>>>
    fun fetchTracksByAlbum(albumId: String): Flow<ApiResult<List<Track>>>
}