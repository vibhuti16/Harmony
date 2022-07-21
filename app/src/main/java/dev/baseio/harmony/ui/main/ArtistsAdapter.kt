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
import dev.baseio.harmony.databinding.ItemArtistBinding
import dev.baseio.harmony.databinding.ItemArtistsBinding
import dev.baseio.harmony.databinding.ItemGenreBinding
import dev.baseio.harmony.databinding.ItemTracksBinding

class ArtistsAdapter(
    val onItemClick: (User) -> Unit)
    : RecyclerView.Adapter<ArtistsAdapter.ArtistsViewHolder>(){

    private val items:MutableList<User> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtistsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding=DataBindingUtil
            .inflate<ItemArtistsBinding>(inflater, R.layout.item_artists,parent,false)

        return ArtistsViewHolder(binding).apply {
            binding.root.setOnClickListener {
                val position = adapterPosition.takeIf { p-> p != NO_POSITION }
                    ?: return@setOnClickListener

                onItemClick.invoke(items[position])
            }
        }
    }

    fun addMainList(artistsList:List<User>){
        val previousSize = items.size
        items.addAll(artistsList)
        notifyItemRangeChanged(previousSize,items.size)
    }

    override fun onBindViewHolder(holder: ArtistsViewHolder, position: Int) {
        holder.binding.apply {
            artist = items[position]
            executePendingBindings()
        }
    }

    override fun getItemCount(): Int = items.size

    class ArtistsViewHolder(val binding:ItemArtistsBinding):RecyclerView.ViewHolder(binding.root)
}
