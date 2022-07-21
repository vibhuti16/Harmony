package dev.baseio.harmony.ui.search

import dev.baseio.harmony.core.data.ApiResult
import dev.baseio.harmony.core.data.DaoResult
import dev.baseio.harmony.core.data.DataSource
import dev.baseio.harmony.data.SearchResponse
import dev.baseio.harmony.di.IODispatcher
import dev.baseio.harmony.domain.local.HarmonyDao
import dev.baseio.harmony.domain.network.HarmonyClient
import dev.baseio.harmony.entities.SearchEntity
import dev.baseio.harmony.core.extensions.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

private const val FAKE_DELAY_TIME = 1500L

class SearchRepository @Inject constructor(
    private val harmonyClient: HarmonyClient,
    private val harmonyDao: HarmonyDao,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher
): DataSource(), SearchRepositoryImpl {


    override fun fetchSearch(query:String) = flow{
        emit(ApiResult.Loading)
        localCallInsert {
            insertSearch(SearchEntity(q=query))
        }.data.isNotNull().letOnTrueOnSuspend {
            networkCall {
                harmonyClient.fetchSearchAlbum(query)
            }.let { apiResult ->
                apiResult.isSuccessAndNotNull().letOnTrueOnSuspend {
                    emit(ApiResult.Success((apiResult.getResult() as SearchResponse).data))
                }.letOnFalseOnSuspend {
                    /* fake call */
                    delay(FAKE_DELAY_TIME)
                    emit(ApiResult.Error(Exception("Unexpected error.")))
                }
            }
        }.letOnFalseOnSuspend {
            /* fake call */
            delay(FAKE_DELAY_TIME)
            emit(ApiResult.Error(Exception("Unexpected error.")))
        }
    }.flowOn(ioDispatcher)

    override fun fetchRecentSearch()= flow {
        localCallFetch {
            harmonyDao.getQueryList()
        }.let { localResult->
            localResult.isSuccessAndNotNull().letOnTrueOnSuspend {
                emit(ApiResult.Success(localResult.data as List<SearchEntity>))
            }.letOnFalseOnSuspend {
                emit(ApiResult.Error(Exception("Unexpected error.")))
            }
        }
    }.flowOn(ioDispatcher)

    override suspend fun insertSearch(query: SearchEntity)= localCallInsert {
        harmonyDao.insertQuery(query)
    }

}

interface SearchRepositoryImpl {

    /**
     * @return List<SearchQuery>?
     * */
    fun fetchRecentSearch() : Flow<ApiResult<List<*>>>

    /**
     * @param query, String
     * @return Result.Error or Result.Succes(List<SearchData>)
     * */
    fun fetchSearch(query:String) : Flow<ApiResult<List<*>>>

    /**
     * @param query,
     * insert the query.
     * */
    suspend fun insertSearch(query: SearchEntity): DaoResult

}
