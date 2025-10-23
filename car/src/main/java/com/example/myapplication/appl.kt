package com.example.myapplication

import android.app.Application
import android.util.Log
import android.widget.Toast
import com.google.android.gms.maps.MapsInitializer

class Appl: Application() {
    override fun onCreate() {
        super.onCreate()
        try {
            MapsInitializer.initialize(applicationContext)
            MapsInitializer.initialize(this, MapsInitializer.Renderer.LATEST, null)

            Toast.makeText(this, "Maps available on this device", Toast.LENGTH_LONG).show()
            Log.d("Maps", "Maps available on this device")

        } catch (e: Exception) {
            Log.d("Maps", "Maps niy available on this device")

            Toast.makeText(this, "Maps not available on this device", Toast.LENGTH_LONG).show()
        }
    }
}