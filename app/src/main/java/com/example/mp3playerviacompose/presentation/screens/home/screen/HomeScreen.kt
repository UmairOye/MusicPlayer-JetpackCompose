package com.example.mp3playerviacompose.presentation.screens.Home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.example.mp3playerviacompose.R
import com.example.mp3playerviacompose.presentation.navigation.NavigationItems
import com.example.mp3playerviacompose.presentation.screens.Home.viewModels.HomeViewModel
import com.example.mp3playerviacompose.presentation.screens.albums.screen.AudioAlbums
import com.example.mp3playerviacompose.presentation.screens.artists.screen.Artists
import com.example.mp3playerviacompose.presentation.screens.folders.screen.AudioFolders
import com.example.mp3playerviacompose.presentation.screens.songs.screen.SongsScreen
import com.example.mp3playerviacompose.domain.utils.Constants
import com.example.mp3playerviacompose.domain.utils.Constants.DIVIDER_HEIGHT
import com.example.mp3playerviacompose.domain.utils.Constants.DIVIDER_PADDING
import com.example.mp3playerviacompose.domain.utils.Constants.MEDIUM_PADDING
import com.example.mp3playerviacompose.domain.utils.Utils
import com.example.mp3playerviacompose.presentation.screens.albums.albumViewModel.AlbumViewModel
import com.example.mp3playerviacompose.presentation.screens.artists.artistViewModel.ArtistViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.abs

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel,
    artistViewModel: ArtistViewModel,
    albumViewModel: AlbumViewModel,
    onNavigation: (String) -> Unit
) {

    val tabs = listOf("Song", "Artist", "Album", "Folder")
    val pagerState = rememberPagerState(pageCount = {
        tabs.size
    })

    val beyondBoundsPageCount = 1
    val coroutineScope = rememberCoroutineScope()
    Scaffold{ innerPadding ->
        Column(modifier = Modifier
            .consumeWindowInsets(innerPadding)
            .fillMaxWidth()) {

            Row (verticalAlignment = Alignment.CenterVertically,
               horizontalArrangement = Arrangement.Center,
               modifier = Modifier
                   .fillMaxWidth()
                   .padding(
                       top = Constants.LARGE_PADDING_1,
                       start = MEDIUM_PADDING,
                       end = MEDIUM_PADDING
                   )){


               Text(text = stringResource(R.string.music_player),
                   textAlign = TextAlign.Start,
                   fontSize = 24.sp,
                   fontWeight = FontWeight.Bold,
                   modifier = Modifier.weight(1f)
               )


               Image(painter = painterResource(id = R.drawable.favorite_icon), contentDescription = stringResource(id = R.string.favorite), modifier = Modifier
                   .weight(.13f).clickable {
                       onNavigation(NavigationItems.Favorites.route)
                   })

               Image(painter = painterResource(id = R.drawable.playlist), contentDescription = stringResource(R.string.playlist), modifier = Modifier
                   .weight(.13f)
                   .clickable {
                       onNavigation(NavigationItems.PlayList.route)
                   })
           }

            TabRow(selectedTabIndex = pagerState.currentPage, indicator = { tabPositions ->
                Box(
                    modifier = Modifier
                        .tabIndicatorOffset(tabPositions[pagerState.currentPage])
                        .height(DIVIDER_HEIGHT)
                        .clip(RoundedCornerShape(MEDIUM_PADDING))
                        .padding(horizontal = DIVIDER_PADDING)
                        .background(
                            color = colorResource(id = R.color.sky_blue),
                            shape = RoundedCornerShape(MEDIUM_PADDING)
                        )
                )
            } ) {
                tabs.forEachIndexed { index, title ->
                    Tab(text = { Text(title,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold
                    ) },
                        selected = pagerState.currentPage == index,
                        onClick = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        },

                        unselectedContentColor = Color.Gray,
                        selectedContentColor = colorResource(id = R.color.sky_blue)
                    )
                }
            }

            HorizontalPager(state = pagerState, modifier = Modifier.fillMaxSize(),
                beyondViewportPageCount = beyondBoundsPageCount) { pages ->

                val page by rememberUpdatedState(newValue = pages)
                val authorizedPage = {
                    abs(pagerState.settledPage - page) <= beyondBoundsPageCount
                }


                val authorizedTiming by produceState(initialValue = false) {
                    while (pagerState.isScrollInProgress) delay(50)
                    if (abs(pagerState.settledPage - page) > 0) {
                        delay(1000)
                        while (pagerState.isScrollInProgress) delay(50)
                    }
                    value = true
                }

                val showContent by remember {
                    derivedStateOf {
                        authorizedPage() && authorizedTiming
                    }
                }

               when(pages){
                   0 -> {

                       if (showContent) {
                           SongsScreen(
                               homeViewModel,
                               onNavigation = {route -> onNavigation(route)},
                               onBackPressed = {}
                           )
                       } else {
                           CircularProgressIndicator()
                       }
                       }
                   1 -> {
                       if (showContent) {
                           Artists(onNavigateToAudioList = {route ->
                               onNavigation(route) },
                               homeViewModel = homeViewModel, artistViewModel = artistViewModel
                           )
                       } else {
                           CircularProgressIndicator()
                       } }
                   2 -> {
                       if (showContent) {
                           AudioAlbums(sharedViewModel = homeViewModel, albumViewModel = albumViewModel){route -> onNavigation(route)}
                       } else {
                           CircularProgressIndicator()
                       }

                   }
                   3 -> {
                       if (showContent) {
                           AudioFolders{route -> onNavigation(route)}
                       } else {
                           CircularProgressIndicator()
                       }
                   }
               }
            }
        }
    }
}