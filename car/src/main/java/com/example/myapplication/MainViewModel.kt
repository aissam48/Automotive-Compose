package com.example.myapplication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.models.Item
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import android.car.*
import android.car.hardware.CarPropertyValue
import android.car.hardware.property.*
import android.content.Context
import android.util.Log
import java.util.concurrent.Executors

data class MainUiState(
    val items: List<Item> = emptyList(),
    val isLoading: Boolean = false,
    val selectedItem: Item? = null
)

class MainViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()
    private val executor = Executors.newSingleThreadExecutor()

    init {
        loadItems()
    }

    private fun loadItems() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            // Simulate data loading
            val items = listOf(
                Item(1, "Navigation", "Open maps and navigation", "🗺️"),
                Item(2, "Music Player", "Listen to your favorite songs", "🎵"),
                Item(3, "Phone", "Make calls and check messages", "📞"),
                Item(4, "Settings", "Adjust vehicle settings", "⚙️"),
                Item(5, "Climate", "Control temperature and air", "❄️"),
                Item(6, "Radio", "Listen to FM/AM radio", "📻"),
                Item(7, "Voice Assistant", "Use voice commands", "🎤"),
                Item(8, "Vehicle Info", "Check vehicle status", "🚗"),
                Item(9, "Media", "Browse media library", "🎬"),
                Item(10, "Contacts", "View your contacts", "👤")
            )

            _uiState.value = _uiState.value.copy(
                items = items,
                isLoading = false
            )
        }
    }

    fun onItemClick(item: Item) {
        _uiState.value = _uiState.value.copy(selectedItem = item)
    }

    fun clearSelection() {
        _uiState.value = _uiState.value.copy(selectedItem = null)
    }

   private var carPropertyManager: CarPropertyManager? = null

    fun initCarApi(context: Context) {
        try {
            val car = Car.createCar(context)
            carPropertyManager = car.getCarManager(Car.PROPERTY_SERVICE) as CarPropertyManager
        } catch (e: Exception) {
            Log.e("CarDataManager", "Failed to initialize Car API", e)
        }
    }

    fun getRealCarInfo() {
        val fuelLevel = carPropertyManager?.getFloatProperty(
            VehiclePropertyIds.FUEL_LEVEL,
            VehicleAreaType.VEHICLE_AREA_TYPE_GLOBAL
        ) ?: 0f

        println("getRealCarInfo fuelLevel = $fuelLevel")
        val batteryLevel = carPropertyManager?.getFloatProperty(
            VehiclePropertyIds.EV_BATTERY_LEVEL,
            VehicleAreaType.VEHICLE_AREA_TYPE_GLOBAL
        ) ?: 0f
        println("getRealCarInfo batteryLevel = $batteryLevel")

    }

    fun listenToSpeed(){
        val propertyList = carPropertyManager?.propertyList
        propertyList?.forEach { config ->
            Log.d("CarProps", "Property ID: ${config.propertyId}, name: ${config.toString()}")
        }

        val callback = object : CarPropertyManager.CarPropertyEventCallback {
            override fun onChangeEvent(value: CarPropertyValue<*>) {
                if (value.propertyId == VehiclePropertyIds.PERF_VEHICLE_SPEED) {
                    // Speed is in m/s, convert to km/h
                    val speedMps = (value.value as? Float) ?: 0f
                    println("Vehicle speed ${speedMps * 3.6f}")
                }
            }

            override fun onErrorEvent(propId: Int, zone: Int) {
                Log.e("CarSpeed", "Error reading speed")
            }
        }

        try {
            carPropertyManager?.registerCallback(
                callback,
                VehiclePropertyIds.PERF_VEHICLE_SPEED,
                CarPropertyManager.SENSOR_RATE_NORMAL
            )
        } catch (e: Exception) {
            Log.e("CarSpeed", "Error: ${e.message}")
        }
    }


}