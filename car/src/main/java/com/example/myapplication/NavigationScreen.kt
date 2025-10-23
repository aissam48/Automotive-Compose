package com.example.myapplication

import androidx.car.app.CarContext
import androidx.car.app.Screen
import androidx.car.app.model.Action
import androidx.car.app.model.ActionStrip
import androidx.car.app.model.CarIcon
import androidx.car.app.model.CarLocation
import androidx.car.app.model.Pane
import androidx.car.app.model.PaneTemplate
import androidx.car.app.model.Place
import androidx.car.app.model.Row
import androidx.car.app.model.Template
import androidx.car.app.navigation.model.MapController
import androidx.car.app.navigation.model.MapWithContentTemplate
import androidx.core.graphics.drawable.IconCompat

class NavigationScreen(carContext: CarContext) : Screen(carContext) {

    override fun onGetTemplate(): Template {
        // Create action strip
        val actionStrip = ActionStrip.Builder()
            .addAction(
                Action.Builder()
                    .setIcon(
                        CarIcon.Builder(
                            IconCompat.createWithResource(
                                carContext,
                                android.R.drawable.ic_menu_mylocation
                            )
                        ).build()
                    )
                    .setOnClickListener {
                        // Handle location action
                    }
                    .build()
            )
            .build()

        // Create map controller
        val mapController = MapController.Builder()
            .setMapActionStrip(actionStrip)
            .build()

        // Create content pane - REMOVED setLoading(true)
        val contentPane = Pane.Builder()
            .addRow(
                Row.Builder()
                    .setTitle("Navigate to San Francisco")
                    .addText("Tap to start navigation")
                    .build()
            )
            .addAction(
                Action.Builder()
                    .setTitle("Start Navigation")
                    .setOnClickListener {
                        startNavigation()
                    }
                    .build()
            )
            .build()

        return MapWithContentTemplate.Builder()
            .setMapController(mapController)
            .setContentTemplate(
                PaneTemplate.Builder(contentPane)
                    .setActionStrip(actionStrip)
                    .build()
            )
            .build()
    }

    private fun startNavigation() {
        // Start navigation logic
        val destination = Place.Builder(
            CarLocation.create(37.7749, -122.4194) // San Francisco
        ).build()

        // You would typically use NavigationManager here
        // For now, just show a toast or handle the navigation start
    }
}