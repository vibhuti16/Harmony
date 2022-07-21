package dev.baseio.harmony.ui.main

import android.view.View
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.datasource.cache.CacheDataSource
import androidx.media3.exoplayer.ExoPlaybackException
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.extractor.DefaultExtractorsFactory
import androidx.media3.extractor.ExtractorsFactory
import androidx.recyclerview.widget.RecyclerView
import dev.baseio.harmony.core.Env
import dev.baseio.harmony.R
import dev.baseio.harmony.core.data.ApiResult
import dev.baseio.harmony.data.*
import dev.baseio.harmony.ui.genre.GenreAdapter
import kotlinx.coroutines.launch
import timber.log.Timber

internal fun MainActivity.navControllerListener() {
    navController.addOnDestinationChangedListener { _, destination, arguments ->
        /**
         * if mediaPlayer or volumeController visible, hidden views.
         */
        if(viewModel.isGoneMediaPlayer.get()){
            viewModel.hideMediaPlayer()
        }

        var name = getString(R.string.app_name)
        arguments?.let {
            name = it[Env.BUND_NAME].toString()
        }

//        when(destination.id){
//            R.id.genre->{ binding.materialToolbar.title = getString(R.string.app_name) }
////            R.id.search->{ binding.materialToolbar.title = getString(R.string.search) }
//            R.id.favorites->{ binding.materialToolbar.title = getString(R.string.favorites) }
//            R.id.genre_list,
//            R.id.artist_details,
//            R.id.album_details,->{ binding.materialToolbar.title = name }
//        }
    }
}

internal fun MainActivity.bottomNavigationListener() {
    /* BottomNavigationBar Click Listener */
    binding.bottomNavigation.setOnNavigationItemReselectedListener {
        when(it.itemId){
            R.id.itemMusic->{ navController.navigate(R.id.action_genre) }
//            R.id.itemSearch->{ navController.navigate(R.id.action_search) }
            R.id.itemFavorites->{  navController.navigate(R.id.favorites) }
        }
    }
}

internal fun MainViewModel.initMediaPlayer() {
    mediaPlayerState.value = MediaPlayerState.BUFFERING
    mediaPlayer.addListener(object : Player.Listener {
        override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
            val state = when {
                playbackState == ExoPlayer.STATE_READY && mediaPlayer.playWhenReady -> MediaPlayerState.PLAYING
                playbackState == ExoPlayer.STATE_READY && mediaPlayer.playWhenReady.not() -> MediaPlayerState.PAUSED
                playbackState == ExoPlayer.STATE_BUFFERING -> MediaPlayerState.BUFFERING
                else -> MediaPlayerState.ERROR
            }

            mediaPlayerState.value = state
        }

        override fun onPlayerError(error: PlaybackException) {
            super.onPlayerError(error)
            mediaPlayerState.value = MediaPlayerState.ERROR
        }

    })
}

fun MainViewModel.resumeMediaPlayer() {
    mediaPlayer.playWhenReady = true
}

fun MainViewModel.stopMediaPlayer() {
    mediaPlayer.playWhenReady = false
}

fun MainViewModel.forwardTrack() {
    if (!albumData.value.isNullOrEmpty() && albumData.value!!.size - 1 > positionTrack)
        ++positionTrack
    playMediaPlayer()
}

fun MainViewModel.previouslyTrack() {
    if (positionTrack > 0) positionTrack--
    playMediaPlayer()
}

internal fun MainViewModel.hideMediaPlayer() {
    isGoneMediaPlayer.set(false)
    stopMediaPlayer()
}


internal fun MainViewModel.playMediaPlayer(){
    viewModelScope.launch {
        val musicUri =albumData.value?.get(0)?.preview
//        val extractorMediaSource = DefaultExtractorsFactory(dataSourceFactory)
//            .setExtractorsFactory(DefaultExtractorsFactory())
//            .createMediaSource(MediaItem.fromUri(musicUri.toString()))
//        mediaPlayer.prepare(extractorMediaSource)
//        mediaPlayer.playWhenReady = true
        val mediaItem = musicUri?.let { MediaItem.fromUri(it) }
        if (mediaItem != null) {
            mediaPlayer.setMediaItem(mediaItem)
            mediaPlayer.playWhenReady = true
            mediaPlayer.prepare()
        }
    }
}



@BindingAdapter("adapterArtistsList")
fun adapterArtistsList(view: RecyclerView, results: LiveData<ApiResult<MainResponse>>) {
    when (results.value) {
        ApiResult.Loading, is ApiResult.Error -> {/* Nothing */ }
        is ApiResult.Success -> {
            Timber.d("result : succes isSplash : false")
            (view.adapter as ArtistsAdapter).addMainList((((results.value as ApiResult.Success<MainResponse>).data.artists?.data)
                    as List<User>))
        }else ->{

    }
    }
}

@BindingAdapter("adapterPlayList")
fun adapterPlayList(view: RecyclerView, results: LiveData<ApiResult<MainResponse>>) {
    when (results.value) {
        ApiResult.Loading, is ApiResult.Error -> {/* Nothing */ }
        is ApiResult.Success -> {
            Timber.d("result : succes isSplash : false")
            (view.adapter as PlayListsAdapter).addMainList((((results.value as ApiResult.Success<MainResponse>).data.playlists?.data)
                    as List<PlayList>))
        }else ->{

    }
    }
}

@BindingAdapter("adapterPodcastsList")
fun adapterPodcastsList(view: RecyclerView, results: LiveData<ApiResult<MainResponse>>) {
    when (results.value) {
        ApiResult.Loading, is ApiResult.Error -> {/* Nothing */ }
        is ApiResult.Success -> {
            Timber.d("result : succes isSplash : false")
            (view.adapter as PodcastsAdapter).addMainList((((results.value as ApiResult.Success<MainResponse>).data.podcasts?.data)
                    as List<PlayList>))
        }else ->{

    }
    }
}

@BindingAdapter("isTrackVisible")
fun isTrackVisible(view: View, results: LiveData<ApiResult<MainResponse>>) {
    when (results.value) {
        ApiResult.Loading, is ApiResult.Error -> {  view.isVisible =false}
        is ApiResult.Success -> {
            if((((results.value as ApiResult.Success<MainResponse>).data.tracks?.data)
                    as List<PlayList>).size>0){
                view.isVisible = true
            }else{
                view.isVisible =false
            }
        }else ->{

    }
    }
}

@BindingAdapter("isArtistsVisible")
fun isArtistsVisible(view: View, results: LiveData<ApiResult<MainResponse>>) {
    when (results.value) {
        ApiResult.Loading, is ApiResult.Error -> {  view.isVisible =false}
        is ApiResult.Success -> {
            if((((results.value as ApiResult.Success<MainResponse>).data.artists?.data)
                        as List<User>).size>0){
                view.isVisible = true
            }else{
                view.isVisible =false
            }
        }else ->{

    }
    }
}
