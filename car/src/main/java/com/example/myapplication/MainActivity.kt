package com.example.myapplication

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.myapplication.components.MainScreen

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.initCarApi(this)
        setContent {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = Color.White
            ) {
                val uiState by viewModel.uiState.collectAsState()
viewModel.getRealCarInfo()
                MainScreen(
                    items = uiState.items,
                    isLoading = uiState.isLoading,
                    selectedItem = uiState.selectedItem,
                    onItemClick = { item ->
                        viewModel.onItemClick(item)
                    },
                    onDismissDialog = {
                        viewModel.clearSelection()
                    }
                )
            }
        }

    }
}