package dev.baseio.harmony.ui.search

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.VisibleForTesting
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dev.baseio.harmony.core.extensions.UIExtensions
import dev.baseio.harmony.core.ui.DataBindingFragment
import dev.baseio.harmony.data.SearchData
import dagger.hilt.android.AndroidEntryPoint
import dev.baseio.harmony.R
import dev.baseio.harmony.databinding.FragmentSearchBinding

@AndroidEntryPoint
class SearchFragment: DataBindingFragment() {

    @VisibleForTesting
    val viewModel: SearchViewModel by viewModels()
    lateinit var binding: FragmentSearchBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = binding(inflater, R.layout.fragment_search, container)
        return binding.root
    }

    override fun getSafeArgs() = Unit

    override fun initBinding() {
        binding.apply {
            lifecycleOwner = this@SearchFragment
            recentAdapter = RecentSearchAdapter(object : RecentSearchAdapter.RecentSearchListener {
                override fun recentSearchListener(query: String) {
                    aetSearch.text = Editable.Factory.getInstance().newEditable(query)
                    viewModel.queryLiveData.value = query
                }
            })
            searchAdapter = SearchAlbumAdapter(::onClickSearch)
            vm = viewModel
        }

    }

    override fun setListeners() = Unit

    override fun observeLiveData() {
        viewModel.fetchingRecentSearch()
        viewModel.fetchSearch()

        viewModel.isNetworkError.observe(viewLifecycleOwner,{
            if(it){
                UIExtensions
                    .showSnackBar(
                        binding.clSearch,
                        this@SearchFragment.getString(R.string.network_error))
            }
        })
    }

    private fun onClickSearch(data: SearchData) {
//       SearchFragmentDirections.actionAlbumDetails(
//            data.id, data.title
//        ).let { actionAlbumDetails ->
//            findNavController().navigate(actionAlbumDetails)
//        }
    }
}
