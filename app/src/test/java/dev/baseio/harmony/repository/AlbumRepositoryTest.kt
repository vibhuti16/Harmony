package dev.baseio.harmony.repository

import MainCoroutineRule
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import dev.baseio.harmony.core.data.ApiResult
import dev.baseio.harmony.data.AlbumData
import dev.baseio.harmony.data.AlbumDetailsResponse
import dev.baseio.harmony.domain.local.HarmonyDao
import dev.baseio.harmony.domain.network.HarmonyClient
import dev.baseio.harmony.domain.network.HarmonyService
import dev.baseio.harmony.ui.album.AlbumRepository
import com.nhaarman.mockitokotlin2.*
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Response
import kotlin.time.ExperimentalTime

class AlbumRepositoryTest {

  private lateinit var repository: AlbumRepository
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
    repository = AlbumRepository(client, deezerDao, Dispatchers.IO)
  }


  @ExperimentalTime
  @Test
  fun fetchAlbumTracksFromNetworkTest() = runBlocking {
    val mockData = AlbumDetailsResponse(data = listOf(MockUtil.album), total = 12)
    val returnData = Response.success(mockData)
    whenever(service.fetchAlbumDetails(MockUtil.albumID)).thenReturn(returnData)

    repository.fetchAlbumTracks(MockUtil.albumID).test {
      val result = expectItem()
      assertEquals(result, ApiResult.Success(listOf(MockUtil.album)))
      Assert.assertEquals((result as ApiResult.Success<List<AlbumData>>).data[0].title, "Alo")
      Assert.assertEquals(result.data[0].id, "425605922")
      Assert.assertEquals(result.data[1].title, "Geceler")
      Assert.assertEquals(result.data[1].id, "425605932")
      expectComplete()
    }

    verify(service, atLeastOnce()).fetchAlbumDetails(MockUtil.albumID)
    verifyNoMoreInteractions(service)
  }
}