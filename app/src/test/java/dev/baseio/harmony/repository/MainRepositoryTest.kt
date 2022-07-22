package dev.baseio.harmony.repository

import MainCoroutineRule
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import MockUtil
import dev.baseio.harmony.core.data.ApiResult
import dev.baseio.harmony.core.mapper.mapper
import dev.baseio.harmony.data.Data
import dev.baseio.harmony.data.GenreResponse
import dev.baseio.harmony.domain.local.HarmonyDao
import dev.baseio.harmony.domain.network.HarmonyClient
import dev.baseio.harmony.domain.network.HarmonyService
import dev.baseio.harmony.ui.main.MainRepository
import com.nhaarman.mockitokotlin2.atLeastOnce
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Response
import kotlin.time.ExperimentalTime

@ExperimentalCoroutinesApi
class MainRepositoryTest {

    private lateinit var repository: MainRepository
    private lateinit var client: HarmonyClient
    private val service: HarmonyService = mock()
    private val deezerDao: HarmonyDao = mock()

    @ExperimentalCoroutinesApi
    @get:Rule
    var coroutinesRule = MainCoroutineRule()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @Before
    fun setup() {
        client = HarmonyClient(service)
        repository = MainRepository(client, deezerDao, Dispatchers.IO)
    }

    @ExperimentalTime
    @Test
    fun fetchGenreListFromNetworkTest() = runBlocking {
        val mockData = GenreResponse(MockUtil.genres)
        val returnData = Response.success(mockData)
        whenever(deezerDao.getGenreList()).thenReturn(emptyList())
        whenever(service.fetchGenreList()).thenReturn(returnData)

        /** add to turbine library.*/
        repository.fetchGenreList().test {
            val result = expectItem()
            assertEquals(result, ApiResult.Success(MockUtil.genres))
            assertEquals((result as ApiResult.Success<List<Data>>).data[0].name , "All")
            assertEquals(result.data[0].id , "0")
            assertEquals(result.data[1].name , "Pop")
            assertEquals(result.data[1].id , "132")
            expectComplete()
        }

        verify(deezerDao, atLeastOnce()).getGenreList()
        verify(service, atLeastOnce()).fetchGenreList()
        verify(deezerDao, atLeastOnce()).insertGenreList(MockUtil.genres.mapper())

    }
}