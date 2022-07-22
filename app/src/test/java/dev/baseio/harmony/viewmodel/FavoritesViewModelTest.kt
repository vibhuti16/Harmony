package dev.baseio.harmony.viewmodel

import MainCoroutineRule
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import MockUtil
import dev.baseio.harmony.core.data.ApiResult
import dev.baseio.harmony.domain.local.HarmonyDao
import dev.baseio.harmony.domain.network.HarmonyClient
import dev.baseio.harmony.domain.network.HarmonyService
import dev.baseio.harmony.entities.AlbumEntity
import dev.baseio.harmony.ui.favorites.FavoritesRepository
import dev.baseio.harmony.ui.favorites.FavoritesViewModel
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
class FavoritesViewModelTest {
    private lateinit var viewModel: FavoritesViewModel
    private lateinit var repository: FavoritesRepository

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
        repository = FavoritesRepository(deezerDao, Dispatchers.IO)
        viewModel = FavoritesViewModel(repository)
    }

    @Test
    fun fetchSearchTest() = runBlocking {
        val mockData = MockUtil.albumEntity
        val mockList = listOf(MockUtil.albumEntity)
        whenever(deezerDao.getFavorites()).thenReturn(listOf(mockData))

        val observer : Observer<ApiResult<List<AlbumEntity>>> = mock()
        val fetchedData : LiveData<ApiResult<List<AlbumEntity>>> = repository.fetchFavorites().asLiveData()
        fetchedData.observeForever(observer)

        viewModel.fetchFavorites()
        delay(500L)

        verify(deezerDao.insertTrack(mockData))
        verify(deezerDao, atLeastOnce()).getFavorites()
        fetchedData.removeObserver(observer)
    }

}