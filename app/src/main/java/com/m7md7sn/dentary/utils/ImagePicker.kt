package com.m7md7sn.dentary.utils

import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import java.io.File
import java.io.FileOutputStream
import kotlin.math.min

/**
 * Original simple gallery picker (kept for compatibility).
 */
@Composable
fun rememberImagePicker(
    onImageSelected: (Uri?) -> Unit
): () -> Unit {
    LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        onImageSelected(uri)
    }

    return remember {
        {
            launcher.launch("image/*")
        }
    }
}

/**
 * Controller returned by the camera + gallery chooser.
 */
data class ImagePickerController(
    val pickFromGallery: () -> Unit,
    val takePhoto: () -> Unit
)

/**
 * New helper that provides both gallery and camera capture launchers.
 * For camera capture it takes a preview bitmap, crops center square and saves
 * into the app cache directory, then returns a file:// Uri to the caller.
 * This avoids adding external cropping libraries while providing basic crop.
 */
@Composable
fun rememberImagePickerWithCamera(
    onImageSelected: (Uri?) -> Unit
): ImagePickerController {
    val context = LocalContext.current

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        onImageSelected(uri)
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap: Bitmap? ->
        if (bitmap == null) {
            onImageSelected(null)
            return@rememberLauncherForActivityResult
        }

        try {
            // Simple center-crop to square
            val width = bitmap.width
            val height = bitmap.height
            val size = min(width, height)
            val x = (width - size) / 2
            val y = (height - size) / 2
            val cropped = Bitmap.createBitmap(bitmap, x, y, size, size)

            // Write to cache
            val cacheFile = File(context.cacheDir, "picked_${System.currentTimeMillis()}.jpg")
            FileOutputStream(cacheFile).use { out ->
                cropped.compress(Bitmap.CompressFormat.JPEG, 85, out)
            }

            // Recycle bitmaps
            try { cropped.recycle() } catch (_: Exception) {}

            val uri = Uri.fromFile(cacheFile)
            onImageSelected(uri)
        } catch (e: Exception) {
            // On error return null
            onImageSelected(null)
        }
    }

    return remember {
        ImagePickerController(
            pickFromGallery = { galleryLauncher.launch("image/*") },
            takePhoto = { cameraLauncher.launch(null) }
        )
    }
}
