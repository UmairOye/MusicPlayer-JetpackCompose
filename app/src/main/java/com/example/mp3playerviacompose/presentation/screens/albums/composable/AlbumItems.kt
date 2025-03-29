package com.example.mp3playerviacompose.presentation.screens.albums.composable

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
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mp3playerviacompose.R
import com.example.mp3playerviacompose.domain.models.Album
import com.example.mp3playerviacompose.domain.utils.Constants

@Composable
//@Preview(showSystemUi = true)
fun AlbumsList(albumModel: Album, onClick:(Album)-> Unit){
    Column (verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()){

        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Start,modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .padding(horizontal = Constants.MEDIUM_PADDING)
            .clickable {
                onClick(albumModel)
            }) {


            Image(painter = painterResource(id = R.drawable.albums),
                contentDescription = stringResource(id = R.string.audio_icon),
                modifier = Modifier
                    .clip(CircleShape)
                    .size(50.dp),
                contentScale = ContentScale.Crop)


            Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.Start,
                modifier = Modifier.weight(1f)
                )
                {
                Text(text = albumModel.albumName,
                    textAlign = TextAlign.Start,
                    fontSize = 15.sp,
                    maxLines = 1,
                    fontWeight = FontWeight.SemiBold,
                    style = TextStyle(color = MaterialTheme.colorScheme.onBackground),
                    modifier = Modifier.padding(horizontal = Constants.MEDIUM_PADDING))

                Text(text = "${albumModel.numberOfSongs} Track",
                    textAlign = TextAlign.Start,
                    fontSize = 12.sp,
                    maxLines = 1,
                    fontWeight = FontWeight.Normal,
                    style = TextStyle(color = MaterialTheme.colorScheme.onBackground),
                    modifier = Modifier.padding(horizontal = Constants.MEDIUM_PADDING, vertical = Constants.SMALL_PADDING))
            }

            Image(painter = painterResource(id = R.drawable.forward_icon),
                contentDescription = stringResource(R.string.forward_icon),
                modifier = Modifier.weight(.1f)
            )

        }

        Divider(modifier = Modifier
            .height(1.dp)
            .background(Color.LightGray)
            .fillMaxWidth())
    }


}