package com.civicsready

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.civicsready.ui.navigation.CivicsNavGraph
import com.civicsready.ui.theme.CivicsReadyTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CivicsReadyTheme {
                val navController = rememberNavController()
                CivicsNavGraph(navController = navController)
            }
        }
    }
}
