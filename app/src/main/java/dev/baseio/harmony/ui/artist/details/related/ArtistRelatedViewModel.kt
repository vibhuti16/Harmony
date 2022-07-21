package dev.baseio.harmony.ui.artist.details.related

import android.accounts.NetworkErrorException
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.baseio.harmony.core.data.ApiResult
import dev.baseio.harmony.data.ArtistRelatedData
import dev.baseio.harmony.ui.artist.ArtistRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ArtistRelatedViewModel @Inject constructor(
   private val repository: ArtistRepository
) : ViewModel() {

   var  result : LiveData<ApiResult<List<ArtistRelatedData>>> = MutableLiveData()
   var isNetworkError = MutableLiveData(false)

   fun fetchArtistRelated(artistID:String){
      viewModelScope.launch {
         try {
            result = repository.fetchArtistRelated(artistID)
               .asLiveData(viewModelScope.coroutineContext+ Dispatchers.Default)
         }catch (e:NetworkErrorException){
            isNetworkError.value = true
            Timber.e(e)
         }
      }
   }
}
