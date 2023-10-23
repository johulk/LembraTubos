package com.example.prototipo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.example.prototipo.util.BitmapDataHolder

class FullSizeImageFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_full_size_image, container, false)
        val imageView = rootView.findViewById<ImageView>(R.id.fullSizeImageView)

        // Retrieve the image position from arguments
        val imagePosition = arguments?.getInt("imagePosition", -1)

        // Retrieve the list of bitmaps from BitmapDataHolder
        val thumbnailBitmaps = BitmapDataHolder.thumbnailBitmaps

        if (imagePosition != null) {
            if (imagePosition != -1 && thumbnailBitmaps != null && imagePosition < thumbnailBitmaps.size) {
                // Use imagePosition to access the corresponding image in thumbnailBitmaps
                val imageBitmap = thumbnailBitmaps[imagePosition]
                imageView.setImageBitmap(imageBitmap)
            }
        }

        return rootView
    }
}
