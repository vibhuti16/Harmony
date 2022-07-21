package dev.baseio.harmony.ui.artist

import dev.baseio.harmony.core.data.ApiResult
import dev.baseio.harmony.core.data.DataSource
import dev.baseio.harmony.core.extensions.getResult
import dev.baseio.harmony.core.extensions.isSuccessAndNotNull
import dev.baseio.harmony.core.extensions.letOnFalseOnSuspend
import dev.baseio.harmony.core.extensions.letOnTrueOnSuspend
import dev.baseio.harmony.data.ArtistAlbumResponse
import dev.baseio.harmony.data.ArtistDetailResponse
import dev.baseio.harmony.data.ArtistRelatedResponse
import dev.baseio.harmony.data.ArtistsResponse
import dev.baseio.harmony.di.IODispatcher
import dev.baseio.harmony.domain.network.HarmonyClient
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import timber.log.Timber
import javax.inject.Inject

private const val FAKE_DELAY_TIME = 1500L

class ArtistRepository @Inject constructor(
    private val harmonyClient: HarmonyClient,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher
): DataSource(), ArtistRepositoryImpl {

    override fun fetchArtistList(genreID:String)
            = flow {
        emit(ApiResult.Loading)
        networkCall {
            harmonyClient.fetchArtistList(genreID)
        }.let { apiResult ->
            apiResult.isSuccessAndNotNull().letOnTrueOnSuspend {
                emit(
                    ApiResult.Success(
                        (apiResult.getResult() as ArtistsResponse)
                            .artistData
                    )
                )
            }.letOnFalseOnSuspend {
                delay(FAKE_DELAY_TIME)
                emit(ApiResult.Error(Exception("Unexpected error.")))
            }
        }
    }.flowOn(ioDispatcher)


    override fun fetchArtistDetails(artistID:String
    ) = flow {
        emit( ApiResult.Loading )
        networkCall {
            harmonyClient.fetchArtistDetails(artistID)
        }.let { apiResult ->
            Timber.d("fetchArtistDetails - api result : $apiResult")
            apiResult.isSuccessAndNotNull().letOnTrueOnSuspend {
                emit(ApiResult.Success(apiResult.getResult() as ArtistDetailResponse))
            }.letOnTrueOnSuspend {
                /* fake call */
                delay(FAKE_DELAY_TIME)
                emit(ApiResult.Error(Exception("Unexpected error.")))
            }
        }
    }.flowOn(ioDispatcher)


    override fun fetchArtistAlbums(artistID:String
    ) = flow {
        emit( ApiResult.Loading )
        networkCall {
            harmonyClient.fetchArtistAlbums(artistID)
        }.let { apiResult ->
            apiResult.isSuccessAndNotNull().letOnTrueOnSuspend {
                Timber.d("fetchArtistAlbums api result : ${apiResult.getResult()}")
                emit(ApiResult.Success((apiResult.getResult() as ArtistAlbumResponse).data))
            }.letOnFalseOnSuspend {
                /* fake call */
                delay(FAKE_DELAY_TIME)
                emit(ApiResult.Error(Exception("Unexpected error.")))
            }
        }
    }.flowOn(ioDispatcher)



    override fun fetchArtistRelated(artistID:String
    ) = flow {
        emit( ApiResult.Loading )
        networkCall {
            harmonyClient.fetchArtistRelated(artistID)
        }.let { apiResult ->
            Timber.d("fetchArtistRelated - api result : $apiResult")
            apiResult.isSuccessAndNotNull().letOnTrueOnSuspend {
                emit(
                    ApiResult.Success(
                        (apiResult.getResult() as ArtistRelatedResponse).data
                    )
                )
            }.letOnFalseOnSuspend {
                /* fake call */
                delay(FAKE_DELAY_TIME)
                emit(ApiResult.Error(Exception("Unexpected error.")))
            }
        }
    }.flowOn(ioDispatcher)

}


interface ArtistRepositoryImpl {
    /**
     * give to id return fetching artist list.
     * @param genreID, String
     * @return Result.Error or Result.Succes(List<ArtistData>)
     * */
    fun fetchArtistList(genreID:String): Flow<ApiResult<*>>

    /**
     * give to id return fetching artist details.
     * @param artistID, String
     * @return Result.Error or Result.Succes(List<ArtistData>)
     * */
    fun fetchArtistDetails(artistID:String):Flow<ApiResult<*>>

    /**
     * give to id return fetching artist albums.
     * @param artistID, artistId
     * @return Result.Error or Result.Succes(List<ArtistAlbumData>)
     * */
    fun fetchArtistAlbums(artistID:String): Flow<ApiResult<List<*>>>

    /**
     * give to id return fetching artist related.
     * @param artistID, String
     * @return Result.Error or Result.Succes(List<ArtistRelatedData>)
     * */
    fun fetchArtistRelated(artistID: String): Flow<ApiResult<List<*>>>

}
