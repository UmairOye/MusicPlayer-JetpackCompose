package com.example.mp3playerviacompose.presentation.composable


import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mp3playerviacompose.R

@Composable
fun CustomCheckBox(checked: Boolean, onClick: (Boolean) -> Unit){
    var isChecked by remember {
        mutableStateOf(checked)
    }

    val alpha: Float by animateFloatAsState(if (isChecked) 1f else 0.5f, label = "alpha")
    val drawableResId = if(isChecked){
        R.drawable.playlist_checked
    }else{
        R.drawable.playlist_unchecked
    }

    Image(painter = painterResource(id = drawableResId), contentDescription = stringResource(R.string.checkbox),
        contentScale = ContentScale.Crop,
        modifier = Modifier.size(30.dp)
            .graphicsLayer(alpha = alpha)
            .clickable {
                isChecked = !isChecked
                onClick(isChecked)
            })

}


@Preview
@Composable
fun CustomCheckBoxPreview(){
    CustomCheckBox(checked = true, onClick = {})
}