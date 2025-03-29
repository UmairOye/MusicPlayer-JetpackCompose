package com.example.mp3playerviacompose.presentation.screens.player.playerScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.mp3playerviacompose.R
import com.example.mp3playerviacompose.presentation.composable.BackButton
import com.example.mp3playerviacompose.presentation.screens.player.composable.PlayerBackground
import com.example.mp3playerviacompose.presentation.screens.player.playerViewModel.PlayerViewModel
import com.example.mp3playerviacompose.domain.utils.Constants.COMPONENT_PADDING
import com.example.mp3playerviacompose.domain.utils.Constants.EXTRA_LARGE_PADDING
import com.example.mp3playerviacompose.domain.utils.Constants.LARGE_PADDING
import com.example.mp3playerviacompose.domain.utils.Constants.LARGE_PADDING_1
import com.example.mp3playerviacompose.domain.utils.Constants.MEDIUM_PADDING
import com.example.mp3playerviacompose.domain.utils.Constants.MUSIC_ICON_SIZE
import com.example.mp3playerviacompose.domain.utils.Constants.SHEET_TOP_PADDING
import com.example.mp3playerviacompose.domain.utils.Constants.SMALL_PADDING
import com.example.mp3playerviacompose.domain.utils.Utils
import com.example.mp3playerviacompose.domain.utils.formatDuration

@Composable
fun PlayerScreen(
    mediaPath: String,
    onNavigation: (String) -> Unit,
    onBackPressed: () -> Unit,
    lifecycleOwner: LifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current
) {
    val playerViewModel: PlayerViewModel = hiltViewModel()
    var isPlaying by remember {
        mutableStateOf(playerViewModel.isMusicPlaying())
    }

    var isUserSliding by remember { mutableStateOf(false) }
    val currentPosition = playerViewModel.currentPlayerPosition.collectAsStateWithLifecycle()
    var currentPost = 0
    val currentPosTemp = remember { mutableFloatStateOf(0f) }

    var mediaModel = Utils.playerList.find { it.path == mediaPath }
    var currentIndex = Utils.playerList.indexOf(mediaModel)
    mediaModel?.let { playerViewModel.playMusic(it)
    isPlaying = true}


    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START -> {
                }

                else -> {}
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            Utils.playerList.clear()
            playerViewModel.stopMusic()
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }


    Scaffold { innerPadding ->
        mediaModel?.thumbnail?.let {
            PlayerBackground(data =it)
        }

        Column(
            modifier = Modifier
                .consumeWindowInsets(innerPadding)
                .fillMaxSize()
                .background(color = colorResource(id = R.color.layer_color)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            BackButton(heading = stringResource(R.string.player), modifier = Modifier.padding(top = LARGE_PADDING_1),
                color = Color.White) {
                onBackPressed()
            }

            LaunchedEffect(currentPosition.value) {
                if (!isUserSliding) {
                    currentPost = currentPosition.value
                    currentPosTemp.floatValue = currentPosition.value.toFloat()
                }
            }


            LaunchedEffect(isPlaying) {
                if (isPlaying) {
                    playerViewModel.resumeMusic()
                } else {
                    playerViewModel.pauseMusic()
                }
            }


            Spacer(modifier = Modifier.padding(top = COMPONENT_PADDING))
            mediaModel?.thumbnail?.let {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current).data(it).crossfade(true)
                        .build(),
                    placeholder = painterResource(id = R.drawable.music),
                    error = painterResource(id = R.drawable.music),
                    contentScale = ContentScale.Crop,
                    contentDescription = stringResource(id = R.string.audio_icon),
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(MUSIC_ICON_SIZE)
                )
            }


            Spacer(modifier = Modifier.padding(top = SHEET_TOP_PADDING))
            mediaModel?.name?.let {
                Text(
                    text = it,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 22.sp,
                    maxLines = 1,
                    modifier = Modifier.padding(horizontal = LARGE_PADDING),
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.padding(top = SMALL_PADDING))
            mediaModel?.artist?.let {
                Text(
                    text = it,
                    fontWeight = FontWeight.Normal,
                    fontSize = 15.sp,
                    maxLines = 1,
                    modifier = Modifier.padding(horizontal = LARGE_PADDING),
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.padding(top = EXTRA_LARGE_PADDING))
            Column(
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(bottom = MEDIUM_PADDING)
            ) {


                Slider(
                    value = if (currentPosTemp.floatValue == 0f) currentPost.toFloat() else currentPosTemp.floatValue,
                    steps = 0,
                    onValueChange = { currentPosTemp.floatValue = it
                        isUserSliding = true },
                    onValueChangeFinished = {
                        currentPost = currentPosTemp.floatValue.toInt()
                        playerViewModel.seekToPosition(currentPost)
                        currentPosTemp.floatValue = 0f
                        isUserSliding = false
                    },
                    colors = SliderDefaults.colors(
                        thumbColor = colorResource(id = R.color.sky_blue),
                        inactiveTrackColor = Color.LightGray,
                        activeTrackColor = colorResource(id = R.color.sky_blue)
                    ),
                    enabled = true,
                    valueRange = 0f..playerViewModel.getCurrentMusicDuration().toFloat(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = MEDIUM_PADDING)
                )


                Spacer(modifier = Modifier.padding(top = SMALL_PADDING))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = LARGE_PADDING),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = currentPosition.value.toLong().formatDuration(),
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp,
                        color = Color.White
                    )

                    mediaModel?.formattedDuration?.let {
                        Text(
                            text = it,
                            fontWeight = FontWeight.Normal,
                            fontSize = 14.sp,
                            color = Color.White
                        )
                    }
                }


                Spacer(modifier = Modifier.padding(top = MEDIUM_PADDING))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = LARGE_PADDING,
                            end = LARGE_PADDING,
                            bottom = LARGE_PADDING_1
                        )
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.backward),
                        contentDescription = stringResource(R.string.back_button),
                        modifier = Modifier
                            .height(40.dp)
                            .height(40.dp)
                            .clip(CircleShape)
                            .clickable {
                                if (Utils.playerList.isNotEmpty()) {
                                    if (currentIndex == 0) {
                                        currentIndex = Utils.playerList.size - 1
                                    } else {
                                        currentIndex -= 1
                                    }
                                    mediaModel = Utils.playerList[currentIndex]
                                    mediaModel?.let { currentSong ->
                                        isPlaying = true
                                        playerViewModel.playMusic(currentSong)
                                    }
                                }
                            }
                    )


                    val imageIcon = if (isPlaying) {
                        R.drawable.pause
                    } else {
                        R.drawable.play
                    }

                    Image(painter = painterResource(id = imageIcon),
                        contentDescription = stringResource(R.string.play_pause),
                        modifier = Modifier
                            .height(60.dp)
                            .clip(CircleShape)
                            .height(60.dp)
                            .clickable {
                                isPlaying = !isPlaying
                            })

                    Image(painter = painterResource(id = R.drawable.forward),
                        contentDescription = stringResource(R.string.forward),
                        modifier = Modifier
                            .height(40.dp)
                            .clip(CircleShape)
                            .height(40.dp)
                            .clickable {
                                if (Utils.playerList.isNotEmpty()) {
                                    if (currentIndex == Utils.playerList.size - 1) {
                                        currentIndex = 0
                                    } else {
                                        currentIndex += 1
                                    }

                                    mediaModel = Utils.playerList[currentIndex]
                                    mediaModel?.let { currentSong ->
                                        isPlaying = true
                                        playerViewModel.playMusic(currentSong)
                                    }
                                }
                            })
                }
            }
        }
    }

}