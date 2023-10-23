package com.example.prototipo.fragments

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ThumbnailUtils
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.prototipo.databinding.FragmentShortsBinding

class ShortsFragment : Fragment() {

    private lateinit var viewBinding: FragmentShortsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        viewBinding = FragmentShortsBinding.inflate(layoutInflater)

        val THUMBSIZE = 150

        val ThumbImage = ThumbnailUtils.extractThumbnail(
            BitmapFactory.decodeFile("/storage/emulated/0/Pictures/CameraX-Image/2023-09-24-18-10-26-730.jpg"),
            THUMBSIZE,
            THUMBSIZE,
        )

        viewBinding.imageView3.setImageBitmap(rotate(ThumbImage, 90f))
        viewBinding.imageView3.setOnClickListener {
        }

        return viewBinding.root
    }
}

fun rotate(_input: Bitmap, _angle: Float): Bitmap? {
    val matrix = Matrix()
    matrix.postRotate(_angle)
    return Bitmap.createBitmap(_input, 0, 0, _input.width, _input.height, matrix, true)
}
