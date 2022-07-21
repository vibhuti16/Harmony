package dev.baseio.harmony.ui.album

import android.accounts.NetworkErrorException
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.baseio.harmony.core.data.ApiResult
import dev.baseio.harmony.core.mapper.mapper
import dev.baseio.harmony.data.AlbumData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AlbumDetailsViewModel @Inject constructor(
    private val repository: AlbumRepository
): ViewModel(){

    var result: LiveData<ApiResult<List<AlbumData>>> = MutableLiveData()
    var isNetworkError = MutableLiveData(false)


    fun favoritedToTrack(data: AlbumData) {
        viewModelScope.launch {
            repository.insertFavoritesData(track = data.mapper())
        }
    }

    fun fetchingAlbumDatas(albumID:String){
        viewModelScope.launch {
            try{
                result = repository.fetchAlbumTracks(albumID)
                    .asLiveData(viewModelScope.coroutineContext+ Dispatchers.Default)
            }catch (e:NetworkErrorException){
                isNetworkError.value = true
                Timber.e(e)
            }
        }
    }


}
