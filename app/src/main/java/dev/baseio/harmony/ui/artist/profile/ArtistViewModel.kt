package dev.baseio.harmony.ui.artist.profile

import android.accounts.NetworkErrorException
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.baseio.harmony.core.data.ApiResult
import dev.baseio.harmony.data.ArtistData
import dev.baseio.harmony.ui.artist.ArtistRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ArtistViewModel @Inject constructor(
    private val repository: ArtistRepository
) : ViewModel(){

    var result: LiveData<ApiResult<List<ArtistData>>> = MutableLiveData()
    var isNetworkError = MutableLiveData(false)

    fun fetchResult(genreId:String){
        viewModelScope.launch {
            try {
                result = repository.fetchArtistList(genreId)
                    .asLiveData(viewModelScope.coroutineContext+ Dispatchers.Default)
            }catch (e:NetworkErrorException){
                isNetworkError.value = true
                Timber.e(e)
            }
        }
    }
}
