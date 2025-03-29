package com.example.mp3playerviacompose.domain.utils

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaScannerConnection
import android.net.Uri
import android.provider.Settings
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.example.mp3playerviacompose.BuildConfig
import java.io.File
import java.util.Locale


fun Context.openAppSettings() {
    val intent = Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", packageName, null)
    )
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    startActivity(intent)
}

fun Context.hasPermission(permission: String): Boolean =
    ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED

fun Long.getFileSize(): String {
    val sizes = arrayOf("B", "KB", "MB", "GB", "TB")
    var sizeIndex = 0
    var size = this.toDouble()

    while (size >= 1024 && sizeIndex < sizes.size - 1) {
        size /= 1024
        sizeIndex++
    }

    return String.format(Locale.US, "%.2f %s", size, sizes[sizeIndex])
}


fun Long.formatDuration(): String {
    val totalSeconds = this / 1000
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    val seconds = totalSeconds % 60

    return if (hours > 0) {
        String.format(Locale.US, "%02d:%02d:%02d", hours, minutes, seconds)
    } else {
        String.format(Locale.US, "%02d:%02d", minutes, seconds)
    }
}

fun File.shareFile(context: Context) {
    try {
        val uri = FileProvider.getUriForFile(
            context,
            "${BuildConfig.APPLICATION_ID}.contentProvider",
            this
        )
        val share = Intent()
        share.action = Intent.ACTION_SEND
        share.type = "Video/*"
        share.putExtra(Intent.EXTRA_STREAM, uri)
        context.startActivity(Intent.createChooser(share, "Share File"))
    } catch (e: Exception) {
        e.printStackTrace()
    }
}


fun File.deleteFile(context: Context,onDeleted: (Boolean) -> Unit) {
    this.absolutePath.scannedFile(context){
        when(it){
            true -> { onDeleted(true) }
            false -> { onDeleted(true) }

        }
    }
}


fun String.scannedFile(context: Context, onDeleted: (Boolean) -> Unit) {
    try {
        MediaScannerConnection.scanFile(
            context, arrayOf(this),
            null
        ) { _, uri ->
            context.contentResolver
                .delete(uri, null, null)
            onDeleted(true)
        }
    } catch (e: java.lang.Exception) {
        onDeleted(false)
        e.printStackTrace()
    }
}
