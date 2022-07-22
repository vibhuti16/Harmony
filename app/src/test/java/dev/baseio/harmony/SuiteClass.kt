package dev.baseio.harmony

import dev.baseio.harmony.domain.network.HarmonyServiceTest
import dev.baseio.harmony.repository.*
import dev.baseio.harmony.viewmodel.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.runner.RunWith
import org.junit.runners.Suite

@ExperimentalCoroutinesApi
@RunWith(Suite::class)
@Suite.SuiteClasses(
    HarmonyServiceTest::class,
    MainRepositoryTest::class,
    AlbumRepositoryTest::class,
    ArtistRepositoryTest::class,
    FavoritesRepositoryTest::class,
    SearchRepositoryTest::class,
    AlbumDetailsViewModelTest::class,
    ArtistAlbumViewModelTest::class,
    ArtistDetailsViewModelTest::class,
    ArtistViewModelTest::class,
    FavoritesViewModelTest::class,
    GenreViewModelTest::class,
    MainViewModelTest::class,
    SearchViewModelTest::class,
)
class SuiteClass