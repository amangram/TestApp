package kg.amangram.testapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kg.amangram.testapp.R
import kg.amangram.testapp.data.model.PhotoItem
import kg.amangram.testapp.databinding.ItemPhotoBinding

class PhotoAdapter(private val interaction: (PhotoItem) -> Unit) :
    ListAdapter<PhotoItem, PhotoAdapter.PhotoVH>(PhotoItemDC()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoVH {
        val inflater = LayoutInflater.from(parent.context)
        return PhotoVH(
            ItemPhotoBinding.inflate(inflater, parent, false)
        )
    }

    override fun onBindViewHolder(holder: PhotoVH, position: Int) {
        holder.bind(getItem(position))
    }

    fun swapData(data: List<PhotoItem>) {
        submitList(data.toMutableList())
    }

    inner class PhotoVH(val itemPhotoBinding: ItemPhotoBinding) :
        RecyclerView.ViewHolder(itemPhotoBinding.root) {

        fun bind(item: PhotoItem) {
            Picasso.get()
                .load(item.thumbnailUrl)
                .into(itemPhotoBinding.ivPhoto)
        }
    }

    private class PhotoItemDC : DiffUtil.ItemCallback<PhotoItem>() {
        override fun areItemsTheSame(
            oldItem: PhotoItem,
            newItem: PhotoItem
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: PhotoItem,
            newItem: PhotoItem
        ): Boolean {
            return oldItem == newItem
        }
    }
}