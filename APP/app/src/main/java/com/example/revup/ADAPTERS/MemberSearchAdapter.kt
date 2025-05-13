package com.example.revup.ADAPTERS

import android.annotation.SuppressLint
import android.content.Intent
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Space
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.revup.ACTIVITIES.ClubDetailsActivity
import com.example.revup.ACTIVITIES.MainActivity
import com.example.revup.ACTIVITIES.MemberDetailsActivity
import com.example.revup.R
import com.example.revup._API.RevupCrudAPI
import com.example.revup._DATACLASS.Member
import com.example.revup._DATACLASS.curr_member
import com.example.revup._DATACLASS.current_user
import com.example.revup._DATACLASS.image_path

class MemberSearchAdapter(var list: MutableList<Member>): RecyclerView.Adapter<MemberSearchAdapter.ViewHolder>() {
    val apiRevUp = RevupCrudAPI()

    class ViewHolder(val vista: View): RecyclerView.ViewHolder(vista) {
        val picture = vista.findViewById<ImageButton>(R.id.cardview_memberSearch_picture)
        val memberName = vista.findViewById<TextView>(R.id.cardview_memberSearch_memberName)
        val memberLocation = vista.findViewById<TextView>(R.id.cardview_memberSearch_memberLocation)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout = LayoutInflater.from(parent.context)
        return ViewHolder(layout.inflate(R.layout.cardview_member_search, parent, false))
    }

    override fun getItemCount() = list.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(position == list.size-1){
            holder.vista.findViewById<Space>(R.id.cardview_memberSearch_space).layoutParams.height = 250
        }
        if(list[position].profilePicture != null && list[position].profilePicture != ""){
            Glide.with(holder.vista.context).load(image_path+list[position].profilePicture).circleCrop().into(holder.picture)
        }else{
            holder.picture.setImageResource(R.drawable.baseline_account_circle_24)
        }
        if(list[position].membername == current_user!!.membername){
            holder.memberName.setText("Me")
            holder.memberName.setTextColor(holder.vista.context.getColor(R.color.orange))
        }else{
            holder.memberName.setText(list[position].membername)
        }
        if (list[position].locationId != null){
            val location = apiRevUp.getLocationById(list[position].locationId!!, holder.vista.context)
            holder.memberLocation.setText(location!!.municipality)
        }
        holder.vista.setOnClickListener{
            val intent = Intent(holder.vista.context, MemberDetailsActivity::class.java)
            curr_member = list[position]
            holder.vista.context.startActivity(intent)
        }
        holder.picture.setOnClickListener{
            val intent = Intent(holder.vista.context, MemberDetailsActivity::class.java)
            curr_member = list[position]
            holder.vista.context.startActivity(intent)
        }
    }

}