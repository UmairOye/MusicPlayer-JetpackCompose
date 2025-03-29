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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.app.ActivityCompat
import com.example.mp3playerviacompose.R
import com.example.mp3playerviacompose.domain.models.MediaModel
import com.example.mp3playerviacompose.domain.utils.Constants
import com.example.mp3playerviacompose.domain.utils.Utils
import com.example.mp3playerviacompose.domain.utils.deleteFile
import java.io.File

@Composable
//@Preview(showSystemUi = true)
fun DeleteOperation(
    mediaUri: MediaModel,
    performDeleteOperation: Boolean,
    onDeleted: (MediaModel) -> Unit,
    onNotDeleted: () -> Unit,
) {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract =
        ActivityResultContracts.StartIntentSenderForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            onDeleted(mediaUri)
        } else {
            onNotDeleted()
        }
    }


    LaunchedEffect(performDeleteOperation) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val contentResolver = context.contentResolver
            try {
                val intentSender = MediaStore.createDeleteRequest(
                    contentResolver,
                    listOf(Uri.parse(mediaUri.uri))
                ).intentSender
                val request = IntentSenderRequest.Builder(intentSender).build()
                launcher.launch(request)
            } catch (e: Exception) {
                Utils.printLog(message = "loggingCall -- ${e.message}")
                e.printStackTrace()
            }
        } else {
            if (context.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Utils.printLog(message = "loggingCall -- yes ${mediaUri.path}")
                File(mediaUri.path).deleteFile(context) { isDeleted ->
                    if (isDeleted) {
                        Utils.printLog(message = "loggingCall -- isDeleted")
                        onDeleted(mediaUri)
                    } else {
                        Utils.printLog(message = "loggingCall -- isDeleted not")
                        onNotDeleted()
                    }
                }
            } else {
                Utils.printLog(message = "loggingCall -- no")
                ActivityCompat.requestPermissions(
                    (context as Activity),
                    arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    101
                )
                File(mediaUri.path).deleteFile(context) { isDeleted ->
                    if (isDeleted) {
                        onDeleted(mediaUri)
                    } else {
                        onNotDeleted()
                    }
                }
            }
        }
    }
}


//@Preview
@Composable
fun PlayListDialog(
    isAlertDialogVisible: Boolean,
    onDialogDismiss: () -> Unit,
    onConfirmation: (String) -> Unit
) {

    if(isAlertDialogVisible){
        Dialog(onDismissRequest = {onDialogDismiss()}) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                shape = RoundedCornerShape(Constants.MEDIUM_PADDING)
            ) {

                var playListName by remember {
                    mutableStateOf("")
                }

                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(id = R.string.add_to_playlist),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp,
                        style = TextStyle(MaterialTheme.colorScheme.onBackground),
                        modifier = Modifier.padding(top = 16.dp, start = Constants.MEDIUM_PADDING).fillMaxWidth(),
                        textAlign = TextAlign.Start
                    )


                    TextField(
                        value = playListName, onValueChange = { playListName = it },
                        placeholder = { Text(text = "Enter playlist name") },
                        modifier = Modifier.padding(top = Constants.MEDIUM_PADDING_1, start = Constants.MEDIUM_PADDING, end = Constants.MEDIUM_PADDING),
                        textStyle = TextStyle(MaterialTheme.colorScheme.onBackground),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    )


                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = Constants.EXTRA_LARGE_PADDING, bottom = Constants.LARGE_PADDING, end = Constants.MEDIUM_PADDING)
                    ) {

                        Text(
                            text = stringResource(R.string.add),
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            style = TextStyle(MaterialTheme.colorScheme.onBackground),
                            textAlign = TextAlign.End,
                            modifier = Modifier.padding(end = Constants.MEDIUM_PADDING).clickable {
                                onConfirmation(playListName)
                                onDialogDismiss()
                            }
                        )

                        Text(
                            text = stringResource(R.string.cancel),
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            style = TextStyle(MaterialTheme.colorScheme.onBackground),
                            textAlign = TextAlign.End,
                            modifier = Modifier.clickable { onDialogDismiss() }
                        )
                    }
                }

            }
        }
    }

}


@Composable
//@Preview(showSystemUi = true)
fun ShowDeleteDialog(
    isAlertDialogVisible: Boolean,
    onDismiss: () -> Unit,
    onConfirmation: () -> Unit,
) {
    if (isAlertDialogVisible) {
        AlertDialog(
            title = { Text(text = stringResource(R.string.delete_operation)) },
            text = { Text(text = stringResource(R.string.delete_desc)) },
            confirmButton = {
                Text(text = stringResource(R.string.yes_delete),
                    modifier = Modifier.clickable {
                        onConfirmation()
                    })
            },
            dismissButton = {
                Text(text = stringResource(R.string.cancel),
                    modifier = Modifier.clickable {
                        onDismiss()
                    })
            },
            onDismissRequest = { onDismiss() },
            containerColor = MaterialTheme.colorScheme.surface
        )
    }
}