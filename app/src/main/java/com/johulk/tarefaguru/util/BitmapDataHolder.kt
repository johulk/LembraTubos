package com.johulk.tarefaguru.util

import android.graphics.Bitmap

object BitmapDataHolder {
    private val thumbnailBitmapsMap: MutableMap<String, List<Bitmap>> = mutableMapOf()

    fun getThumbnailBitmaps(clientId: String): List<Bitmap>? {
        return thumbnailBitmapsMap[clientId]
    }

    fun setThumbnailBitmaps(clientId: String, thumbnails: List<Bitmap>) {
        thumbnailBitmapsMap[clientId] = thumbnails
    }

    // Add other functions as needed
}