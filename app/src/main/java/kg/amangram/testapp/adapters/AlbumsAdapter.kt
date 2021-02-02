package kg.amangram.testapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kg.amangram.testapp.data.model.AlbumItem
import kg.amangram.testapp.databinding.ItemAlbumBinding

class AlbumsAdapter(private val interaction: (AlbumItem) -> Unit,private val onDelete: (AlbumItem)-> Unit) :
    ListAdapter<AlbumItem, AlbumsAdapter.AlbumsVH>(AlbumsItemDC()) {

    private var isDatabase = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumsVH {
        val inflater = LayoutInflater.from(parent.context)
        return AlbumsVH(ItemAlbumBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: AlbumsVH, position: Int) {
        holder.bind(getItem(position))
    }

    fun setIsDatabase(isDatabase: Boolean){
        this.isDatabase = isDatabase
    }
    fun swapData(data: List<AlbumItem>) {
        submitList(data.toMutableList())
    }

    inner class AlbumsVH(val itemBinding: ItemAlbumBinding) : RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(item: AlbumItem) {
            itemBinding.tvTitle.text = item.title
            if (isDatabase){
                itemBinding.ibDelete.visibility = View.VISIBLE
            }else{
                itemBinding.ibDelete.visibility = View.GONE
            }
            itemBinding.root.setOnClickListener {
                interaction(item)
            }
            itemBinding.ibDelete.setOnClickListener {
                onDelete(item)
            }
        }
    }

    private class AlbumsItemDC : DiffUtil.ItemCallback<AlbumItem>() {
        override fun areItemsTheSame(
            oldItem: AlbumItem,
            newItem: AlbumItem
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: AlbumItem,
            newItem: AlbumItem
        ): Boolean {
            return oldItem == newItem
        }
    }
}