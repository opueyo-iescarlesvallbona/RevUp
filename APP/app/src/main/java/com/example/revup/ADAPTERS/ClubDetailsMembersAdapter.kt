package com.example.revup.ADAPTERS

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.revup.ACTIVITIES.MemberDetailsActivity
import com.example.revup.R
import com.example.revup._API.RevupCrudAPI
import com.example.revup._DATACLASS.Member
import com.example.revup._DATACLASS.image_path

class ClubDetailsMembersAdapter(var list: MutableList<Member>): RecyclerView.Adapter<ClubDetailsMembersAdapter.ViewHolder>() {
    val apiRevUp = RevupCrudAPI()

    class ViewHolder(val vista: View): RecyclerView.ViewHolder(vista) {
        val picture = vista.findViewById<ImageView>(R.id.cardview_clubDetailsMembers_picture)
        val memberName = vista.findViewById<TextView>(R.id.cardview_clubDetailsMembers_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout = LayoutInflater.from(parent.context)
        return ViewHolder(layout.inflate(R.layout.cardview_club_details_members, parent, false))
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(list[position].profilePicture != null && list[position].profilePicture != ""){
            Glide.with(holder.vista.context).load(image_path+list[position].profilePicture).circleCrop().into(holder.picture)
        }else{
            holder.picture.setImageResource(R.drawable.baseline_account_circle_24)
        }
        holder.memberName.setText(list[position].membername)
        holder.vista.setOnClickListener{
            val intent = Intent(holder.vista.context, MemberDetailsActivity::class.java)
            intent.putExtra("member", list[position])
            holder.vista.context.startActivity(intent)
        }
    }

}