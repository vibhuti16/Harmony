package dev.baseio.harmony.ui.artist

import androidx.databinding.BindingAdapter
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import dev.baseio.harmony.core.data.ApiResult
import dev.baseio.harmony.data.ArtistData
import dev.baseio.harmony.ui.artist.profile.ArtistAdapter
import com.google.android.material.tabs.TabLayout

@BindingAdapter("adapterTablayout")
fun bindingTabLayoutAdapter(view: TabLayout, viewPager: ViewPager){
    view.setupWithViewPager(viewPager)
}

@BindingAdapter("adapterViewPager")
fun bindingViewPagerAdapter(view: ViewPager, adapter: FragmentPagerAdapter){
    view.adapter = adapter
}

@BindingAdapter("adapterArtistList")
fun bindingArtistList(view: RecyclerView, results: LiveData<ApiResult<List<ArtistData>>>) {
    when (results.value) {
        ApiResult.Loading, is ApiResult.Error -> {/* Nothing */ }
        is ApiResult.Success -> {
            (view.adapter as ArtistAdapter)
                .addArtistList(
                    (results.value as ApiResult.Success<List<ArtistData>>)
                        .data)
        }else ->{

        }
    }
}
