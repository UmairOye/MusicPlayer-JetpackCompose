package com.example.mp3playerviacompose.presentation.screens.favorite.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.mp3playerviacompose.R
import com.example.mp3playerviacompose.domain.models.RequestState
import com.example.mp3playerviacompose.domain.utils.Constants.LARGE_PADDING_1
import com.example.mp3playerviacompose.presentation.composable.BackButton
import com.example.mp3playerviacompose.presentation.composable.NothingFound
import com.example.mp3playerviacompose.presentation.screens.Home.viewModels.HomeViewModel
import com.example.mp3playerviacompose.presentation.screens.favorite.composables.FavoriteItems

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun FavoriteScreen(
    homeViewModel: HomeViewModel,
    onNavigation: (String) -> Unit,
    onBackPressed: () -> Unit,
) {

    val favoriteState = homeViewModel.favorites.collectAsStateWithLifecycle()
    Scaffold { innerPadding ->
        Column(modifier = Modifier.consumeWindowInsets(innerPadding)) {
            BackButton(
                heading = stringResource(R.string.favorites),
                modifier = Modifier.padding(top = LARGE_PADDING_1),
                color = colorResource(id = R.color.back_button_color)
            ) { onBackPressed() }


            when (favoriteState.value) {
                is RequestState.Error -> {}
                RequestState.Idle -> {}
                RequestState.Loading -> {}
                is RequestState.Success -> {
                    val favoriteList = (favoriteState.value as RequestState.Success).data
                    if (favoriteList.isEmpty()) {
                        NothingFound()
                    } else {
                        LazyColumn(modifier = Modifier.fillMaxWidth()) {
                            items(favoriteList, key = { it.mediaId }) { mediaModel ->
                                FavoriteItems(
                                    isFavorite = mediaModel.isFavorite,
                                    onItemsClicked = {},
                                    mediaModel = mediaModel,
                                    onFavoriteClicked = { model, _ ->
                                        homeViewModel.removeFavorites(model)
                                    })
                            }
                        }
                    }
                }
            }
        }
    }

}