package com.example.mp3playerviacompose.presentation.screens.Home

import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.mp3playerviacompose.R
import com.example.mp3playerviacompose.presentation.navigation.NavigationItems
import com.example.mp3playerviacompose.presentation.screens.Home.viewModels.HomeViewModel
import com.example.mp3playerviacompose.presentation.screens.albums.AudioAlbums
import com.example.mp3playerviacompose.presentation.screens.artists.Artists
import com.example.mp3playerviacompose.presentation.screens.folders.AudioFolders
import com.example.mp3playerviacompose.presentation.screens.songs.SongsScreen
import com.example.mp3playerviacompose.domain.utils.Constants
import com.example.mp3playerviacompose.domain.utils.Constants.DIVIDER_HEIGHT
import com.example.mp3playerviacompose.domain.utils.Constants.DIVIDER_PADDING
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun HomeScreen(
    onNavigation: (String) -> Unit,
    onBackPressed: () -> Unit
) {

    val tabs = listOf("Song", "Artist", "Album", "Folder")
    val pagerState = rememberPagerState(pageCount = {
        tabs.size
    })
    val homeViewModel: HomeViewModel = hiltViewModel()

    val coroutineScope = rememberCoroutineScope()
    Scaffold{ innerPadding ->
        Column(modifier = Modifier.consumeWindowInsets(innerPadding)) {

            Text(text = "Music Player",
                textAlign = TextAlign.Start,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = Constants.MEDIUM_PADDING, top = Constants.LARGE_PADDING_1))

            TabRow(selectedTabIndex = pagerState.currentPage, indicator = { tabPositions ->
                Box(
                    modifier = Modifier
                        .tabIndicatorOffset(tabPositions[pagerState.currentPage])
                        .height(DIVIDER_HEIGHT)
                        .clip(RoundedCornerShape(Constants.MEDIUM_PADDING))
                        .padding(horizontal = DIVIDER_PADDING)
                        .background(
                            color = colorResource(id = R.color.sky_blue),
                            shape = RoundedCornerShape(Constants.MEDIUM_PADDING)
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

            HorizontalPager(state = pagerState, modifier = Modifier.fillMaxSize()) { page ->
               when(page){
                   0 -> {
                       SongsScreen(
                       onNavigation = {onNavigation(NavigationItems.Player.route + "/${Uri.encode(homeViewModel.selectedMediaModel?.path)}")},
                       onBackPressed = {}
                   )}


                   1 -> { Artists {onNavigation(NavigationItems.MusicDetails.route + "/${homeViewModel.artistName}/${homeViewModel.albumName}/${homeViewModel.folderName}")}}
                   2 -> { AudioAlbums{onNavigation(NavigationItems.MusicDetails.route + "/${homeViewModel.artistName}/${homeViewModel.albumName}/${homeViewModel.folderName}")}}
                   3 -> { AudioFolders{onNavigation(NavigationItems.MusicDetails.route + "/${homeViewModel.artistName}/${homeViewModel.albumName}/${homeViewModel.folderName}")}}
               }
            }
        }
    }
}