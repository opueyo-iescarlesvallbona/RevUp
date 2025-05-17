package com.example.revup.FRAGMENTS

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.RadioGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.example.revup.ACTIVITIES.EventDetailsActivity
import com.example.revup.ADAPTERS.CardviewEventMapAdapter
import com.example.revup.R
import com.example.revup._API.RevupCrudAPI
import com.example.revup._DATACLASS.CardViewEventMap
import com.example.revup._DATACLASS.ClubEvent
import com.example.revup._DATACLASS.curr_event
import com.example.revup._DATACLASS.current_user
import com.example.revup._DATACLASS.image_path
import com.example.revup.databinding.FragmentMapEventsBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

var tincPermisos = false
class MapEventsFragment : Fragment(), OnMapReadyCallback {
    val apiRevup = RevupCrudAPI()
    lateinit var binding: FragmentMapEventsBinding
    var mMap: GoogleMap? = null
    val REQUEST_LOCATION_CODE = 1
    private lateinit var radioGroup: RadioGroup
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var googleMapsFragment: SupportMapFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMapEventsBinding.inflate(layoutInflater, container, false)
        return binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapStyles = listOf("Normal", "Satellite", "Hybrid", "Terrain")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, mapStyles)
        binding.textViewMenuMapStyle.setAdapter(adapter)

        var options = binding.textViewMenuMapStyle
        binding.textViewMenuMapStyle.setText(mapStyles[0], false)
        options.setOnItemClickListener { _, _, position, _ ->
            when (mapStyles[position]) {
                "Normal" -> {
                    mMap?.mapType = GoogleMap.MAP_TYPE_NORMAL
                    Log.i("MAP", "Map type set to normal")
                }
                "Satellite" -> mMap?.mapType = GoogleMap.MAP_TYPE_SATELLITE
                "Hybrid" -> mMap?.mapType = GoogleMap.MAP_TYPE_HYBRID
                "Terrain" -> mMap?.mapType = GoogleMap.MAP_TYPE_TERRAIN
            }

        }
        val mapFragment = childFragmentManager
            .findFragmentById(R.id.mapFragment_eventsFragment_mainActivity_map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(map: GoogleMap) {
        mMap = map
        map!!.mapType = GoogleMap.MAP_TYPE_NORMAL
        map.uiSettings?.isZoomControlsEnabled = true



        val clubs = apiRevup.getClubsByMember(current_user!!.id, requireContext())
        var events: MutableList<ClubEvent> = mutableListOf()
        if(clubs!=null){
            for(club in clubs){
                var cevents = apiRevup.getAllEventsByClub(club.id!!, requireContext())
                if(cevents==null){
                    cevents = mutableListOf()
                }

                for (event in cevents){
                    if(event.state==1||event.state==0){
                        events.add(event)
                    }
                }
            }
        }

        if(events!=null){
            for (event in events!!){
                val pos = LatLng(event.lat, event.long)
                Glide.with(requireContext())
                    .asBitmap()
                    .load(image_path+event.picture)
                    .into(object : CustomTarget<Bitmap>() {
                        override fun onResourceReady(resource: Bitmap, transition: com.bumptech.glide.request.transition.Transition<in Bitmap>?) {
                            event.bitmap = resource
                        }

                        override fun onLoadCleared(placeholder: Drawable?) {

                        }
                    })

                val markerOption = MarkerOptions()
                markerOption.position(pos)

                markerOption.title(event.name.toString())

                val marker = map?.addMarker(markerOption)
                marker?.tag = CardViewEventMap(event)
                map?.setInfoWindowAdapter(CardviewEventMapAdapter(requireView().context))

                map?.setOnInfoWindowClickListener{
                    val intent = Intent(context, EventDetailsActivity::class.java)
                    val data = marker?.tag as CardViewEventMap
                    curr_event = data.event
                    context?.startActivity(intent)
                }

                map?.setOnMarkerClickListener(object : OnMarkerClickListener {
                    override fun onMarkerClick(p0: Marker): Boolean {
                        val position: LatLng = p0!!.getPosition()


                        // Animate camera to marker with zoom level 15 (adjust as needed)
                        map?.animateCamera(CameraUpdateFactory.newLatLngZoom(position, 10f))


                        // Show the info window (optional)
                        p0.showInfoWindow()

                        return true
                    }
                })

            }
        }
    }

}