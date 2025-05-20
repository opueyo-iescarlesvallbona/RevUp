package com.example.revup.ADAPTERS

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Space
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.revup.ACTIVITIES.ClubChatActivity
import com.example.revup.ACTIVITIES.ClubDetailsActivity
import com.example.revup.ACTIVITIES.MemberChatActivity
import com.example.revup.R
import com.example.revup._API.RevupCrudAPI
import com.example.revup._DATACLASS.Car
import com.example.revup._DATACLASS.Club
import com.example.revup._DATACLASS.curr_club
import com.example.revup._DATACLASS.curr_club_chat
import com.example.revup._DATACLASS.curr_member_chat
import com.example.revup._DATACLASS.image_path

class ClubChatAdapter(var list: MutableList<Club>): RecyclerView.Adapter<ClubChatAdapter.ViewHolder>() {
    val apiRevUp = RevupCrudAPI()

    class ViewHolder(val vista: View): RecyclerView.ViewHolder(vista) {
        val picture = vista.findViewById<ImageView>(R.id.cardview_clubSearch_picture)
        val carName = vista.findViewById<TextView>(R.id.cardview_clubSearch_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout = LayoutInflater.from(parent.context)
        return ViewHolder(layout.inflate(R.layout.cardview_club_search, parent, false))
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(position == list.size-1){
            holder.vista.findViewById<Space>(R.id.cardview_clubSearch_space).layoutParams.height = 250
        }
        if(list[position].picture != null && list[position].picture != ""){
            Glide.with(holder.vista.context).load(image_path+list[position].picture).circleCrop().into(holder.picture)
        }else{
            holder.picture.setImageResource(R.drawable.baseline_account_circle_24)
        }
        holder.carName.setText(list[position].name)
        holder.vista.findViewById<ImageView>(R.id.cardview_clubSearch_picture).setOnClickListener{
            val intent = Intent(holder.vista.context, ClubChatActivity::class.java)
            curr_club_chat = list[position]
            holder.vista.context.startActivity(intent)
        }
        holder.vista.setOnClickListener{
            val intent = Intent(holder.vista.context, ClubChatActivity::class.java)
            curr_club_chat = list[position]
            holder.vista.context.startActivity(intent)
        }
    }

}