package dev.baseio.harmony.viewmodel

import MainCoroutineRule
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import dev.baseio.harmony.core.data.ApiResult
import dev.baseio.harmony.data.SearchData
import dev.baseio.harmony.domain.local.HarmonyDao
import dev.baseio.harmony.domain.network.HarmonyClient
import dev.baseio.harmony.domain.network.HarmonyService
import dev.baseio.harmony.ui.search.SearchRepository
import dev.baseio.harmony.ui.search.SearchViewModel
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
class SearchViewModelTest {
    private lateinit var viewModel:SearchViewModel
    private lateinit var searchRepository: SearchRepository

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
        searchRepository = SearchRepository(deezerClient,deezerDao,Dispatchers.IO)
        viewModel = SearchViewModel(searchRepository)
    }

    @Test
    fun fetchSearchTest() = runBlocking {
        val mockData = MockUtil.recentData
        val mockList = listOf(MockUtil.searchData)
        whenever(deezerDao.getQueryList()).thenReturn(listOf(mockData))

        val observer : Observer<ApiResult<List<SearchData>>> = mock()
        val fetchedData : LiveData<ApiResult<List<SearchData>>> = searchRepository.fetchSearch(
          MockUtil.query).asLiveData()
        fetchedData.observeForever(observer)

        viewModel.fetchingRecentSearch()
        delay(500L)

        verify(deezerDao.insertQuery(mockData))
        verify(deezerDao, atLeastOnce()).getGenreList()
        verify(observer).onChanged(ApiResult.Success(mockList))
        fetchedData.removeObserver(observer)
    }
}