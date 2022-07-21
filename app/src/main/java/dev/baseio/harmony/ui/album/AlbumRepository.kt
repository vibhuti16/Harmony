package dev.baseio.harmony.ui.album

import dev.baseio.harmony.core.data.ApiResult
import dev.baseio.harmony.core.data.DaoResult
import dev.baseio.harmony.core.data.DataSource
import dev.baseio.harmony.core.extensions.getResult
import dev.baseio.harmony.core.extensions.isSuccessAndNotNull
import dev.baseio.harmony.core.extensions.letOnFalseOnSuspend
import dev.baseio.harmony.core.extensions.letOnTrueOnSuspend
import dev.baseio.harmony.data.AlbumData
import dev.baseio.harmony.data.AlbumDetailsResponse
import dev.baseio.harmony.di.IODispatcher
import dev.baseio.harmony.domain.local.HarmonyDao
import dev.baseio.harmony.domain.network.HarmonyClient
import dev.baseio.harmony.entities.AlbumEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import timber.log.Timber
import javax.inject.Inject

private const val FAKE_DELAY_TIME = 1500L

class AlbumRepository @Inject constructor(
    private val harmonyClient: HarmonyClient,
    private val harmonyDao: HarmonyDao,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher
): DataSource(), AlbumRepositoryImpl {

    override fun fetchAlbumTracks(albumID:String
    ) = flow {
        emit(ApiResult.Loading)
        networkCall {
            harmonyClient.fetchAlbumDetails(albumID)
        }.let { apiResult ->
            apiResult.isSuccessAndNotNull().letOnTrueOnSuspend {
                Timber.d("fetchAlbumTracks apiResult : ${apiResult.getResult() }")
                Timber.d("fetchAlbumTracks apiResult : ${(apiResult.getResult() as AlbumDetailsResponse).data}")
                val response = (apiResult.getResult() as AlbumDetailsResponse).data
                response.forEach { it.durationToTime() }
                emit(ApiResult.Success(response))
            }.letOnFalseOnSuspend {
                /* fake call */
                delay(FAKE_DELAY_TIME)
                emit(ApiResult.Error(Exception("Unexpected error.")))
            }
        }
    }.flowOn(ioDispatcher)

    override suspend fun insertFavoritesData(track: AlbumEntity?) = localCallInsert {
        track?.let {
            harmonyDao.insertTrack(it)
        }
    }

}


interface AlbumRepositoryImpl {
    /**
     * give to id return fetching album tracks.
     * @param albumID, String
     * @return Result.Error or Result.Succes(List<AlbumData>)
     * */
    fun fetchAlbumTracks(albumID:String): Flow<ApiResult<List<AlbumData>>>


    /**
     * @param track, AlbumData
     * insert the track on local data.
     * */
    suspend fun insertFavoritesData(track: AlbumEntity?): DaoResult

}
