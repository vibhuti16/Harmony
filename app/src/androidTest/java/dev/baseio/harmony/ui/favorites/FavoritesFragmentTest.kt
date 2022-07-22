package dev.baseio.harmony.ui.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import dev.baseio.harmony.R
import dev.baseio.harmony.core.data.ApiResult
import dev.baseio.harmony.domain.local.HarmonyDao
import dev.baseio.harmony.domain.network.HarmonyClient
import dev.baseio.harmony.domain.network.HarmonyService
import dev.baseio.harmony.entities.AlbumEntity
import dev.baseio.harmony.launchFragmentInHiltContainer
import dev.baseio.harmony.utilies.isVisible
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import org.junit.Before

import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import dev.baseio.harmony.MainCoroutineRule
import dev.baseio.harmony.MockUtil.albumEntity
import dev.baseio.harmony.utilies.isGone

/**
 * @author: fevziomurtekin
 * @date: 17/04/2021
 */
@ExperimentalCoroutinesApi
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
@MediumTest
class FavoritesFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    lateinit var controller: NavController

    var fragment: FavoritesFragment? = null

    @MockK
    private lateinit var repository: FavoritesRepository

    private lateinit var viewModel: FavoritesViewModel

    @MockK
    lateinit var deezerService: HarmonyService

    lateinit var deezerClient: HarmonyClient

    @MockK
    lateinit var deezerDao: HarmonyDao

    @ExperimentalCoroutinesApi
    @get:Rule
    var coroutineRule = MainCoroutineRule()

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxed = true)
        controller = TestNavHostController(
            ApplicationProvider.getApplicationContext())
        controller.setGraph(R.navigation.nav_graph)

        launchFragmentInHiltContainer<FavoritesFragment> {
            Navigation.setViewNavController(requireView(), controller)
            fragment = this as? FavoritesFragment
        }
        deezerClient = HarmonyClient(deezerService)
        viewModel = FavoritesViewModel(repository)
    }

    @Test
    fun clicked_item_after_then_load_favorites_data_show_media_player() {
        fragment?.let { favoritesFragment ->
            //given
            val res = listOf(albumEntity, albumEntity, albumEntity)

            coEvery { deezerDao.getFavorites() } returns res

            //when
            coroutineRule.launch {
                val observer : Observer<ApiResult<List<AlbumEntity>?>> = mockk()
                val favoritesData : LiveData<ApiResult<List<AlbumEntity>?>> =
                    repository.fetchFavorites().asLiveData()
                favoritesData.observeForever(observer)

                viewModel.fetchFavorites()
            }
            clickedFavoritesItem()

            onView(withId(R.id.cl_media_player)).isGone()
        }
    }

    private fun clickedFavoritesItem() {
        onView(withId(R.id.recycler_favorites))
            .perform(RecyclerViewActions
                .actionOnItemAtPosition<RecyclerView.ViewHolder>(
                2, click()))
    }
}
