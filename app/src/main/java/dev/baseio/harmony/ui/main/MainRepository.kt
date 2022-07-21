package dev.baseio.harmony.ui.main

import dev.baseio.harmony.core.data.ApiResult
import dev.baseio.harmony.core.data.DataSource
import dev.baseio.harmony.core.extensions.getResult
import dev.baseio.harmony.core.extensions.isSuccessAndNotNull
import dev.baseio.harmony.core.extensions.letOnFalseOnSuspend
import dev.baseio.harmony.core.extensions.letOnTrueOnSuspend
import dev.baseio.harmony.core.mapper.mapper
import dev.baseio.harmony.data.GenreResponse
import dev.baseio.harmony.data.MainResponse
import dev.baseio.harmony.di.IODispatcher
import dev.baseio.harmony.domain.local.HarmonyDao
import dev.baseio.harmony.domain.network.HarmonyClient
import dev.baseio.harmony.entities.GenreEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

private const val FAKE_DELAY_TIME = 1500L

class MainRepository @Inject constructor(
    private val harmonyClient: HarmonyClient,
    private val harmonyDao: HarmonyDao,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher
): DataSource(), MainRepositoryImpl {

    override suspend fun fetchGenreList() = flow {
        emit(ApiResult.Loading)
        localCallFetch {
            harmonyDao.getGenreList()
        }.let { localResult ->
            localResult.isSucces.letOnFalseOnSuspend {
                networkCall {
                    harmonyClient.fetchGenreList()
                }.let { apiResult->
                    apiResult.isSuccessAndNotNull().letOnTrueOnSuspend {
                        (apiResult.getResult() as? GenreResponse)?.data?.let {
                            localCallInsert { harmonyDao.insertGenreList(it.mapper()) }
                            emit(ApiResult.Success(it))
                        } ?: run {  emit(ApiResult.Error(TypeCastException("unkown error."))) }
                    }
                }
            }.letOnTrueOnSuspend {
                val result = (localResult.data as? List<GenreEntity>)?.mapper()
                delay(FAKE_DELAY_TIME)
                emit(ApiResult.Success(result?.toList()))
            }
        }
    }.flowOn(ioDispatcher)

    override suspend fun fetchMainList()= flow {
        emit(ApiResult.Loading)
        networkCall {
            harmonyClient.fetchEditorial()
        }.let { apiResult->
            apiResult.isSuccessAndNotNull().letOnTrueOnSuspend {
                (apiResult.getResult() as? MainResponse)?.let {
//                    localCallInsert { harmonyDao.insertGenreList(it.mapper()) }
                    emit(ApiResult.Success(it))
                } ?: run {  emit(ApiResult.Error(TypeCastException("unkown error."))) }
            }
        }
    }.flowOn(ioDispatcher)
}


interface MainRepositoryImpl {
    /**
     * give to id return fetching genreList list.
     * @return Result.Error or Result.Succes(List<Data>)
     * */
    suspend fun fetchGenreList(): Flow<ApiResult<*>>

    suspend fun fetchMainList():Flow<ApiResult<*>>
}

