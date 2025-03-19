package com.example.mp3playerviacompose.presentation.composable

import android.app.Activity
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import com.example.mp3playerviacompose.R
import com.example.mp3playerviacompose.data.local.SharedPref
import com.example.mp3playerviacompose.domain.utils.Constants
import com.example.mp3playerviacompose.domain.utils.Constants.SEND_USER_TO_SETTINGS
import com.example.mp3playerviacompose.domain.utils.Constants.permission
import com.example.mp3playerviacompose.domain.utils.hasPermission
import com.example.mp3playerviacompose.domain.utils.openAppSettings

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun AskedPermission(isPermissionCalled: Boolean, permissionStatus: (Boolean) -> Unit) {

    val context = LocalContext.current
    val hasPermission by remember {
        mutableStateOf(
            context.hasPermission(permission)
        )
    }


    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()) { isGranted ->
            when (isGranted) {
                true -> {
                    permissionStatus(true)
                }

                false -> {
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(context as Activity, permission)) {
                        SharedPref.setBool(SEND_USER_TO_SETTINGS, true)
                    }
                    permissionStatus(false)
                }
            }
        }

    LaunchedEffect(isPermissionCalled){
        if (!hasPermission) {
            if (SharedPref.getBool(SEND_USER_TO_SETTINGS, false)) {
                context.openAppSettings()
            } else {
                launcher.launch(permission)
            }
        }
    }

}


@Composable
fun RationalPermission(onButtonClick:()-> Unit){

    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center, modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = Constants.LARGE_PADDING)) {
        Image(painter = painterResource(id = R.drawable.sad), contentDescription = stringResource(R.string.sad_icon))
        Spacer(modifier = Modifier.padding(top = Constants.SHEET_TOP_PADDING))
        Text(text = stringResource(R.string.why_we_need_this_permission),
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            style = TextStyle(MaterialTheme.colorScheme.onBackground),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.padding(top = Constants.MEDIUM_PADDING))
        Text(text = stringResource(R.string.rational_desc),
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            style = TextStyle(MaterialTheme.colorScheme.onBackground),
            textAlign = TextAlign.Justify
        )

        Spacer(modifier = Modifier.padding(top = Constants.EXTRA_LARGE_PADDING))
        Button(onClick = { onButtonClick() },
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(id = R.color.sky_blue)
            ), modifier = Modifier
                .fillMaxWidth()
                .height(Constants.BUTTON_HEIGHT)) {
            
            Text(text = stringResource(id = R.string.grant_permission),
                color = Color.White,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold)
            
        }
    }

}

@Preview(showSystemUi = true)
@Composable
fun PreviewRationalPermission(){
    RationalPermission {

    }
}