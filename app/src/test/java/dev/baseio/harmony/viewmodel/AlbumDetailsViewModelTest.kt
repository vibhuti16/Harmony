package dev.baseio.harmony.viewmodel

import MainCoroutineRule
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import MockUtil
import dev.baseio.harmony.core.data.ApiResult
import dev.baseio.harmony.data.AlbumData
import dev.baseio.harmony.domain.local.HarmonyDao
import dev.baseio.harmony.domain.network.HarmonyClient
import dev.baseio.harmony.domain.network.HarmonyService
import dev.baseio.harmony.ui.album.AlbumDetailsViewModel
import dev.baseio.harmony.ui.album.AlbumRepository
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
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
class AlbumDetailsViewModelTest {
    private lateinit var viewModel: AlbumDetailsViewModel
    private lateinit var repository: AlbumRepository

    private lateinit var deezerClient: HarmonyClient

    @MockK
    private lateinit var HarmonyService: HarmonyService
    @MockK
    private lateinit var deezerDao: HarmonyDao

    @ExperimentalCoroutinesApi
    @get:Rule
    var coroutineRule = MainCoroutineRule()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @Before
    fun setup(){
        MockKAnnotations.init(this, relaxed = true)
        deezerClient = HarmonyClient(HarmonyService)
        repository = AlbumRepository(deezerClient,deezerDao, Dispatchers.IO)
        viewModel = AlbumDetailsViewModel(repository)
    }

    @Test
    fun fetchAlbumListTest() = runBlocking {
        val mockList = listOf(MockUtil.album)

        val observer : Observer<ApiResult<List<AlbumData>>> = mock()
        val fetchedData : LiveData<ApiResult<List<AlbumData>>> = repository.fetchAlbumTracks("302127").asLiveData()
        fetchedData.observeForever(observer)

        viewModel.fetchingAlbumDatas(MockUtil.albumID)
        delay(500L)

        verify(observer).onChanged(ApiResult.Success(mockList))
        fetchedData.removeObserver(observer)


    }



}