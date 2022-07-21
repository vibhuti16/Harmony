package dev.baseio.harmony.ui.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.VisibleForTesting
import androidx.fragment.app.viewModels
import dev.baseio.harmony.core.ui.DataBindingFragment
import dev.baseio.harmony.entities.AlbumEntity
import dev.baseio.harmony.ui.main.MainActivity
import dev.baseio.harmony.ui.main.playMediaPlayer
import dagger.hilt.android.AndroidEntryPoint
import dev.baseio.harmony.R
import dev.baseio.harmony.databinding.FragmentFavoritesBinding

@AndroidEntryPoint
class FavoritesFragment: DataBindingFragment() {

    lateinit var binding: FragmentFavoritesBinding
    @VisibleForTesting
    val viewModel: FavoritesViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = binding(inflater, R.layout.fragment_favorites,container)
        return binding.root
    }

    override fun getSafeArgs() = Unit

    override fun initBinding() {
        binding.apply {
            lifecycleOwner = this@FavoritesFragment
            vm = viewModel
            adapter = FavoritesAdapter(object : FavoritesAdapter.OnClick {
                override fun onItemClickListener(v: View,trackPos:Int, albumList:List<AlbumEntity>) {
                    ((this@FavoritesFragment).requireActivity() as MainActivity)
                        .viewModel.apply {
                            positionTrack = trackPos
                            isGoneMediaPlayer.set(true)
                            playMediaPlayer()
                        }
                }
            })
        }
    }

    override fun setListeners() = Unit

    override fun observeLiveData() {
        viewModel.fetchFavorites()
    }
}
