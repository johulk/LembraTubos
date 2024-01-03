package com.johulk.tarefaguru.documentsHandler

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ThumbnailUtils
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.exifinterface.media.ExifInterface
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.johulk.tarefaguru.R
import com.johulk.tarefaguru.databinding.FragmentDocumentsBinding
import com.johulk.tarefaguru.fragments.LibraryFragmentDirections
import com.johulk.tarefaguru.util.BitmapDataHolder

class DocumentsFragment(val idcliente: String) : Fragment() {

    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var thumbnailAdapter: ThumbnailAdapter
    private lateinit var binding: FragmentDocumentsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentDocumentsBinding.inflate(inflater, container, false)

        recyclerView = binding.recyclerView
        swipeRefreshLayout = binding.swipeRefreshLayout

        setupRecyclerView()
        setupSwipeRefreshLayout()
        setupButton()
        loadInitialData()
        return binding.root
    }

    private fun setupRecyclerView() {
        recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        thumbnailAdapter = ThumbnailAdapter(
            object : ThumbnailAdapter.ThumbnailClickListener {
                override fun onThumbnailClick(position: Int) {
                    openFullSizeImage(position)
                }
            },
        )

        recyclerView.adapter = thumbnailAdapter
    }

    private fun loadInitialData() {
        val thumbnailBitmaps = BitmapDataHolder.getThumbnailBitmaps(idcliente)
        if (thumbnailBitmaps != null) {
            thumbnailAdapter.submitList(thumbnailBitmaps)
        } else {
            val newThumbnailBitmaps = retrieveThumbnailBitmaps(idcliente)
            thumbnailAdapter.submitList(newThumbnailBitmaps)
            BitmapDataHolder.setThumbnailBitmaps(idcliente, newThumbnailBitmaps)
        }
    }
    private fun setupSwipeRefreshLayout() {
        swipeRefreshLayout.setOnRefreshListener {
            reloadData(idcliente)
        }
    }

    private fun setupButton() {
        binding.buttonAdicionarFoto.setOnClickListener {
            val direction =
                LibraryFragmentDirections.actionLibraryFragmentToCameraFragment(idcliente)
            findNavController().navigate(direction)
        }
    }

    private fun openFullSizeImage(position: Int) {
        val thumbnailBitmaps = BitmapDataHolder.getThumbnailBitmaps(idcliente)
        if (thumbnailBitmaps != null) {
            if (position >= 0 && position < thumbnailBitmaps.size) {
                val bundle = bundleOf(
                    "imagePosition" to position,
                    "idcliente" to idcliente,
                )
                findNavController().navigate(R.id.fullSizeImageFragment, bundle)
            }
        }
    }

    private fun reloadData(idcliente: String) {
        val thumbnailBitmaps = retrieveThumbnailBitmaps(idcliente)
        thumbnailAdapter.submitList(thumbnailBitmaps)
        BitmapDataHolder.setThumbnailBitmaps(idcliente, thumbnailBitmaps)
        swipeRefreshLayout.isRefreshing = false
    }

    private fun retrieveThumbnailBitmaps(clientId: String): List<Bitmap> {
        val thumbnailBitmaps = mutableListOf<Bitmap>()

        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DATA,
        )

        val selection = "${MediaStore.Images.Media.RELATIVE_PATH} LIKE ?"
        val selectionArgs = arrayOf("%Pictures/CameraX-Image/$clientId%")

        val cursor = requireContext().contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            selectionArgs,
            null,
        )

        cursor?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            val dataColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)

            while (cursor.moveToNext()) {
                val imageId = cursor.getLong(idColumn)
                val imagePath = cursor.getString(dataColumn)
                val thumbnailBitmap = generateThumbnail(imagePath)
                thumbnailBitmap?.let {
                    thumbnailBitmaps.add(it)
                }
            }
        }

        return thumbnailBitmaps
    }

    private fun generateThumbnail(imagePath: String): Bitmap? {
        try {
            // Create an ExifInterface to read the image's EXIF data
            val exif = ExifInterface(imagePath)

            // Get the orientation information from the EXIF data
            val orientation = exif.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL,
            )

            // Calculate the rotation angle based on the orientation
            val rotationAngle = when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> 90
                ExifInterface.ORIENTATION_ROTATE_180 -> 180
                ExifInterface.ORIENTATION_ROTATE_270 -> 270
                else -> 0
            }

            // Decode the image and apply the rotation
            val options = BitmapFactory.Options()
            val fullSizeBitmap = BitmapFactory.decodeFile(imagePath, options)
            val rotatedBitmap = rotateBitmap(fullSizeBitmap, rotationAngle)

            // Create a thumbnail from the rotated bitmap
            return ThumbnailUtils.extractThumbnail(rotatedBitmap, THUMBNAIL_WIDTH, THUMBNAIL_HEIGHT)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }

    private fun rotateBitmap(source: Bitmap, angle: Int): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(angle.toFloat())
        return Bitmap.createBitmap(source, 0, 0, source.width, source.height, matrix, true)
    }

    companion object {
        // Define the width and height of the thumbnails
        private const val THUMBNAIL_WIDTH = 350
        private const val THUMBNAIL_HEIGHT = 350
    }
}
