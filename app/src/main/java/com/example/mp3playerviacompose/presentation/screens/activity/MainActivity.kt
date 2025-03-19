package com.example.mp3playerviacompose.presentation.screens.activity

import android.graphics.Color.TRANSPARENT
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.mp3playerviacompose.presentation.navigation.AppNavHost
import com.example.mp3playerviacompose.presentation.theme.Mp3PlayerViaComposeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var navController: NavHostController
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val lightTransparentStyle = SystemBarStyle.light(
            scrim = TRANSPARENT,
            darkScrim = TRANSPARENT
        )
        enableEdgeToEdge(
            statusBarStyle = lightTransparentStyle,
            navigationBarStyle = lightTransparentStyle
        )

        setContent {
            Mp3PlayerViaComposeTheme {
                navController = rememberNavController()
                AppNavHost(navController = navController)
            }
        }
    }
}

