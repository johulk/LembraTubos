package com.example.prototipo.documentsHandler

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.prototipo.R

class ThumbnailAdapter(private val imageList: List<Bitmap>, private val clickListener: ThumbnailClickListener) :
    RecyclerView.Adapter<ThumbnailAdapter.ThumbnailViewHolder>() {

    inner class ThumbnailViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val thumbnailImageView: ImageView = itemView.findViewById(R.id.thumbnailImageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ThumbnailViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.thumbnail_layout, parent, false)
        return ThumbnailViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ThumbnailViewHolder, position: Int) {
        val thumbnailBitmap = imageList[position]
        holder.thumbnailImageView.setImageBitmap(thumbnailBitmap)

        // Set a click listener for the thumbnail view
        holder.thumbnailImageView.setOnClickListener {
            clickListener.onThumbnailClick(position)
        }
    }

    override fun getItemCount(): Int {
        return imageList.size
    }

    interface ThumbnailClickListener {
        fun onThumbnailClick(position: Int)
    }
}
