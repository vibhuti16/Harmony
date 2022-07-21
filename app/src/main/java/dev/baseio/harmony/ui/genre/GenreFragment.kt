package dev.baseio.harmony.ui.genre

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.VisibleForTesting
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dev.baseio.harmony.core.data.ApiResult
import dev.baseio.harmony.core.extensions.UIExtensions
import dev.baseio.harmony.core.ui.DataBindingFragment
import dev.baseio.harmony.data.Data
import dagger.hilt.android.AndroidEntryPoint
import dev.baseio.harmony.R
import dev.baseio.harmony.data.PlayList
import dev.baseio.harmony.data.User
import dev.baseio.harmony.databinding.FragmentGenreBinding
import dev.baseio.harmony.ui.main.ArtistsAdapter
import dev.baseio.harmony.ui.main.PlayListsAdapter
import dev.baseio.harmony.ui.main.PodcastsAdapter
import timber.log.Timber

@AndroidEntryPoint
class GenreFragment : DataBindingFragment() {

    lateinit var  binding: FragmentGenreBinding
    @VisibleForTesting
    val viewModel : GenreViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = binding(inflater, R.layout.fragment_genre,container)
        return binding.root
    }

    override fun getSafeArgs() = Unit

    override fun initBinding() {
        binding.apply {
            lifecycleOwner = this@GenreFragment
            adapter = GenreAdapter(::onGenreItemClick)
            artistsAdapter = ArtistsAdapter(::onArtistsItemClick)
            playlistAdapter = PlayListsAdapter(::onPlayListsItemClick)
            podcastAdapter = PodcastsAdapter(::onPodcastsItemClick)
            vm = viewModel
        }
    }

    override fun setListeners() = Unit

    override fun observeLiveData() {
        viewModel.fetchResult()
        viewModel.fetchMainList()
        viewModel.mainListLiveData.observe(viewLifecycleOwner,{
            when(it){
                ApiResult.Loading->{ }
                is ApiResult.Error->{
                    UIExtensions
                        .showSnackBar(
                            binding.clGenre,
                            this@GenreFragment.getString(R.string.unexpected_error
                            ))
                    Timber.d("mainList : error isSplash : false")
                }
                is ApiResult.Success->{
                    Timber.d("mainList : succes isSplash : false")
                }
            }

        })

        viewModel.isNetworkError.observe(viewLifecycleOwner,{
            if(it){
                UIExtensions.showSnackBar(
                    binding.clGenre,
                    this@GenreFragment.getString(R.string.network_error
                    ))
            }
        })
    }

    private fun onGenreItemClick(genreData: Data) {
    GenreFragmentDirections.actionGenreList(
         genreData.id,
         genreData.name.orEmpty()
     ).let { actionGenreList ->
         findNavController().navigate(actionGenreList)
     }
    }

    private fun onMainItemClick(data : PlayList) {
//        GenreFragmentDirections.actionGenreList(
//            genreData.id,
//            genreData.name.orEmpty()
//        ).let { actionGenreList ->
//            findNavController().navigate(actionGenreList)
//        }
    }

    private fun onArtistsItemClick(data : User) {
//        GenreFragmentDirections.actionGenreList(
//            genreData.id,
//            genreData.name.orEmpty()
//        ).let { actionGenreList ->
//            findNavController().navigate(actionGenreList)
//        }
    }

    private fun onPlayListsItemClick(data : PlayList) {
//        GenreFragmentDirections.actionGenreList(
//            genreData.id,
//            genreData.name.orEmpty()
//        ).let { actionGenreList ->
//            findNavController().navigate(actionGenreList)
//        }
    }

    private fun onPodcastsItemClick(data : PlayList) {
//        GenreFragmentDirections.actionGenreList(
//            genreData.id,
//            genreData.name.orEmpty()
//        ).let { actionGenreList ->
//            findNavController().navigate(actionGenreList)
//        }
    }
}

