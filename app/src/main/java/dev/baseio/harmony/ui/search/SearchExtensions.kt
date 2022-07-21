package dev.baseio.harmony.ui.search

import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import dev.baseio.harmony.core.data.ApiResult
import dev.baseio.harmony.data.SearchData
import dev.baseio.harmony.entities.SearchEntity
import timber.log.Timber

internal fun SearchViewModel.initSearchActionListener() {
    editorActionListener = TextView.OnEditorActionListener { v, actionId, _ ->
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            queryLiveData.value = v.text.toString()
            true
        } else false
    }
}

@BindingAdapter("adapterRecentSearch")
fun bindingRecentSeach(view: RecyclerView, results: LiveData<ApiResult<List<SearchEntity>>>) {
    Timber.d("binding recentData : ${results.value}")
    when(results.value){
        ApiResult.Loading, is ApiResult.Error-> {/* Nothing */ }
        is ApiResult.Success -> {
            (view.adapter as RecentSearchAdapter)
                .addRecentSearch(
                    (((results.value as ApiResult.Success<List<SearchEntity>>).data)
                            as List<SearchEntity>))
        }else ->{

        }
    }
}

@BindingAdapter("adapterSearchAlbum")
fun bindingSearchAlbum(view: RecyclerView, results: LiveData<ApiResult<Any>>) {
    when (results.value) {
        ApiResult.Loading, is ApiResult.Error -> {/* Nothing */ }
        is ApiResult.Success -> {
            Timber.d("adapterSearchAlbum")
            (view.adapter as SearchAlbumAdapter)
                .addAlbumSearch(
                    ((results.value) as ApiResult.Success<List<SearchData>>).data)
        }else ->{

        }
    }
}

@BindingAdapter("onEditorActionListener")
fun bindOnEditorActionListener(editText: EditText, editorActionListener: TextView.OnEditorActionListener) {
    editText.setOnEditorActionListener(editorActionListener)
}

