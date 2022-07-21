//package dev.baseio.harmony.ui.main
//
//import android.view.LayoutInflater
//import android.view.ViewGroup
//import androidx.databinding.DataBindingUtil
//import androidx.recyclerview.widget.RecyclerView
//import androidx.recyclerview.widget.RecyclerView.NO_POSITION
//import dev.baseio.harmony.data.Data
//import dev.baseio.harmony.R
//import dev.baseio.harmony.data.PlayList
//import dev.baseio.harmony.data.Tracks
//import dev.baseio.harmony.databinding.ItemGenreBinding
//import dev.baseio.harmony.databinding.ItemTracksBinding
//
//class MainAdapter(
//    val onGenreItemClick: (PlayList) -> Unit)
//    : RecyclerView.Adapter<MainAdapter.GenreViewHolder>(){
//
//    private val items:MutableList<PlayList> = mutableListOf()
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenreViewHolder {
//        val inflater = LayoutInflater.from(parent.context)
//        val binding=DataBindingUtil
//            .inflate<ItemTracksBinding>(inflater, R.layout.item_genre,parent,false)
//
//        return GenreViewHolder(binding).apply {
//            binding.root.setOnClickListener {
//                val position = adapterPosition.takeIf { p-> p != NO_POSITION }
//                    ?: return@setOnClickListener
//
//                onGenreItemClick.invoke(items[position])
//            }
//        }
//    }
//
//    fun addMainList(genreList:List<PlayList>){
//        val previousSize = items.size
//        items.addAll(genreList)
//        notifyItemRangeChanged(previousSize,items.size)
//    }
//
//    override fun onBindViewHolder(holder: GenreViewHolder, position: Int) {
//        holder.binding.apply {
//            tracks = items[position]
//            executePendingBindings()
//        }
//    }
//
//    override fun getItemCount(): Int = items.size
//
//    class GenreViewHolder(val binding:ItemTracksBinding):RecyclerView.ViewHolder(binding.root)
//}
