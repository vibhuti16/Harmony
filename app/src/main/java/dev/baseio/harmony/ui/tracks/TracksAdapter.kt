package dev.baseio.harmony.ui.tracks

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import dev.baseio.harmony.R
import dev.baseio.harmony.core.ui.DataBindingFragment
import dev.baseio.harmony.data.AlbumData
import dev.baseio.harmony.data.Track
import dev.baseio.harmony.databinding.ItemAlbumBinding
import dev.baseio.harmony.databinding.ItemTracksBinding
import dev.baseio.harmony.ui.album.AlbumDetailsAdapter

class TracksAdapter (val listener: OnClick): RecyclerView.Adapter<TracksAdapter.TracksViewHolder>() {

    interface OnClick{
        fun onItemClickListener(v: View, pos:Int, item:Track)
    }

    private val items: MutableList<Track> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TracksViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil
            .inflate<ItemTracksBinding>(inflater, R.layout.item_tracks, parent, false)

        return TracksViewHolder(binding).apply {
            binding.mainTrack.setOnClickListener {
                val position = adapterPosition
                listener.onItemClickListener(it,position,items.get(position))
            }
//            binding.ibnFav.setOnClickListener {
//                val position = adapterPosition.takeIf { p -> p != RecyclerView.NO_POSITION }
//                val item = items[position!!]
//                listener.onItemClickListener(it,position,item)
//                Toast.makeText(binding.root.context,"Add to Favorites!", Toast.LENGTH_LONG).show()}
//            binding.ibnShare.setOnClickListener {
//                val position = adapterPosition.takeIf { p -> p != RecyclerView.NO_POSITION }
//                val item = items[position!!]
//                listener.onItemClickListener(it,position,item)
//                Toast.makeText(binding.root.context,"Sharing Tracks... Waiting.", Toast.LENGTH_LONG).show()}
        }
    }

    fun addAlbumTracks(albums: List<Track>) {
        val previousSize = items.size
        items.addAll(albums)
        notifyItemRangeChanged(previousSize, items.size)
    }

    override fun getItemCount(): Int = items.size

    class TracksViewHolder(val binding: ItemTracksBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(
        holder: TracksViewHolder,
        position: Int
    ) {
        holder.binding.apply {
            track = items[position]
            executePendingBindings()
        }
    }
}
