package dev.baseio.harmony.ui.artist.details.info

import android.accounts.NetworkErrorException
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.baseio.harmony.core.data.ApiResult
import dev.baseio.harmony.data.ArtistDetailResponse
import dev.baseio.harmony.ui.artist.ArtistRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ArtistDetailsViewModel @Inject constructor(
    val repository: ArtistRepository
): ViewModel() {

    var result: LiveData<ApiResult<ArtistDetailResponse>> = MutableLiveData()
    var isNetworkError = MutableLiveData(false)


    fun fetchArtistDetails(artistID:String){
        viewModelScope.launch {
            try {
                result = repository.fetchArtistDetails(artistID)
                    .asLiveData(viewModelScope.coroutineContext+ Dispatchers.Default)
            }catch (e:NetworkErrorException){
                isNetworkError.value = true
                Timber.e(e)
            }
        }
    }
}
