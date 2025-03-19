package com.example.mp3playerviacompose.presentation.composable

import android.app.Activity
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.app.ActivityCompat
import com.example.mp3playerviacompose.R
import com.example.mp3playerviacompose.domain.models.MediaModel
import com.example.mp3playerviacompose.domain.utils.Utils
import com.example.mp3playerviacompose.domain.utils.deleteFile
import java.io.File

@Composable
//@Preview(showSystemUi = true)
fun DeleteOperation(mediaUri: MediaModel, performDeleteOperation: Boolean, onDeleted:()-> Unit, onNotDeleted:()-> Unit) {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(contract =
    ActivityResultContracts.StartIntentSenderForResult()) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            onDeleted()
        } else {
            onNotDeleted()
        }
    }


    LaunchedEffect(performDeleteOperation) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val contentResolver = context.contentResolver
            try {
                val intentSender = MediaStore.createDeleteRequest(contentResolver, listOf(Uri.parse(mediaUri.uri))).intentSender
                val request = IntentSenderRequest.Builder(intentSender).build()
                launcher.launch(request)
            } catch (e: Exception) {
                Utils.printLog(message = "loggingCall -- ${e.message}")
                e.printStackTrace()
            }
        }else{
            if(context.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                Utils.printLog(message = "loggingCall -- yes ${mediaUri.path}")
                File(mediaUri.path).deleteFile(context) {isDeleted ->
                    if(isDeleted){
                        Utils.printLog(message = "loggingCall -- isDeleted")
                        onDeleted()
                    }else{
                        Utils.printLog(message = "loggingCall -- isDeleted not")
                        onNotDeleted()
                    }
                }
            }else{
                Utils.printLog(message = "loggingCall -- no")
                ActivityCompat.requestPermissions(
                    (context as Activity),
                    arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    101
                )
                File(mediaUri.path).deleteFile(context) {isDeleted ->
                    if(isDeleted){
                        onDeleted()
                    }else{
                        onNotDeleted()
                    }
                }
            }
        }
    }




}


@Composable
//@Preview(showSystemUi = true)
fun ShowDeleteDialog(isAlertDialogVisible: Boolean, onDismiss: () -> Unit, onConfirmation:()-> Unit) {
   if(isAlertDialogVisible){
       AlertDialog(
           title = { Text(text = stringResource(R.string.delete_operation)) },
           text = { Text(text = stringResource(R.string.delete_desc)) },
           confirmButton = { Text(text = stringResource(R.string.yes_delete),
               modifier = Modifier.clickable {
                   onConfirmation()
               })},
           dismissButton = { Text(text = stringResource(R.string.cancel),
               modifier = Modifier.clickable {
                   onDismiss()
               })},
           onDismissRequest = { onDismiss() },
           containerColor = MaterialTheme.colorScheme.surface)
   }
}