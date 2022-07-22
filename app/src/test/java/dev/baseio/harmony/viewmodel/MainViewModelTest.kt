package dev.baseio.harmony.viewmodel

import MainCoroutineRule
import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import androidx.test.core.app.ActivityScenario.launch
import MockUtil
import dev.baseio.harmony.core.data.ApiResult
import dev.baseio.harmony.core.mapper.mapper
import dev.baseio.harmony.data.Data
import dev.baseio.harmony.data.MediaPlayerState
import dev.baseio.harmony.domain.local.HarmonyDao
import dev.baseio.harmony.domain.network.HarmonyClient
import dev.baseio.harmony.domain.network.HarmonyService
import dev.baseio.harmony.ui.main.MainActivity
import dev.baseio.harmony.ui.main.MainRepository
import dev.baseio.harmony.ui.main.MainViewModel
import dev.baseio.harmony.ui.main.playMediaPlayer
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
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test


/**
*   While writing some tests, I used the tests at
*   "https://github.com/Koducation/AndroidCourse101/blob/master/app/src/test/java/com/koducation/androidcourse101/PlayerViewStateTest.kt."
*   Thank you @muratcanbur.
 * */

@ExperimentalCoroutinesApi
class MainViewModelTest {
    private val application:Application = mock()
    private lateinit var viewModel:MainViewModel
    private lateinit var mainRepository: MainRepository

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
        mainRepository = MainRepository(deezerClient, deezerDao,Dispatchers.IO)
        viewModel = MainViewModel(application, mainRepository)
    }

    @Test
    fun fetchGenreListTest() = runBlocking {
        val mockList = MockUtil.genreEntityList
        whenever(deezerDao.getGenreList()).thenReturn(mockList)

        val observer : Observer<ApiResult<List<Data>?>> = mock()
        val fetchedData : LiveData<ApiResult<List<Data>?>> = mainRepository.fetchGenreList().asLiveData()
        fetchedData.observeForever(observer)

        viewModel.fetchGenreList()
        delay(500L)

        verify(deezerDao, atLeastOnce()).getGenreList()
        verify(observer).onChanged(ApiResult.Success(mockList.mapper()))
        fetchedData.removeObserver(observer)
    }

    @Test
    fun `given mediaPlayer PLAYING state, when ibtnPlayPlayer is called, then return match two values`() {

        /*  Given */
        val playerState = MediaPlayerState.PLAYING
        viewModel.playMediaPlayer()

        /* When */
        val actualResult = viewModel.mediaPlayerState.value.let {
            it?: MediaPlayerState.ERROR
        }

        /* Then */
        Assert.assertEquals(playerState, actualResult)
    }

    @Test
    fun `given mediaPlayer PLAYING state, when onBackPressed action, then return mediaPlayerState PAUSED`(){
        val scenario = launch(MainActivity::class.java)

        /* When */
        scenario.onActivity {a->
            a.onBackPressed()
        }
        val actualResult = viewModel.mediaPlayerState.value.let {
            it?: MediaPlayerState.ERROR
        }


        Assert.assertEquals(actualResult, MediaPlayerState.PAUSED)
    }

}