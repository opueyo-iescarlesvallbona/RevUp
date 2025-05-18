package com.example.revup.ACTIVITIES

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.revup.R
import com.example.revup._API.RevupCrudAPI
import com.example.revup._DATACLASS.Route
import com.example.revup._DATACLASS.curr_car
import com.example.revup._DATACLASS.curr_route
import com.example.revup._DATACLASS.current_user
import com.example.revup._DATACLASS.formatDistance
import com.example.revup._DATACLASS.recreated
import com.example.revup.databinding.ActivityEditRouteBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.math.BigDecimal
import java.time.LocalDateTime

class EditRouteActivity : AppCompatActivity(), OnMapReadyCallback {
    lateinit var binding: ActivityEditRouteBinding
    var apiRevUp = RevupCrudAPI()
    var route: Route? = null
    var mMap: GoogleMap? = null
    var waypoints: MutableList<LatLng> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityEditRouteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.editRouteActivity_map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        binding.editRouteActivityBackButton.setOnClickListener{
            this.onBackPressed()
            val returnIntent = Intent()
            setResult(RESULT_OK, returnIntent)
            finish()
        }

        binding.editRouteActivityBackButtonText.setOnClickListener {
            this.onBackPressed()
            val returnIntent = Intent()
            setResult(RESULT_OK, returnIntent)
            finish()
        }

        binding.editRouteActivityEditButton.setOnClickListener{
            if(binding.editRouteActivityEditButton.text == "Edit Route"){
                binding.editRouteActivityEditButton.text = "Save Route"
                binding.editRouteActivityEditButton.setTextColor(getColor(R.color.green))
                disableWidgets(true)
            }else {
                var new_route = checkParams()
                if (new_route != null) {
                    recreated = false
                    try {
                        if (curr_route!!.id == 0) {
                            val result = apiRevUp.postRoute(new_route, applicationContext)
                            if (result != null) {
                                Toast.makeText(this, "Route saved", Toast.LENGTH_LONG).show()
                                binding.editRouteActivityEditButton.text = "Edit Route"
                                binding.editRouteActivityEditButton.setTextColor(getColor(R.color.orange))
                                curr_route = null
                                val intent = Intent(this, MainActivity::class.java)
                                startActivity(intent)
                            } else {
                                Toast.makeText(this, "Error saving route", Toast.LENGTH_LONG).show()
                            }
                        } else {
                            new_route.id = curr_route!!.id
                            val result = apiRevUp.putRoute(new_route, applicationContext)
                            if (result != null) {
                                Toast.makeText(this, "Route saved", Toast.LENGTH_LONG).show()
                                binding.editRouteActivityEditButton.text = "Edit Car"
                                binding.editRouteActivityEditButton.setTextColor(getColor(R.color.orange))
                                curr_route = null
                                curr_route = null
                                val intent = Intent(this, MainActivity::class.java)
                                startActivity(intent)
                            } else {
                                Toast.makeText(this, "Error saving route", Toast.LENGTH_LONG).show()
                            }
                        }
                    } catch (e: Exception) {
                        Toast.makeText(this, "Error saving route. ${e.message}", Toast.LENGTH_LONG)
                            .show()
                        Log.i("ERROR ROUTE SAVE", e.message.toString())
                    }
                }
            }
        }

        route = curr_route
        if(route != null){
            if(route!!.memberId != current_user!!.id){
                binding.editRouteActivityEditButton.isEnabled = false
                disableWidgets(false)
            }

            if(route!!.id != 0){
                binding.editRouteActivityName.setText(route!!.name)
                binding.editRouteActivityDescript.setText(route!!.description)
                disableWidgets(false)
            }else{
                binding.editRouteActivityEditButton.text = "Save Route"
                binding.editRouteActivityEditButton.setTextColor(getColor(R.color.green))
                disableWidgets(true)
            }
            binding.editRouteActivityDistance.isEnabled = false
            binding.editRouteActivityStartAddress.isEnabled = false
            binding.editRouteActivityEndAddress.isEnabled = false
            binding.editRouteActivityDatetime.isEnabled = false
            binding.editRouteActivityDistance.setText(formatDistance(route!!.distance!!))
            binding.editRouteActivityStartAddress.setText(route!!.startAddress.toString())
            binding.editRouteActivityEndAddress.setText(route!!.endAddress.toString())
            binding.editRouteActivityDatetime.setText(route!!.datetime.toString())
        }
    }

    fun disableWidgets(set: Boolean = false) {
        binding.editRouteActivityName.isEnabled = set
        binding.editRouteActivityDescript.isEnabled = set
    }

    fun checkParams(): Route? {
        var name: String? = null
        var description: String? = null

        if(binding.editRouteActivityName.text.toString() == ""){
            Toast.makeText(this, "Please enter a name", Toast.LENGTH_LONG).show()
            return null
        }else{
            name = binding.editRouteActivityName.text.toString()
        }

        if(binding.editRouteActivityDescript.text.toString() == ""){
            description = null
        }else{
            description = binding.editRouteActivityDescript.text.toString()
        }

        var new_route = curr_route
        new_route!!.name = name
        new_route.description = description
        new_route.datetime = LocalDateTime.now().toString()

        return new_route
    }

    override fun onMapReady(map: GoogleMap) {
        mMap = map

        val type = object : TypeToken<MutableList<LatLng>>() {}.type
        waypoints = Gson().fromJson(route!!.waypoints, type)
        drawRoute(map, waypoints)

        val central = LatLng((waypoints.first().latitude+waypoints.last().latitude)/2, (waypoints.first().longitude + waypoints.last().longitude)/2)
        map.addMarker(
            MarkerOptions().position(waypoints.first()).title("")
        )
        map.animateCamera(
            CameraUpdateFactory.newLatLngZoom(central, 13.0f),
            2000, null
        )
        map.addMarker(
            MarkerOptions().position(waypoints.last()).title("")
        )
    }

    private fun drawRoute(map: GoogleMap, coordenades: MutableList<LatLng>){
        val polyLineOptions = PolylineOptions()
        for(cord in coordenades){
            polyLineOptions.add(cord)
        }
        runOnUiThread{
            val poly = map.addPolyline(polyLineOptions)
        }
    }
}