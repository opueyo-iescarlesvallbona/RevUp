package com.example.revup.ACTIVITIES

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.revup.R
import com.example.revup.databinding.ActivitySetLocationMapBinding
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class SetLocationMapActivity : AppCompatActivity(), OnMapReadyCallback {
    lateinit var binding: ActivitySetLocationMapBinding
    var location: LatLng? = null
    var mMap: GoogleMap? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySetLocationMapBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnSetlocationMapSetlocation.setOnClickListener {
            val resultIntent = Intent()
            resultIntent.putExtra("LAT", location!!.latitude)
            resultIntent.putExtra("LNG", location!!.longitude)
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }

        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.mapFragment_setlocationmap) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(p0: GoogleMap) {
        mMap = p0
        mMap?.setOnMapClickListener { latLng ->
            mMap?.clear()
            location = latLng
            mMap?.addMarker(MarkerOptions().position(latLng).title("Selected Location"))
        }
    }
}