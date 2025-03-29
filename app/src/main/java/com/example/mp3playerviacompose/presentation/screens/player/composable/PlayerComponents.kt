package com.example.mp3playerviacompose.presentation.screens.player.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.mp3playerviacompose.domain.utils.Constants.LARGE_PADDING

@Composable
//@Preview(showSystemUi = true)
fun PlayerComponents(rotationDegrees: Float){
    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = LARGE_PADDING)) {



    }

}


@Composable
@Preview(showSystemUi = true)
fun PreviewPlayerComponents(){
    PlayerComponents(rotationDegrees = 0f)
}