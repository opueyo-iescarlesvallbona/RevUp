package com.example.revup.ADAPTERS

import android.content.Intent
import android.util.Log
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.Glide.init
import com.example.revup.ACTIVITIES.MemberDetailsActivity
import com.example.revup.R
import com.example.revup._API.RevupCrudAPI
import com.example.revup._DATACLASS.Member
import com.example.revup._DATACLASS.curr_club
import com.example.revup._DATACLASS.curr_member
import com.example.revup._DATACLASS.current_user
import com.example.revup._DATACLASS.image_path

class ClubDetailsMembersAdapter(var list: MutableList<Member>): RecyclerView.Adapter<ClubDetailsMembersAdapter.ViewHolder>() {
    val apiRevUp = RevupCrudAPI()

    class ViewHolder(val vista: View): RecyclerView.ViewHolder(vista), View.OnCreateContextMenuListener {
        val picture = vista.findViewById<ImageView>(R.id.cardview_clubDetailsMembers_picture)
        val memberName = vista.findViewById<TextView>(R.id.cardview_clubDetailsMembers_name)
        val role = vista.findViewById<TextView>(R.id.cardview_clubDetailsMembers_role)

        var currentRole: String? = null
        init {
            role.setOnCreateContextMenuListener(this)
        }
        override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
            menu?.setHeaderTitle("Modify Role")
            when (currentRole) {
                "Member" -> {
                    menu?.add(adapterPosition, 1, 0, "Set role Administrator")
                }
                "Administrator" -> {
                    menu?.add(adapterPosition, 2, 0, "Set role Member")
                }
            }
        }
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
        if(list[position].membername == current_user!!.membername){
            holder.memberName.setText("Me")
            holder.memberName.setTextColor(holder.vista.context.getColor(R.color.orange))
        }else{
            holder.memberName.setText(list[position].membername)
        }
        Log.i("CLUB_ROLE", "clubid: ${curr_club!!.id}, memberid: ${list[position].id}")
        val role = apiRevUp.getMemberClubRoleById(curr_club!!.id!!, list[position].id!!, holder.vista.context)
        holder.currentRole = role!!.name
        when(role!!.name){
            "Founder" -> {
                holder.role.setTextColor(holder.vista.context.getColor(R.color.clubRole_Founder))
                holder.role.setText("Founder")
            }
            "Administrator" -> {
                holder.role.setTextColor(holder.vista.context.getColor(R.color.clubRole_Administrator))
                holder.role.setText("Administrator")
            }
            else -> {
                holder.role.setTextColor(holder.vista.context.getColor(R.color.clubRole_Member))
                holder.role.setText("Member")
            }
        }
        holder.vista.setOnClickListener {
            val intent = Intent(holder.vista.context, MemberDetailsActivity::class.java)
            curr_member = list[position]
            holder.vista.context.startActivity(intent)
        }

        val user_role = apiRevUp.getMemberClubRoleById(curr_club!!.id!!, current_user!!.id!!, holder.vista.context)
        if(user_role != null){
            if(user_role!!.name == "Founder" || user_role!!.name == "Administrator"){
                holder.role.setOnClickListener {
                    holder.role.showContextMenu()
                    true
                }
            }
        }
    }

}