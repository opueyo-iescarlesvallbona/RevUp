package com.example.revup.ACTIVITIES

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.FragmentContainerView
import com.bumptech.glide.Glide
import com.example.revup.R
import com.example.revup._API.RevupCrudAPI
import com.example.revup._DATACLASS.Club
import com.example.revup._DATACLASS.EventState
import com.example.revup._DATACLASS.curr_event
import com.example.revup._DATACLASS.image_path
import com.example.revup.databinding.ActivityEventDetailsBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class EventDetailsActivity : AppCompatActivity(), OnMapReadyCallback {
    lateinit var binding : ActivityEventDetailsBinding
    val apiRevup = RevupCrudAPI()
    var mMap: GoogleMap? = null
    val event = curr_event
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityEventDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.carDetailsActivityBackButton.setOnClickListener {
            this.onBackPressed()
            val returnIntent = Intent()
            setResult(RESULT_OK, returnIntent)
            finish()
        }

        if(event!!.bitmap!=null){
            try{
                binding.eventDetailsActivityPicture.setImageBitmap(event?.bitmap)
            }catch (e: Exception){
                Toast.makeText(this, "Error setting event image", Toast.LENGTH_SHORT).show()
            }
        }else{
            try{
                Glide.with(this).load(image_path+event.picture).into(binding.eventDetailsActivityPicture)
            }catch (e: Exception){
                Toast.makeText(this, "Error setting event image", Toast.LENGTH_SHORT).show()
            }
        }

        binding.eventDetailsActivityNameTextField.setText(event?.name.toString())
        binding.eventDetailsActivityAddressTextField.setText(event?.address.toString())

        var club: Club? = null
        try{
            club = apiRevup.getClubById(event!!.clubId, this)
        }catch (e: Exception){
            Toast.makeText(this, "Error getting club data", Toast.LENGTH_SHORT).show()
        }

        if(club!=null){
            binding.eventDetailsActivityClub.setText(club.name)
        }

        binding.eventDetailsActivityStartdate.setText(event?.startDate.toString())
        binding.eventDetailsActivityRoutestartdate.setText(event?.routeStartDate.toString())
        binding.eventDetailsActivityEnddate.setText(event?.endDate.toString())
        binding.eventDetailsActivityDescript.setText(event?.description.toString())

        var state: EventState? = null
        try{
            state = apiRevup.getEventStateById(event!!.state, this)
        }catch (e: Exception){
            Toast.makeText(this, "Error getting event state data", Toast.LENGTH_SHORT).show()
        }

        if(state!=null){
            binding.eventDetailsActivityState.setText(state.name.toString())
        }

        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapFragment_eventsDetails_map) as SupportMapFragment
        mapFragment.getMapAsync(this)


    }

    override fun onMapReady(map: GoogleMap) {
        mMap = map
        map!!.mapType = GoogleMap.MAP_TYPE_NORMAL
        map.uiSettings?.isZoomControlsEnabled = true

        val pos = LatLng(event!!.lat, event.long)

        val markerOption = MarkerOptions()
        markerOption.position(pos)

        markerOption.title(event.name.toString())

        map?.addMarker(markerOption)
        map?.animateCamera(CameraUpdateFactory.newLatLngZoom(pos, 15f))
    }
}