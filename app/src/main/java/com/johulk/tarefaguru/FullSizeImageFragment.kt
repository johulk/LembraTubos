package com.johulk.tarefaguru

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.johulk.tarefaguru.util.BitmapDataHolder
import com.johulk.tarefaguru.databinding.FragmentFullSizeImageBinding

class FullSizeImageFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val binding = FragmentFullSizeImageBinding.inflate(inflater, container, false)
        val imageView = binding.fullSizeImageView

        // Retrieve the image position from arguments
        val imagePosition = arguments?.getInt("imagePosition", -1)
        val cliente = requireArguments().getString("idcliente")

        val thumbnailBitmaps = BitmapDataHolder.getThumbnailBitmaps(cliente!!)

        if (imagePosition != null) {
            if (imagePosition != -1 && thumbnailBitmaps != null && imagePosition < thumbnailBitmaps.size) {
                // Use imagePosition to access the corresponding image in thumbnailBitmaps
                val imageBitmap = thumbnailBitmaps[imagePosition]
                imageView.setImageBitmap(imageBitmap)
            }
        }

        return binding.root
    }
}
