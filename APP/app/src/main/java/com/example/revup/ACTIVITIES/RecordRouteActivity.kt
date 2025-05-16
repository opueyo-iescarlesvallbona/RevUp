package com.example.revup.ACTIVITIES

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.revup.R
import com.example.revup._API.RevupCrudAPI
import com.example.revup.databinding.ActivityRecordRouteBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng

class RecordRouteActivity : AppCompatActivity(), OnMapReadyCallback {
    lateinit var binding: ActivityRecordRouteBinding
    var apiRevup = RevupCrudAPI()
    var mMap: GoogleMap? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    val REQUEST_LOCATION_CODE = 100
    var havePermission: Boolean = false

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
            //latitudActual = location!!.latitude
            //longitudActual = location!!.longitude
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
}

private fun GoogleMap.setOnMyLocationButtonClickListener(activity: RecordRouteActivity) {
    return
}

private fun GoogleMap.setOnMyLocationClickListener(activity: RecordRouteActivity) {
    return
}
