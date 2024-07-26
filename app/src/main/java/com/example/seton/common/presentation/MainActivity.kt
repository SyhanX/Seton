package com.example.seton.common.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.seton.common.presentation.theme.SetonTheme
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "main_activity"

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SetonTheme {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    AppScreen()
                }
            }
        }
    }
}