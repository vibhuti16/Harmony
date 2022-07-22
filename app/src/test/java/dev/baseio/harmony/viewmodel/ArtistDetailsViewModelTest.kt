package dev.baseio.harmony.viewmodel

import MainCoroutineRule
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import MockUtil
import dev.baseio.harmony.core.data.ApiResult
import dev.baseio.harmony.data.ArtistDetailResponse
import dev.baseio.harmony.domain.network.HarmonyClient
import dev.baseio.harmony.domain.network.HarmonyService
import dev.baseio.harmony.ui.artist.ArtistRepository
import dev.baseio.harmony.ui.artist.details.info.ArtistDetailsViewModel
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

class ArtistDetailsViewModelTest {
    private lateinit var viewModel: ArtistDetailsViewModel
    private lateinit var repository: ArtistRepository

    private lateinit var deezerClient: HarmonyClient

    @MockK
    private lateinit var deezerService: HarmonyService

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
        viewModel = ArtistDetailsViewModel(repository)
    }

    @Test
    fun fetchArtistDetailsTest() = runBlocking {
        val mockList = MockUtil.artistDetails

        val observer : Observer<ApiResult<ArtistDetailResponse>> = mock()
        val fetchedData : LiveData<ApiResult<ArtistDetailResponse>> = repository.fetchArtistDetails(
          MockUtil.artistID).asLiveData()
        fetchedData.observeForever(observer)

        viewModel.fetchArtistDetails(MockUtil.artistID)
        delay(500L)

        verify(observer).onChanged(ApiResult.Success(mockList))
        fetchedData.removeObserver(observer)


    }



}