package com.linkjf.spacex.launch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.linkjf.spacex.launch.navigation.SpaceXNavigation
import com.linkjf.spacex.launch.ui.theme.SpaceXLaunchTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SpaceXLaunchTheme {
                SpaceXApp()
            }
        }
    }
}

@Composable
fun SpaceXApp() {
    val navController = rememberNavController()

    Scaffold(modifier = Modifier.fillMaxSize()) { paddingValues ->
        SpaceXNavigation(
            navController = navController,
            modifier = Modifier.padding(paddingValues),
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SpaceXAppPreview() {
    SpaceXLaunchTheme {
        SpaceXApp()
    }
}
