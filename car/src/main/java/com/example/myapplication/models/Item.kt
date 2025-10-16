package com.example.myapplication.models

data class Item(
    val id: Int,
    val title: String,
    val description: String,
    val icon: String = "📱" // Using emoji for simplicity
)
