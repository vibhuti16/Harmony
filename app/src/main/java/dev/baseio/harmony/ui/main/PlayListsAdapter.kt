package dev.baseio.harmony.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.NO_POSITION
import dev.baseio.harmony.data.Data
import dev.baseio.harmony.R
import dev.baseio.harmony.data.PlayList
import dev.baseio.harmony.data.Tracks
import dev.baseio.harmony.data.User
import dev.baseio.harmony.databinding.*

class PlayListsAdapter(
    val onItemClick: (PlayList) -> Unit)
    : RecyclerView.Adapter<PlayListsAdapter.ArtistsViewHolder>(){

    private val items:MutableList<PlayList> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtistsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding=DataBindingUtil
            .inflate<ItemPlaylistBinding>(inflater, R.layout.item_playlist,parent,false)

        return ArtistsViewHolder(binding).apply {
            binding.root.setOnClickListener {
                val position = adapterPosition.takeIf { p-> p != NO_POSITION }
                    ?: return@setOnClickListener

                onItemClick.invoke(items[position])
            }
        }
    }

    fun addMainList(artistsList:List<PlayList>){
        val previousSize = items.size
        items.addAll(artistsList)
        notifyItemRangeChanged(previousSize,items.size)
    }

    override fun onBindViewHolder(holder: ArtistsViewHolder, position: Int) {
        holder.binding.apply {
            playList = items[position]
            executePendingBindings()
        }
    }

    override fun getItemCount(): Int = items.size

    class ArtistsViewHolder(val binding:ItemPlaylistBinding):RecyclerView.ViewHolder(binding.root)
}
