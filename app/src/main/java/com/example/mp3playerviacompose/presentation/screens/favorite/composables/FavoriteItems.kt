package com.example.mp3playerviacompose.presentation.screens.favorite.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.mp3playerviacompose.R
import com.example.mp3playerviacompose.domain.models.MediaModel
import com.example.mp3playerviacompose.domain.utils.Constants

//@ThemePreviews
@Composable
fun FavoriteItems(isFavorite: Boolean, onItemsClicked:(MediaModel)-> Unit, mediaModel: MediaModel,onFavoriteClicked:(MediaModel, Boolean)-> Unit ){
    val context = LocalContext.current
    var isFavoriteByUser by remember {
        mutableStateOf(isFavorite)
    }

    Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    onItemsClicked(mediaModel)
                }
                .height(60.dp)
                .padding(horizontal = Constants.MEDIUM_PADDING)
        ) {

            mediaModel.thumbnail?.let {
                val imageRequest = remember {
                    ImageRequest.Builder(context)
                        .data(mediaModel.thumbnail)
                        .size(50)
                        .memoryCachePolicy(CachePolicy.ENABLED)
                        .diskCachePolicy(CachePolicy.ENABLED)
                        .crossfade(true)
                        .build()
                }

                AsyncImage(
                    model = imageRequest,
                    placeholder = painterResource(id = R.drawable.music),
                    error = painterResource(id = R.drawable.music),
                    contentScale = ContentScale.Crop,
                    contentDescription = stringResource(id = R.string.audio_icon),
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(50.dp)
                )
            }

            Spacer(modifier = Modifier.padding(start = Constants.MEDIUM_PADDING))

            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    .weight(.9f)
                    .padding(end = Constants.MEDIUM_PADDING)) {
                Text(
                    text = mediaModel.name,
                    fontSize = 15.sp,
                    maxLines = 1,
                    fontWeight = FontWeight.Medium,
                    style = TextStyle(color = MaterialTheme.colorScheme.onBackground)
                )

                Text(
                    text = mediaModel.artist,
                    fontSize = 11.sp,
                    maxLines = 1,
                    fontWeight = FontWeight.Normal,
                    style = TextStyle(color = MaterialTheme.colorScheme.onBackground)
                )

            }


            Text(
                text = mediaModel.formattedDuration,
                fontSize = 10.sp,
                maxLines = 1,
                fontWeight = FontWeight.Normal,
                modifier = Modifier.weight(.19f),
                style = TextStyle(color = MaterialTheme.colorScheme.onBackground)
            )


            val drawable = if(isFavoriteByUser){
                R.drawable.favorite
            }else{
                R.drawable.unfavorite
            }

            Image(painter = painterResource(id = drawable),
                contentDescription = stringResource(R.string.favorite),
                modifier = Modifier
                    .weight(.19f)
                    .clip(CircleShape)
                    .clickable {
                        isFavoriteByUser = !isFavoriteByUser
                        onFavoriteClicked(mediaModel, isFavoriteByUser)
                    })


        }

        Divider(modifier = Modifier
            .height(1.dp)
            .background(Color.LightGray)
            .fillMaxWidth())
    }
}