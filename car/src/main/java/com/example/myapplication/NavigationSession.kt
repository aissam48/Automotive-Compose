package com.example.myapplication

import androidx.car.app.Screen
import androidx.car.app.Session
import androidx.car.app.model.Template
import androidx.car.app.model.MessageTemplate

class NavigationSession : Session() {
    override fun onCreateScreen(intent: android.content.Intent): Screen {
        return NavigationScreen(carContext)
    }

}