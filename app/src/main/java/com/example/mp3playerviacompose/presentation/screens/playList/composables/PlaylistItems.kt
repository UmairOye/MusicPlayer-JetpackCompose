package com.example.mp3playerviacompose.presentation.screens.playList.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mp3playerviacompose.R
import com.example.mp3playerviacompose.domain.models.PlayListEntity
import com.example.mp3playerviacompose.domain.utils.Constants
import com.example.mp3playerviacompose.domain.utils.Constants.SMALL_PADDING
import com.example.mp3playerviacompose.domain.utils.formatDuration
import com.example.mp3playerviacompose.presentation.composable.CustomCheckBox

@Composable
fun PlayListItems(
    wantToAddSongs: Boolean,
    playListEntity: PlayListEntity,
    onDelete: (PlayListEntity) -> Unit,
    onAddTracks: () -> Unit,
    playListChecked:(PlayListEntity)-> Unit,
    playListUnchecked:(PlayListEntity)-> Unit,
    onClicked:(String)-> Unit){


    val isExpanded = remember {
        mutableStateOf(false)
    }

    val isChecked = remember {
        mutableStateOf(false)
    }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {

        Row(verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .padding(horizontal = Constants.MEDIUM_PADDING)
                .clickable {
                    playListEntity.playListName?.let { onClicked(it) }
                }) {


            Image(
                painter = painterResource(id = R.drawable.playlist),
                contentDescription = stringResource(id = R.string.audio_icon),
                modifier = Modifier
                    .clip(CircleShape)
                    .size(50.dp),
                contentScale = ContentScale.Crop
            )


            Column(
                verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.Start,
                modifier = Modifier.weight(1f)
            ) {
                    Text(
                        text = playListEntity.playListName!!,
                        textAlign = TextAlign.Start,
                        fontSize = 15.sp,
                        maxLines = 1,
                        fontWeight = FontWeight.SemiBold,
                        style = TextStyle(color = MaterialTheme.colorScheme.onBackground),
                        modifier = Modifier.padding(horizontal = Constants.MEDIUM_PADDING)
                    )

                Text(
                    text = "${playListEntity.songsCount} Track",
                    textAlign = TextAlign.Start,
                    fontSize = 12.sp,
                    maxLines = 1,
                    fontWeight = FontWeight.Normal,
                    style = TextStyle(color = MaterialTheme.colorScheme.onBackground),
                    modifier = Modifier.padding(
                        horizontal = Constants.MEDIUM_PADDING,
                        vertical = Constants.SMALL_PADDING
                    )
                )
            }


            playListEntity.songDuration?.formatDuration()?.let {
                Text(text = it,
                    textAlign = TextAlign.Start,
                    fontSize = 12.sp,
                    style = TextStyle(color = MaterialTheme.colorScheme.onBackground),
                    modifier = Modifier.weight(.2f).padding(end = SMALL_PADDING))
            }

            if (wantToAddSongs) {
                CustomCheckBox(playListEntity.isChecked) { isChecked ->
                    playListEntity.isChecked = isChecked
                    if (isChecked) {
                        playListChecked(playListEntity)
                    }else{
                        playListUnchecked(playListEntity)
                    }
                }
            } else {
                Icon(imageVector = Icons.Default.MoreVert,
                    contentDescription = stringResource(id = R.string.menu_button),
                    modifier = Modifier
                        .weight(.1f)
                        .clickable {
                            isExpanded.value = true
                        })
            }

            DropdownMenu(
                expanded = isExpanded.value,
                onDismissRequest = { isExpanded.value = false },
                offset = DpOffset(x = (-20).dp, y = 0.dp),
                modifier = Modifier.background(color = MaterialTheme.colorScheme.background)
            ) {
                DropdownMenuItem(
                    text = { Text(stringResource(R.string.delete)) },
                    onClick = {
                        isExpanded.value = false
                        onDelete(playListEntity)
                    }
                )

                Divider()
                DropdownMenuItem(
                    text = { Text(stringResource(R.string.add_tracks)) },
                    onClick = {
                        isExpanded.value = false
                        onAddTracks()
                    }
                )
            }
        }

        Divider(
            modifier = Modifier
                .height(1.dp)
                .background(Color.LightGray)
                .fillMaxWidth()
        )
    }

}


@Composable
@Preview(showSystemUi = true)
fun ShowPlayListItems(){
    PlayListItems(
        wantToAddSongs = false,
        playListEntity = PlayListEntity(),
        onDelete = {},
        onAddTracks = { /*TODO*/ },
        onClicked = {},
        playListChecked = {},
        playListUnchecked = {}
    )
}