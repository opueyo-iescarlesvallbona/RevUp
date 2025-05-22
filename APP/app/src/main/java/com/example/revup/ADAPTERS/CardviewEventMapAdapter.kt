package com.example.revup.ADAPTERS

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.transition.Transition
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.SimpleTarget
import com.example.revup.ACTIVITIES.EventDetailsActivity
import com.example.revup.R
import com.example.revup._API.RevupCrudAPI
import com.example.revup._DATACLASS.CardViewEventMap
import com.example.revup._DATACLASS.curr_event
import com.example.revup._DATACLASS.image_path
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import org.w3c.dom.Text


class CardviewEventMapAdapter (private val context : Context) : GoogleMap.InfoWindowAdapter {
    var apiRevUp = RevupCrudAPI()

    override fun getInfoContents(p0: Marker): View? {
        return null
    }

    override fun getInfoWindow(marker: Marker): View? {
        val view = LayoutInflater.from(context).inflate(R.layout.cardview_event_map,null)
        val title : TextView = view.findViewById(R.id.infoWindowTitle)
        val desc : TextView = view.findViewById(R.id.infoWindowDesc)
        val image : ImageView = view.findViewById<ImageView>(R.id.infoWindowIv)
        val club : TextView = view.findViewById<TextView>(R.id.infoWindowClub)

        val data = marker?.tag as? CardViewEventMap
        
        title.text = data?.event!!.name
        desc.text = data.event.description

        var clubObj = apiRevUp.getClubById(data?.event?.clubId!!, context)
        if(clubObj != null){
            club.text = clubObj.name
        }


        image.setImageBitmap(data.event.bitmap)
        return view
    }
}