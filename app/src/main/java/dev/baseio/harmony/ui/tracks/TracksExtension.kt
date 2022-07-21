package dev.baseio.harmony.ui.tracks

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.media3.common.MediaItem
import androidx.media3.common.MimeTypes
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector
import androidx.recyclerview.widget.RecyclerView
import dev.baseio.harmony.core.data.ApiResult
import dev.baseio.harmony.data.AlbumData
import dev.baseio.harmony.data.Track
import dev.baseio.harmony.ui.album.AlbumDetailsAdapter
import timber.log.Timber

@BindingAdapter("bindingTracksList")
fun bindingTracksList(view: RecyclerView, results: LiveData<ApiResult<List<Track>>>) {
    when (results.value) {
        ApiResult.Loading, is ApiResult.Error -> {/* Nothing */ }
        is ApiResult.Success -> {
            Timber.d("result : succes isSplash : false")
            (view.adapter as TracksAdapter)
                .addAlbumTracks(
                    (results.value as ApiResult.Success<List<Track>>).data
                )
        }else ->{

    }
    }
}

private val playbackStateListener: Player.Listener = playbackStateListener()
private var player: ExoPlayer? = null

private var playWhenReady = true
private var currentItem = 0
private var playbackPosition = 0L

 fun initializePlayer(context : Context, track: String) {
    player = ExoPlayer.Builder(context)
        .build()
        .also { exoPlayer ->
//            viewBinding.videoView.player = exoPlayer

            val mediaItem = MediaItem.Builder()
                .setUri(track)
                .build()
            exoPlayer.setMediaItem(mediaItem)
            exoPlayer.playWhenReady = playWhenReady
            exoPlayer.seekTo(currentItem, playbackPosition)
            exoPlayer.addListener(playbackStateListener)
            exoPlayer.prepare()
        }
}

fun pausePlayer(){
    player?.let { exoPlayer ->
        exoPlayer.pause()
    }
}

private fun releasePlayer() {
    player?.let { exoPlayer ->
        playbackPosition = exoPlayer.currentPosition
        currentItem = exoPlayer.currentMediaItemIndex
        playWhenReady = exoPlayer.playWhenReady
        exoPlayer.removeListener(playbackStateListener)
        exoPlayer.release()

    }
    player = null
}


private fun playbackStateListener() = object : Player.Listener {
    override fun onPlaybackStateChanged(playbackState: Int) {
        val stateString: String = when (playbackState) {
            ExoPlayer.STATE_IDLE -> "ExoPlayer.STATE_IDLE      -"
            ExoPlayer.STATE_BUFFERING -> "ExoPlayer.STATE_BUFFERING -"
            ExoPlayer.STATE_READY -> "ExoPlayer.STATE_READY     -"
            ExoPlayer.STATE_ENDED -> "ExoPlayer.STATE_ENDED     -"
            else -> "UNKNOWN_STATE             -"
        }
    }
}