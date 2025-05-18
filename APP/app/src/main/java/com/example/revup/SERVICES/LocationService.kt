package com.example.revup.SERVICES

import android.app.*
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.revup.ACTIVITIES.RecordRouteActivity
import com.example.revup.R
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng

class LocationService : Service() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private lateinit var locationRequest: LocationRequest
    private var startTime: Long = 0L
    private var endTime: Long = 0L

    companion object {
        var routePoints = mutableListOf<LatLng>()
        var isRunning = false
        var routeDurationInMillis: Long = 0L
    }

    override fun onCreate() {
        super.onCreate()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        locationRequest = LocationRequest.create().apply {
            interval = 5000
            fastestInterval = 2000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                for (location in result.locations) {
                    val point = LatLng(location.latitude, location.longitude)
                    if(routePoints.isEmpty()){
                        routePoints.add(point)
                        Log.d("LocationService", "Location: $point")
                        val intent = Intent("com.example.revup.LOCATION_UPDATE")
                        intent.putExtra("lat", location.latitude)
                        intent.putExtra("lng", location.longitude)
                        sendBroadcast(intent)
                    }else{
                        if(point != routePoints.last()){
                            routePoints.add(point)
                            Log.d("LocationService", "Location: $point")
                            val intent = Intent("com.example.revup.LOCATION_UPDATE")
                            intent.putExtra("lat", location.latitude)
                            intent.putExtra("lng", location.longitude)
                            sendBroadcast(intent)
                        }
                    }
                }
            }
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startTime = System.currentTimeMillis()
        isRunning = true
        startForeground(1, createNotification())
        startLocationUpdates()
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        endTime = System.currentTimeMillis()
        routeDurationInMillis = endTime - startTime
        isRunning = false
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun startLocationUpdates() {
        if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, mainLooper)
        }
    }

    private fun createNotification(): Notification {
        val channelId = "location_channel"
        val channel = NotificationChannel(
            channelId,
            "Recording on background",
            NotificationManager.IMPORTANCE_LOW
        )
        getSystemService(NotificationManager::class.java).createNotificationChannel(channel)

        val activityIntent = Intent(this, RecordRouteActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            activityIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(this, channelId)
            .setContentTitle("Recording Route")
            .setContentText("Recording your route in the background")
            .setSmallIcon(R.drawable.baseline_route_24)
            .setContentIntent(pendingIntent)
            .build()
    }
}