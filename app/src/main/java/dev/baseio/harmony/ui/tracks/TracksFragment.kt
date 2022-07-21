package dev.baseio.harmony.ui.tracks

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.VisibleForTesting
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import dev.baseio.harmony.R
import dev.baseio.harmony.core.Env
import dev.baseio.harmony.core.data.ApiResult
import dev.baseio.harmony.core.extensions.UIExtensions
import dev.baseio.harmony.core.ui.DataBindingFragment
import dev.baseio.harmony.data.AlbumData
import dev.baseio.harmony.data.Track
import dev.baseio.harmony.databinding.FragmentAlbumDetailsBinding
import dev.baseio.harmony.databinding.FragmentTracksBinding
import dev.baseio.harmony.ui.album.AlbumDetailsAdapter
import dev.baseio.harmony.ui.album.AlbumDetailsFragmentArgs
import dev.baseio.harmony.ui.album.AlbumDetailsViewModel
import dev.baseio.harmony.ui.main.MainActivity
import dev.baseio.harmony.ui.main.playMediaPlayer
import timber.log.Timber

@AndroidEntryPoint
class TracksFragment: DataBindingFragment() {

    lateinit var binding: FragmentTracksBinding
    @VisibleForTesting
    val viewModel: TracksViewModel by viewModels()
    private var id = "0" //
    private var imgUrl = ""
    private val args: TracksFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = binding(inflater, R.layout.fragment_tracks,container)
        return binding.root
    }
    override fun getSafeArgs() {
        id = args.id
        imgUrl = args.imgUrl
    }

    override fun initBinding() {
        binding.apply {
            Picasso.get().load(imgUrl).into(binding.imgArtist)
            lifecycleOwner = this@TracksFragment
            adapter = TracksAdapter(object : TracksAdapter.OnClick {
                override fun onItemClickListener(v: View, pos:Int, data: Track) {
                    context?.let { data.preview?.let { it1 -> initializePlayer(it, it1) } }
                    binding.play.visibility = View.VISIBLE
                    binding.trackPlayed.text = data.title
//                    when (v.id) {
//                        R.id.ibn_fav -> {
//                            val item = data as AlbumData
//                            viewModel.favoritedToTrack(item)
//                        }
//                        R.id.ibn_share -> {
//                            val item = data as AlbumData
//                            (this@AlbumDetailsFragment.requireActivity() as MainActivity).apply {
//                                val sharingIntent = Intent(Intent.ACTION_SEND)
//                                    .putExtra(Intent.EXTRA_SUBJECT, Env.APP_NAME)
//                                    .putExtra(Intent.EXTRA_TEXT, item.link)
//                                startActivity(Intent.createChooser(sharingIntent, "Share via"))
//                            }
//                        }
//                        R.id.cardView -> {
//                            val list = data as List<AlbumData>
//                            ((this@AlbumDetailsFragment).requireActivity() as MainActivity).viewModel.apply {
//                                albumData.value = list
//                                positionTrack = pos
//                                isGoneMediaPlayer.set(true)
//                                playMediaPlayer()
//                            }
//
//                        }
//                    }
                }
            })
            vm = viewModel
        }
    }

    override fun setListeners() {
        binding.play.setOnClickListener {
            pausePlayer()
            binding.play.background = ResourcesCompat.getDrawable(resources,android.R.drawable.ic_media_play,null)
        }
    }

    override fun observeLiveData() {
        viewModel.fetchingTracksByArtist(id)
        viewModel.result.observe(viewLifecycleOwner, {
            when(it){
                //TODO  progress dialog add.
                ApiResult.Loading->{ }
                is ApiResult.Error->{
//                    UIExtensions.showSnackBar(
//                        binding.lvAlbumDetails,
//                        this.getString(
//                            R.string.unexpected_error
//                        ))
                    Timber.d("result : error isSplash : false")
                }
                is ApiResult.Success->{
                    Timber.d("result : ${viewModel.result.value}")
                }
            }
        })
        viewModel.isNetworkError.observe(viewLifecycleOwner,{
            if(it){
//                UIExtensions.showSnackBar(
//                    binding.lvAlbumDetails,
//                    this.getString(R.string.network_error))
            }
        })
    }
}