package dev.baseio.harmony.ui.favorites

import dev.baseio.harmony.core.data.ApiResult
import dev.baseio.harmony.core.data.DataSource
import dev.baseio.harmony.core.extensions.isSuccessAndNotNull
import dev.baseio.harmony.core.extensions.letOnFalseOnSuspend
import dev.baseio.harmony.core.extensions.letOnTrueOnSuspend
import dev.baseio.harmony.di.IODispatcher
import dev.baseio.harmony.domain.local.HarmonyDao
import dev.baseio.harmony.entities.AlbumEntity
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import timber.log.Timber
import javax.inject.Inject

private const val DELAY_TIME = 1500L

class FavoritesRepository @Inject constructor(
    private val harmonyDao: HarmonyDao,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher
): DataSource(), FavoritesRepositoryImpl {

    override fun fetchFavorites()= flow {
        localCallFetch {
            harmonyDao.getFavorites()
        }.let { localResult ->
            localResult.isSuccessAndNotNull().letOnTrueOnSuspend {
                Timber.d("getResult : ${Gson().toJson(localResult.data)}")
                emit(ApiResult.Success(localResult.data as List<AlbumEntity>))
            }.letOnFalseOnSuspend {
                /* fake call */
                delay(DELAY_TIME)
                emit(ApiResult.Error(Exception("Unexpected error.")))
            }
        }
    }.flowOn(ioDispatcher)
}



interface FavoritesRepositoryImpl{
    /**
     * @return List<Favorites>?
     * */
    fun fetchFavorites() : Flow<ApiResult<List<*>>>
}
