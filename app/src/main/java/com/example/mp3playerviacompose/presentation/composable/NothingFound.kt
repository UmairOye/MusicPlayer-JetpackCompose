package com.example.mp3playerviacompose.presentation.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mp3playerviacompose.R
import com.example.mp3playerviacompose.domain.utils.Constants

@ThemePreviews
@Composable
fun NothingFound(placeHolder: String = stringResource(id = R.string.no_favorites_found)){
    Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()) {
        Image(painter = painterResource(id = R.drawable.nothing_found), contentDescription = stringResource(R.string.nothing_found),
            modifier = Modifier.size(200.dp)
        )

        Spacer(modifier = Modifier.padding(top = Constants.LARGE_PADDING_1))
        Text(text = placeHolder,
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium,
            style = TextStyle(MaterialTheme.colorScheme.onBackground)
        )

    }
}