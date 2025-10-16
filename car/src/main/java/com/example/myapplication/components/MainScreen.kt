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
        }
    ) { paddingValues ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(64.dp)
                            .align(Alignment.Center)
                    )
                }

                items.isEmpty() -> {
                    Text(
                        text = "No items available",
                        modifier = Modifier.align(Alignment.Center),
                        style = MaterialTheme.typography.bodyLarge,
                        fontSize = 20.sp
                    )
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(vertical = 8.dp)
                    ) {
                        items(items = items, key = { it.id }) { item ->
                            ItemCard(
                                item = item,
                                onClick = { onItemClick(item) }
                            )
                        }
                    }
                }
            }
        }
    }

    // Dialog for selected item
    if (selectedItem != null) {
        AlertDialog(
            onDismissRequest = onDismissDialog,
            title = {
                Text(
                    text = selectedItem.title,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column {
                    Text(
                        text = selectedItem.icon,
                        fontSize = 64.sp,
                        modifier = Modifier.padding(vertical = 16.dp)
                    )
                    Text(
                        text = selectedItem.description,
                        fontSize = 18.sp
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = onDismissDialog,
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(
                        text = "Open",
                        fontSize = 20.sp
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = onDismissDialog,
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(
                        text = "Cancel",
                        fontSize = 20.sp
                    )
                }
            }
        )
    }
}