package com.raen.optidroid.presentation.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.CompositionLocalProvider
import com.raen.optidroid.presentation.navigation.AppNavigator
import com.raen.optidroid.presentation.navigation.interfaces.NavigationManager
import com.raen.optidroid.presentation.tools.LocalWindowSizeClass
import com.raen.optidroid.presentation.ui.theme.AppBoosterTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var navigationManager: NavigationManager

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val windowSizeClass = calculateWindowSizeClass(this)
            AppBoosterTheme {
                CompositionLocalProvider(LocalWindowSizeClass provides windowSizeClass) {
                    AppNavigator(navigationManager = navigationManager)
                }
            }
        }
    }
}
