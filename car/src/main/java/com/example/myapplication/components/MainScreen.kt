package com.example.myapplication.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.models.Item
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapEffect

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    items: List<Item>,
    isLoading: Boolean,
    selectedItem: Item?,
    onItemClick: (Item) -> Unit,
    onDismissDialog: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Automotive Dashboard",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        modifier = modifier
            .fillMaxSize()
    ) { paddingValues ->


        GoogleMap(modifier = Modifier.fillMaxSize()) {

            MapEffect {map->

            }

        }
    }
}