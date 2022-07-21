package dev.baseio.harmony.ui.genre

import android.accounts.NetworkErrorException
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.baseio.harmony.core.data.ApiResult
import dev.baseio.harmony.data.Data
import dev.baseio.harmony.data.MainResponse
import dev.baseio.harmony.ui.main.MainRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class GenreViewModel @Inject constructor(
    private val mainRepository: MainRepository
): ViewModel() {

    var result: LiveData<ApiResult<List<Data>?>> = MutableLiveData()
    var mainListLiveData: LiveData<ApiResult<MainResponse?>> = MutableLiveData()
    var isNetworkError = MutableLiveData(false)

    fun fetchResult(){
        viewModelScope.launch {
            try {
                result = mainRepository.fetchGenreList()
                    .asLiveData(viewModelScope.coroutineContext + Dispatchers.Default)
            }catch (e:NetworkErrorException){
                isNetworkError.value = true
                Timber.e(e)
            }
        }
    }

    fun fetchMainList(){
        viewModelScope.launch {
            try {
                mainListLiveData = mainRepository.fetchMainList()
                    .asLiveData(viewModelScope.coroutineContext+Dispatchers.Default)
            }catch (e:NetworkErrorException){
                isNetworkError.value = true
                Timber.e(e)
            }

        }
    }
}
