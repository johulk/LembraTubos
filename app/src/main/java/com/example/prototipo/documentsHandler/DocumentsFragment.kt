package com.example.prototipo.documentsHandler

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
import com.example.prototipo.R
import com.example.prototipo.util.BitmapDataHolder

class DocumentsFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var thumbnailAdapter: ThumbnailAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val rootView = inflater.inflate(R.layout.fragment_documents, container, false)

        val recyclerView = rootView?.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView?.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        recyclerView?.let {
            // Retrieve the list of thumbnail bitmaps (you'll need to implement this)
            val thumbnailBitmaps = retrieveThumbnailBitmaps()

            BitmapDataHolder.thumbnailBitmaps = thumbnailBitmaps

            val thumbnailAdapter = ThumbnailAdapter(
                thumbnailBitmaps,
                object : ThumbnailAdapter.ThumbnailClickListener {
                    override fun onThumbnailClick(position: Int) {
                        // Handle the click event here, e.g., open the full-sized image
                        openFullSizeImage(position)
                    }
                },
            )
            it.adapter = thumbnailAdapter
        }

        return rootView
    }

    private fun openFullSizeImage(position: Int) {
        val bundle = bundleOf("imagePosition" to position)
        findNavController().navigate(R.id.fullSizeImageFragment, bundle)
    }

    private fun retrieveThumbnailBitmaps(): List<Bitmap> {
        val thumbnailBitmaps = mutableListOf<Bitmap>()

        // Define the projection to query image data
        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DATA,
        )

        // Query the MediaStore for image data
        val cursor = requireContext().contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            null,
        )

        cursor?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            val dataColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)

            while (cursor.moveToNext()) {
                val imageId = cursor.getLong(idColumn)
                val imagePath = cursor.getString(dataColumn)

                // Generate a thumbnail from the full-sized image
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
            val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)

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
