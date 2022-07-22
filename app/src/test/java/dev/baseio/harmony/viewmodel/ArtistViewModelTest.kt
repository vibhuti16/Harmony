package dev.baseio.harmony.viewmodel

import MainCoroutineRule
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import MockUtil
import dev.baseio.harmony.core.data.ApiResult
import dev.baseio.harmony.data.ArtistData
import dev.baseio.harmony.domain.network.HarmonyClient
import dev.baseio.harmony.domain.network.HarmonyService
import dev.baseio.harmony.ui.artist.ArtistRepository
import dev.baseio.harmony.ui.artist.profile.ArtistViewModel
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
class ArtistViewModelTest {
    private lateinit var viewModel: ArtistViewModel
    private lateinit var repository: ArtistRepository

    @MockK
    private lateinit var deezerService: HarmonyService

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
        repository = ArtistRepository(deezerClient, Dispatchers.IO)
        viewModel = ArtistViewModel(repository)
    }

    @Test
    fun fetchArtistListTest() = runBlocking {
        val mockList = listOf(MockUtil.artist)

        val observer : Observer<ApiResult<List<ArtistData>>> = mock()
        val fetchedData : LiveData<ApiResult<List<ArtistData>>> = repository.fetchArtistList(
          MockUtil.genreID).asLiveData()
        fetchedData.observeForever(observer)

        viewModel.fetchResult(MockUtil.genreID)
        delay(500L)

        verify(observer).onChanged(ApiResult.Success(mockList))
        fetchedData.removeObserver(observer)


    }



}