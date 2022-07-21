package dev.baseio.harmony.di

import dev.baseio.harmony.domain.local.HarmonyDao
import dev.baseio.harmony.domain.network.HarmonyClient
import dev.baseio.harmony.ui.album.AlbumRepository
import dev.baseio.harmony.ui.artist.ArtistRepository
import dev.baseio.harmony.ui.favorites.FavoritesRepository
import dev.baseio.harmony.ui.main.MainRepository
import dev.baseio.harmony.ui.search.SearchRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideMainRepository(
        harmonyClient: HarmonyClient,
        harmonyDao: HarmonyDao,
        @IODispatcher dispatcher: CoroutineDispatcher
    ) = MainRepository(harmonyClient, harmonyDao, dispatcher)

    @Provides
    @Singleton
    fun provideFavoriteRepository(
        harmonyDao: HarmonyDao,
        @IODispatcher dispatcher: CoroutineDispatcher
    ) = FavoritesRepository(harmonyDao, dispatcher)

    @Provides
    @Singleton
    fun providesSearchRepository(
        harmonyClient: HarmonyClient,
        harmonyDao: HarmonyDao,
        @IODispatcher dispatcher: CoroutineDispatcher
    ) = SearchRepository(harmonyClient, harmonyDao, dispatcher)

    @Provides
    @Singleton
    fun providesArtistRepository(
        harmonyClient: HarmonyClient,
        @IODispatcher dispatcher: CoroutineDispatcher
    ) = ArtistRepository(harmonyClient, dispatcher)


    @Provides
    @Singleton
    fun providesAlbumRepository(
        harmonyClient: HarmonyClient,
        harmonyDao: HarmonyDao,
        @IODispatcher dispatcher: CoroutineDispatcher
    ) = AlbumRepository(harmonyClient,harmonyDao, dispatcher)

}
