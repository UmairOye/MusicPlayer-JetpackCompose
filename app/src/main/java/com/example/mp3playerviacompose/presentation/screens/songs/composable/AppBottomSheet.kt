package com.example.mp3playerviacompose.presentation.screens.songs.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.example.mp3playerviacompose.R
import com.example.mp3playerviacompose.domain.utils.Constants.BUTTON_HEIGHT
import com.example.mp3playerviacompose.domain.utils.Constants.BUTTON_ROUNDNESS
import com.example.mp3playerviacompose.domain.utils.Constants.HANDLER_HEIGHT
import com.example.mp3playerviacompose.domain.utils.Constants.HANDLER_WIDTH
import com.example.mp3playerviacompose.domain.utils.Constants.MEDIUM_PADDING
import com.example.mp3playerviacompose.domain.utils.Constants.SHEET_TOP_PADDING
import com.example.mp3playerviacompose.domain.utils.Constants.TONAL_ELEVATION

@OptIn(ExperimentalMaterial3Api::class)
@Composable
//@Preview(showSystemUi = true)
fun ShowBottomSheet(isBottomSheetVisible: Boolean,
                    sheetState: SheetState,
                    onDismiss:()-> Unit, onRequestPermission: ()-> Unit){


    if(isBottomSheetVisible){
        ModalBottomSheet(onDismissRequest = {onDismiss()},
            shape = RoundedCornerShape(topStart = SHEET_TOP_PADDING,
                topEnd = SHEET_TOP_PADDING),
            tonalElevation = TONAL_ELEVATION,
            sheetState = sheetState,
            containerColor = colorResource(id = R.color.white),
            dragHandle = {
                Box(modifier = Modifier
                    .padding(top = MEDIUM_PADDING)
                    .width(HANDLER_WIDTH)
                    .height(HANDLER_HEIGHT)
                    .clip(RoundedCornerShape(50))
                    .background(color = Color.LightGray))
            }
        ) {

            Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(all = SHEET_TOP_PADDING)
                    .fillMaxWidth()) {
                Text(text = stringResource(R.string.unlock_your_music_experience),
                    fontSize = 28.sp,
                    fontWeight = FontWeight.SemiBold,
                    style = TextStyle(color = MaterialTheme.colorScheme.onBackground),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.padding(top = SHEET_TOP_PADDING))

                Text(text = stringResource(R.string.audio_desc),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    style = TextStyle(color = MaterialTheme.colorScheme.onBackground),
                    textAlign = TextAlign.Justify)

                Spacer(modifier = Modifier.padding(top = SHEET_TOP_PADDING))

                Button(onClick = { onRequestPermission()},
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(BUTTON_HEIGHT),
                    colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.sky_blue)),
                    shape = RoundedCornerShape(BUTTON_ROUNDNESS)
                ) {
                    Text(text = stringResource(R.string.grant_permission),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 15.sp,
                        color = Color.White,
                        style = TextStyle(color = MaterialTheme.colorScheme.onBackground)
                    )
                }
            }
        }
    }

}