package dev.baseio.harmony.ui.main
import android.os.Bundle
import androidx.activity.viewModels
import androidx.annotation.VisibleForTesting
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import dev.baseio.harmony.core.data.ApiResult
import dev.baseio.harmony.core.extensions.UIExtensions
import dev.baseio.harmony.core.ui.DataBindingActivity
import dev.baseio.harmony.data.MediaPlayerState
import dagger.hilt.android.AndroidEntryPoint
import dev.baseio.harmony.R
import dev.baseio.harmony.databinding.ActivityMainBinding

@AndroidEntryPoint
class MainActivity : DataBindingActivity() {

    @VisibleForTesting val viewModel : MainViewModel by viewModels()
    val binding: ActivityMainBinding by binding(R.layout.activity_main)
    internal lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.apply {
            lifecycleOwner = this@MainActivity
            vm = viewModel
            navController = (supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
                    as NavHostFragment).navController
        }
        viewModel.isSplash.postValue(false)
//        observeLiveData()
        bottomNavigationListener()
        navControllerListener()
        setListeners()
    }


    private fun setListeners() {
        binding.ibtnPlayPlayer.setOnClickListener {
            when(viewModel.mediaPlayerState.value) {
                MediaPlayerState.PLAYING->viewModel.playMediaPlayer()
                MediaPlayerState.PAUSED->viewModel.resumeMediaPlayer()
                else->viewModel.stopMediaPlayer()
            }
        }

        binding.ibtnTrackForward.setOnClickListener {
            viewModel.forwardTrack()
        }

        binding.ibtnTrackPrevious.setOnClickListener {
            viewModel.previouslyTrack()
        }
    }

    private fun handleDeepLink() {
        navController.handleDeepLink(intent)
    }

    override fun onBackPressed() {
        if(viewModel.isGoneMediaPlayer.get()){
            viewModel.hideMediaPlayer()
        }
        else{
            super.onBackPressed()
        }
    }
}
