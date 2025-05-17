package com.example.revup.ACTIVITIES

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.revup.R
import com.example.revup._API.RevupCrudAPI
import com.example.revup._DATACLASS.Route
import com.example.revup._DATACLASS.curr_route
import com.example.revup._DATACLASS.current_user
import com.example.revup.databinding.ActivityRecordRouteBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import java.math.BigDecimal

class RecordRouteActivity : AppCompatActivity(), OnMapReadyCallback {
    lateinit var binding: ActivityRecordRouteBinding
    var mMap: GoogleMap? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    val REQUEST_LOCATION_CODE = 100
    var havePermission: Boolean = false

    private lateinit var locationCallback: LocationCallback
    private lateinit var locationRequest: LocationRequest
    private var isTracking = false
    private val routePoints = mutableListOf<LatLng>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRecordRouteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.recordRouteActivity_map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        CheckPermissions()

        if(havePermission){
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        }else{
            Toast.makeText(this, "You do not have permission for this function.\nYou have to activate it manually.", Toast.LENGTH_SHORT).show()
        }

        binding.recordRouteActivityStartPause.setOnClickListener{
            if (isTracking) {
                stopLocationUpdates()
                binding.recordRouteActivityStartPause.setImageResource(R.drawable.baseline_play_circle_filled_24)
            } else {
                startLocationUpdates()
                binding.recordRouteActivityStartPause.setImageResource(R.drawable.baseline_pause_circle_filled_24)
                binding.recordRouteActivityFinish.visibility = View.VISIBLE
            }
            isTracking = !isTracking
        }

        binding.recordRouteActivityFinish.setOnClickListener{
            MaterialAlertDialogBuilder(this)
                .setTitle("Finish Record Route")
                .setMessage("You are going to finish the record.\nAre you sure you want to finish it?")
                .setPositiveButton("Finish") { dialog, _ ->
                    stopLocationUpdates()
                    isTracking = false
                    binding.recordRouteActivityStartPause.setImageResource(R.drawable.baseline_play_circle_filled_24)
                    if(routePoints.size > 5){
                        val route = addParams()
                        curr_route = route
                        val intent = Intent(this, EditRouteActivity::class.java)
                        startActivity(intent)
                    }else{
                        Toast.makeText(this, "The route is too short to be saved.", Toast.LENGTH_SHORT).show()
                    }
                    dialog.dismiss()
                }
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }

        locationRequest = LocationRequest.create().apply {
            interval = 5000 // cada 5 segundos
            fastestInterval = 2000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                for (location in locationResult.locations) {
                    val newPoint = LatLng(location.latitude, location.longitude)
                    if(routePoints.isEmpty()){
                        routePoints.add(newPoint)
                        drawRoute(mMap!!, routePoints)
                    }else{
                        if(newPoint != routePoints.last()){
                            routePoints.add(newPoint)
                            drawRoute(mMap!!, routePoints)
                        }
                    }
                }
            }
        }
    }

    override fun onMapReady(map: GoogleMap) {
        mMap = map

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        mMap!!.isMyLocationEnabled = true
        mMap!!.setOnMyLocationClickListener(this)
        mMap!!.setOnMyLocationButtonClickListener(this)
        fusedLocationClient.lastLocation.addOnSuccessListener { location : Location? ->
            mMap!!.animateCamera(
                CameraUpdateFactory.newLatLngZoom(LatLng(location!!.latitude, location!!.longitude), 15.0f),
                3000, null
            )
        }
    }

    fun CheckPermissions(){
        if (
            (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            &&
            (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        ){
            havePermission = true
        }else{
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)
                || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION))
                havePermission = false
            else{
                ActivityCompat.requestPermissions(this,
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION), REQUEST_LOCATION_CODE
                )
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_LOCATION_CODE) {
            if (grantResults != null &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                grantResults[1] == PackageManager.PERMISSION_GRANTED){
                havePermission = true
                onMapReady(mMap!!)
            }
            else
                havePermission = false
        }
    }

    private fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, mainLooper)
        }
    }

    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    private fun drawRoute(map: GoogleMap, coordenades: MutableList<LatLng>){
        val polyLineOptions = PolylineOptions()
        coordenades.forEach{
            polyLineOptions.add(it)
        }
        runOnUiThread{
            val poly = map.addPolyline(polyLineOptions)
        }
    }

    override fun onBackPressed() {
        if (isTracking || routePoints.isNotEmpty()) {
            MaterialAlertDialogBuilder(this)
                .setTitle("Exit Record Route")
                .setMessage("You are going to loose the recorded route.\nAre you sure you want to exit?")
                .setPositiveButton("Exit") { dialog, _ ->
                    super.onBackPressed()
                    dialog.dismiss()
                }
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        } else {
            super.onBackPressed()
        }
    }

    fun addParams(): Route {
        var distance: BigDecimal? = null
        var startAddress: String? = null
        var endAddress: String? = null

        try{
            distance = calculateTotalDistance(routePoints)
        }catch (e: Exception){
            Toast.makeText(this, "Error calculating distance", Toast.LENGTH_LONG).show()
        }

        try{
            startAddress = getAddressFromLatLng(this, routePoints.first())
            endAddress = getAddressFromLatLng(this, routePoints.last())
        }catch (e: Exception){
            Toast.makeText(this, "Error getting start and end address", Toast.LENGTH_LONG).show()
        }

        val waypoints = Gson().toJson(routePoints)

        return Route(memberId = current_user!!.id, waypoints = waypoints, distance = distance!!, startAddress = startAddress, endAddress = endAddress)
    }

    fun calculateTotalDistance(points: MutableList<LatLng>): BigDecimal {
        var totalDistance = 0f
        for (i in 0 until points.size - 1) {
            val loc1 = Location("").apply {
                latitude = points[i].latitude
                longitude = points[i].longitude
            }
            val loc2 = Location("").apply {
                latitude = points[i + 1].latitude
                longitude = points[i + 1].longitude
            }
            totalDistance += loc1.distanceTo(loc2) // en metros
        }
        return BigDecimal(totalDistance.toDouble())
    }

    fun getAddressFromLatLng(context: Context, latLng: LatLng): String? {
        val geocoder = Geocoder(context)
        val addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
        return addresses?.firstOrNull()?.getAddressLine(0)
    }
}

private fun GoogleMap.setOnMyLocationButtonClickListener(activity: RecordRouteActivity) {
    return
}

private fun GoogleMap.setOnMyLocationClickListener(activity: RecordRouteActivity) {
    return
}
