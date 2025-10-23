package com.example.myapplication


import android.content.Intent
import android.content.ServiceConnection
import androidx.car.app.CarAppService
import androidx.car.app.Session
import androidx.car.app.validation.HostValidator

class MyCarAppService : CarAppService() {
    override fun onCreateSession(): Session = NavigationSession()

    override fun createHostValidator(): HostValidator {
        return HostValidator.ALLOW_ALL_HOSTS_VALIDATOR
    }

    override fun bindService(
        service: Intent,
        conn: ServiceConnection,
        flags: BindServiceFlags
    ): Boolean {
        return super.bindService(service, conn, flags)
    }

}