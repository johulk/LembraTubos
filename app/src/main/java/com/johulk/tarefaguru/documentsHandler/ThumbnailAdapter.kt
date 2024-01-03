package com.johulk.tarefaguru.documentsHandler

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.johulk.tarefaguru.databinding.ThumbnailLayoutBinding

class ThumbnailAdapter(
    private val clickListener: ThumbnailClickListener,
) : ListAdapter<Bitmap, ThumbnailAdapter.ThumbnailViewHolder>(ThumbnailDiffCallback()) {

    interface ThumbnailClickListener {
        fun onThumbnailClick(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ThumbnailViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ThumbnailLayoutBinding.inflate(inflater, parent, false)
        return ThumbnailViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ThumbnailViewHolder, position: Int) {
        val thumbnailBitmap = getItem(position)
        holder.bind(thumbnailBitmap, position)
    }

    inner class ThumbnailViewHolder(private val binding: ThumbnailLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(thumbnailBitmap: Bitmap, position: Int) {
            binding.thumbnailImageView.setImageBitmap(thumbnailBitmap)

            // Set a click listener for the thumbnail view
            binding.thumbnailImageView.setOnClickListener {
                clickListener.onThumbnailClick(position)
            }
        }
    }
}

class ThumbnailDiffCallback : DiffUtil.ItemCallback<Bitmap>() {
    override fun areItemsTheSame(oldItem: Bitmap, newItem: Bitmap): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Bitmap, newItem: Bitmap): Boolean {
        return oldItem.sameAs(newItem)
    }
}
