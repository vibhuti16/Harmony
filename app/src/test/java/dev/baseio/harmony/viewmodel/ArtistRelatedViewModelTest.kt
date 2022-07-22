package dev.baseio.harmony.viewmodel

import MainCoroutineRule
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import MockUtil
import dev.baseio.harmony.core.data.ApiResult
import dev.baseio.harmony.data.ArtistRelatedData
import dev.baseio.harmony.domain.network.HarmonyClient
import dev.baseio.harmony.domain.network.HarmonyService
import dev.baseio.harmony.ui.artist.ArtistRepository
import dev.baseio.harmony.ui.artist.details.related.ArtistRelatedViewModel
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

class ArtistRelatedViewModelTest {
    private lateinit var viewModel: ArtistRelatedViewModel
    private lateinit var repository: ArtistRepository
    private lateinit var deezerClient: HarmonyClient

    @MockK
    private lateinit var harmonyService: HarmonyService

    @ExperimentalCoroutinesApi
    @get:Rule
    var coroutineRule = MainCoroutineRule()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @Before
    fun setup(){
        MockKAnnotations.init(this, relaxed = true)
        deezerClient = HarmonyClient(harmonyService = harmonyService)
        repository = ArtistRepository(deezerClient, Dispatchers.IO)
        viewModel = ArtistRelatedViewModel(repository)
    }

    @Test
    fun fetchArtistDetailsTest() = runBlocking {
        val mockList = listOf(MockUtil.artistRelatedData)

        val observer : Observer<ApiResult<List<ArtistRelatedData>>> = mock()
        val fetchedData : LiveData<ApiResult<List<ArtistRelatedData>>> = repository.fetchArtistRelated(
          MockUtil.artistID).asLiveData()
        fetchedData.observeForever(observer)

        viewModel.fetchArtistRelated(MockUtil.artistID)
        delay(500L)

        verify(observer).onChanged(ApiResult.Success(mockList))
        fetchedData.removeObserver(observer)
    }

}