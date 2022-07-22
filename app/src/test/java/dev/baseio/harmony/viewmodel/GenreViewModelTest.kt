package dev.baseio.harmony.viewmodel

import MainCoroutineRule
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import MockUtil
import dev.baseio.harmony.core.data.ApiResult
import dev.baseio.harmony.data.Data
import dev.baseio.harmony.domain.local.HarmonyDao
import dev.baseio.harmony.domain.network.HarmonyClient
import dev.baseio.harmony.domain.network.HarmonyService
import dev.baseio.harmony.ui.genre.GenreViewModel
import dev.baseio.harmony.ui.main.MainRepository
import com.nhaarman.mockitokotlin2.atLeastOnce
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class GenreViewModelTest {
    private lateinit var viewModel: GenreViewModel
    private lateinit var repository: MainRepository

    @MockK
    private lateinit var deezerService: HarmonyService

    @MockK
    private lateinit var deezerDao: HarmonyDao

    private lateinit var deezerClient: HarmonyClient

    @ExperimentalCoroutinesApi
    @get:Rule
    var coroutineRule = MainCoroutineRule()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @Before
    fun setup(){
        MockKAnnotations.init(this, relaxed = true)
        deezerClient = HarmonyClient(deezerService)
        repository = MainRepository(deezerClient,deezerDao, Dispatchers.IO)
        viewModel = GenreViewModel(repository)
    }

    @Test
    fun fetchGenreListTest() = runBlocking {
        val mockListEntity = MockUtil.genreEntityList
        val mockListData = MockUtil.genres
        whenever(deezerDao.getGenreList()).thenReturn(mockListEntity)

        val observer : Observer<ApiResult<List<Data>?>> = mock()
        val fetchedData : LiveData<ApiResult<List<Data>?>> = repository.fetchGenreList().asLiveData()
        fetchedData.observeForever(observer)

        viewModel.fetchResult()
        delay(500L)

        verify(deezerDao, atLeastOnce()).getGenreList()
        verify(observer).onChanged(ApiResult.Success(mockListData))
        fetchedData.removeObserver(observer)
    }
}