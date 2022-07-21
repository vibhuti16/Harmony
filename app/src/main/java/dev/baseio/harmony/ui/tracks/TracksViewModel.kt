package dev.baseio.harmony.ui.tracks

import android.accounts.NetworkErrorException
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.baseio.harmony.core.data.ApiResult
import dev.baseio.harmony.data.AlbumData
import dev.baseio.harmony.data.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class TracksViewModel @Inject constructor(private val repository : TracksRepository): ViewModel() {

    var result: LiveData<ApiResult<List<Track>>> = MutableLiveData()
    var isNetworkError = MutableLiveData(false)

    fun fetchingTracksByArtist(artistId:String){
        viewModelScope.launch {
            try{
                result = repository.fetchTracksByArtist(artistId)
                    .asLiveData(viewModelScope.coroutineContext+ Dispatchers.Default)
            }catch (e: NetworkErrorException){
                isNetworkError.value = true
                Timber.e(e)
            }
        }
    }

    fun fetchingTracksByAlbum(albumID:String){
        viewModelScope.launch {
            try{
                result = repository.fetchTracksByAlbum(albumID)
                    .asLiveData(viewModelScope.coroutineContext+ Dispatchers.Default)
            }catch (e: NetworkErrorException){
                isNetworkError.value = true
                Timber.e(e)
            }
        }
    }
}