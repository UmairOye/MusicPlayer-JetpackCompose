package com.example.mp3playerviacompose.presentation.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.mp3playerviacompose.R

@Composable
fun BackButton(heading: String = "Music Player" ,color: Color,modifier: Modifier, onBackClick: () -> Unit){

    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement =
    Arrangement.Start,
        modifier = modifier
            .fillMaxWidth()) {

        IconButton(onClick = { onBackClick()}) {
            Icon(imageVector = Icons.Default.ArrowBack,
                contentDescription = stringResource(R.string.backarrow),
                tint = color)
        }
        
        Text(
            text = heading,
            fontSize = 24.sp,
            fontWeight = FontWeight.SemiBold,
            color = color,
        )
    }
}


@Composable
@Preview(showSystemUi = false, heightDp = 50, showBackground = false)
fun PreviewBackButton(){
    BackButton(modifier = Modifier, color = colorResource(id = R.color.back_button_color)) {
    }
}