package dev.baseio.harmony.ui.artist.profile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import dev.baseio.harmony.data.ArtistData
import dev.baseio.harmony.R
import dev.baseio.harmony.databinding.ItemArtistBinding
import timber.log.Timber


class ArtistAdapter(
    val onClickItem: (ArtistData) -> Unit
) : RecyclerView.Adapter<ArtistAdapter.ArtistViewHolder>(){

    private val items:MutableList<ArtistData> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtistViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding= DataBindingUtil
            .inflate<ItemArtistBinding>(inflater, R.layout.item_artist,parent,false)

        return ArtistViewHolder(binding).apply {
            binding.root.setOnClickListener {
                val position = adapterPosition.takeIf { p-> p != RecyclerView.NO_POSITION }
                    ?: return@setOnClickListener
                onClickItem(items[position])
            }
        }
    }

    fun addArtistList(artistList:List<ArtistData>){
        val previousSize = items.size
        items.addAll(artistList)
        notifyItemRangeChanged(previousSize,items.size)
    }

    override fun onBindViewHolder(holder: ArtistViewHolder, position: Int) {
        holder.binding.apply {
            Timber.d("binding..")
            artist = items[position]
            executePendingBindings()
        }
    }

    override fun getItemCount(): Int = items.size

    class ArtistViewHolder(val binding: ItemArtistBinding): RecyclerView.ViewHolder(binding.root)
}
