package com.example.myapplication

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
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
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.dynamite.DynamiteModule
import com.google.android.gms.dynamite.DynamiteModule.PREFER_HIGHEST_OR_LOCAL_VERSION
import com.google.android.gms.dynamite.DynamiteModule.PREFER_HIGHEST_OR_REMOTE_VERSION
import com.google.android.gms.dynamite.DynamiteModule.PREFER_LOCAL
import com.google.android.gms.dynamite.DynamiteModule.PREFER_REMOTE
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.OnMapsSdkInitializedCallback

class MainActivity : AppCompatActivity(), OnMapsSdkInitializedCallback {

    private val viewModel: MainViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        tryLoadMapsModuleDirectly(this)
        val availability = GoogleApiAvailability.getInstance()
        val resultCode = availability.isGooglePlayServicesAvailable(this)

        if (resultCode != ConnectionResult.SUCCESS) {
            Log.e("Maps", "Google Play Services not available: $resultCode")
            if (availability.isUserResolvableError(resultCode)) {
                availability.getErrorDialog(this, resultCode, 9000)?.show()
            }
        } else {
            Log.d("Maps", "Google Play Services available")
        }

        // Force LATEST renderer instead of LEGACY
        try {
            MapsInitializer.initialize(
                applicationContext,
                MapsInitializer.Renderer.LATEST,
                this
            )
        } catch (e: Exception) {
            Log.e("Maps", "MapsInitializer error", e)
        }

        viewModel.initCarApi(this)
        setContent {

            Surface(
                modifier = Modifier.fillMaxSize(),
                color = Color.White
            ) {
                val uiState by viewModel.uiState.collectAsState()
                viewModel.getRealCarInfo()
                viewModel.listenToSpeed()

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
        val hasPlayServices = checkGooglePlayServices(this)
    }

    fun checkGooglePlayServices(context: Context): Boolean {
        val googleApiAvailability = GoogleApiAvailability.getInstance()
        val resultCode = googleApiAvailability.isGooglePlayServicesAvailable(context)
        val text = when (resultCode) {
            ConnectionResult.SERVICE_MISSING -> "Google Play Services missing"
            ConnectionResult.SERVICE_UPDATING -> "Google Play Services updating"
            ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED -> "Update Google Play Services"
            ConnectionResult.SERVICE_DISABLED -> "Google Play Services disabled"
            ConnectionResult.SERVICE_INVALID -> "Google Play Services invalid"
            else -> "Google Play Services unavailable: $resultCode"
        }
        return resultCode == ConnectionResult.SUCCESS
    }

    // Usage in your Activity or Composable

    override fun onMapsSdkInitialized(renderer: MapsInitializer.Renderer) {
        Log.d("Maps", "Maps initialized successfully: $renderer")
    }

    private fun tryLoadMapsModuleDirectly(context: Context) {
        val moduleId = "com.google.android.gms.maps_dynamite"

        // Check versions first
        try {
            val localVersion = DynamiteModule.getLocalVersion(context, moduleId)
            val remoteVersion = DynamiteModule.getRemoteVersion(context, moduleId)
            Log.d("Maps", "Local version: $localVersion, Remote version: $remoteVersion")
        } catch (e: Exception) {
            Log.e("Maps", "Error checking versions", e)
        }

        // Try different loading strategies
        val strategies = listOf(
            "PREFER_REMOTE" to PREFER_REMOTE,
            "PREFER_LOCAL" to PREFER_LOCAL,
            "PREFER_HIGHEST_OR_REMOTE" to PREFER_HIGHEST_OR_REMOTE_VERSION,
            "PREFER_HIGHEST_OR_LOCAL" to PREFER_HIGHEST_OR_LOCAL_VERSION
        )

        for ((name, strategy) in strategies) {
            try {
                Log.d("Maps", "Trying to load with strategy: $name")
                val module = DynamiteModule.load(context, strategy, moduleId)
                Log.d("Maps", "✓ SUCCESS! Module loaded with $name strategy")
                Log.d("Maps", "Module context: ${module.moduleContext}")
                return // Success!
            } catch (e: DynamiteModule.LoadingException) {
                Log.e("Maps", "✗ Failed with $name: ${e.message}")
            } catch (e: Exception) {
                Log.e("Maps", "✗ Unexpected error with $name", e)
            }
        }

        Log.e("Maps", "All loading strategies failed")
    }
}