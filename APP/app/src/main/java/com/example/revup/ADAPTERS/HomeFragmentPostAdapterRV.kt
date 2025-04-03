package com.example.revup.ADAPTERS

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.revup.R
import org.w3c.dom.Text

class HomeFragmentPostAdapterRV(var list: List<String>): RecyclerView.Adapter<HomeFragmentPostAdapterRV.ViewHolder>() {
    var viewTypeCust = 1
    class ViewHolder(val vista: View): RecyclerView.ViewHolder(vista) {
        val user = vista.findViewById<TextView>(R.id.cardview_post_mainactivity_username)
        val content = vista.findViewById<TextView>(R.id.cardview_post_mainactivity_contentText)
        val timeAgo = vista.findViewById<TextView>(R.id.cardview_post_mainactivity_timeAgo)
        val image = vista.findViewById<ImageView>(R.id.cardview_post_mainactivity_contentImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout = LayoutInflater.from(parent.context)
        if(viewTypeCust%2==0){
            return ViewHolder(layout.inflate(R.layout.cardview_textpost_mainactivity, parent, false))
        }
        return ViewHolder(layout.inflate(R.layout.cardview_imagepost_mainactivity, parent, false))
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.user.setText("user" + position.toString())
        if(viewTypeCust%2==0){
            holder.content.setText("Past messages and message conversation history is accessible via two sources. First, you can get it through the messaging app. Secondly, you get it online through your cloud storage; Google Drive for Android and iCloud for iPhone.")
        }else{
            holder.image.setImageResource(R.drawable.car_test)
        }
        holder.timeAgo.setText(position.toString() + " day ago")
        viewTypeCust++
    }
}