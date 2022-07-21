package dev.baseio.harmony.ui.main

import android.accounts.NetworkErrorException
import android.app.Application
import androidx.annotation.VisibleForTesting
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.*
import androidx.media3.common.util.Util.getUserAgent
import androidx.media3.datasource.DefaultDataSourceFactory
import androidx.media3.exoplayer.ExoPlayer
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.baseio.harmony.core.data.ApiResult
import dev.baseio.harmony.data.AlbumData
import dev.baseio.harmony.data.Data
import dev.baseio.harmony.data.MainResponse
import dev.baseio.harmony.data.MediaPlayerState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

private const val APPLICATION_NAME = "Harmony"

@HiltViewModel
class MainViewModel @Inject constructor(
    app: Application,
    private val mainRepository: MainRepository
): ViewModel(){

    @VisibleForTesting var genreListLiveData: LiveData<ApiResult<List<Data>?>> = MutableLiveData()

    var isSplash: MutableLiveData<Boolean> = MutableLiveData()
    var isGoneMediaPlayer :ObservableBoolean = ObservableBoolean(false)
    var albumData:MutableLiveData<List<AlbumData>> = MutableLiveData()
    var positionTrack = 0
    val isNetworkError = MutableLiveData(false)

    val mediaPlayer = ExoPlayer.Builder(app.applicationContext).build()
    internal val dataSourceFactory =
        DefaultDataSourceFactory(
            app.applicationContext,
            getUserAgent(app.applicationContext, APPLICATION_NAME))

    var mediaPlayerState:MutableLiveData<MediaPlayerState> = MutableLiveData()

    init {
        isSplash.value = true
        initMediaPlayer()
    }

    fun fetchGenreList(){
        viewModelScope.launch {
            try {
                genreListLiveData = mainRepository.fetchGenreList()
                    .asLiveData(viewModelScope.coroutineContext+Dispatchers.Default)
            }catch (e:NetworkErrorException){
                isNetworkError.value = true
                Timber.e(e)
            }

        }
    }


}
