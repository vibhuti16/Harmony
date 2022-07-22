package dev.baseio.harmony.viewmodel

import MainCoroutineRule
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import MockUtil
import dev.baseio.harmony.core.data.ApiResult
import dev.baseio.harmony.data.ArtistAlbumData
import dev.baseio.harmony.domain.network.HarmonyClient
import dev.baseio.harmony.domain.network.HarmonyService
import dev.baseio.harmony.ui.artist.ArtistRepository
import dev.baseio.harmony.ui.artist.details.albums.ArtistAlbumViewModel
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
class ArtistAlbumViewModelTest {
    private lateinit var viewModel: ArtistAlbumViewModel
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
        viewModel = ArtistAlbumViewModel(repository)
    }

    @Test
    fun fetchArtistAlbumTest() = runBlocking {
        val mockList = listOf(MockUtil.artistAlbum)

        val observer : Observer<ApiResult<List<ArtistAlbumData>>> = mock()
        val fetchedData : LiveData<ApiResult<List<ArtistAlbumData>>> = repository.fetchArtistAlbums(
          MockUtil.artistID).asLiveData()
        fetchedData.observeForever(observer)

        viewModel.fetchArtistAlbum(MockUtil.artistID)
        delay(500L)

        verify(observer).onChanged(ApiResult.Success(mockList))
        fetchedData.removeObserver(observer)
    }
}