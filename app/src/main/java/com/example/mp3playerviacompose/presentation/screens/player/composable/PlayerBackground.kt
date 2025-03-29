package com.example.mp3playerviacompose.presentation.screens.player.composable

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.mp3playerviacompose.R

@Composable
fun PlayerBackground(data: String) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current).data(data).crossfade(true)
            .build(),
        contentScale = ContentScale.Crop,
        contentDescription = stringResource(id = R.string.audio_icon),
        modifier = Modifier.fillMaxSize().blur(radiusY = 25.dp, radiusX = 25.dp)
    )
}