package dev.baseio.harmony.ui.favorites

import android.accounts.NetworkErrorException
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.baseio.harmony.core.data.ApiResult
import dev.baseio.harmony.entities.AlbumEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
        private val favoritesRepository: FavoritesRepository
): ViewModel() {

    var favorites: LiveData<ApiResult<List<AlbumEntity>>> = MutableLiveData()
    var isNetworkError = MutableLiveData(false)

    fun fetchFavorites(){
        viewModelScope.launch {
            try {
                favorites = favoritesRepository.fetchFavorites()
                    .asLiveData(viewModelScope.coroutineContext+ Dispatchers.Default)
            } catch (e:NetworkErrorException){
                isNetworkError.value = true
                Timber.e(e)
            }
        }
    }
}
