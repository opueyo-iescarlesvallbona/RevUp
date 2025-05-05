package com.example.revup.ADAPTERS

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.revup.R
import com.example.revup._API.RevupCrudAPI
import com.example.revup._DATACLASS.Member

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
        //holder.picture.setImageResource()
        holder.memberName.setText(list[position].name)
        //holder.memberLocation.setText(list[position].name)
        holder.vista.findViewById<ImageButton>(R.id.cardview_memberSearch_picture).setOnClickListener{
            // go to profile
        }
        holder.vista.findViewById<ImageButton>(R.id.cardview_memberSearch_optionsButton).setOnClickListener{
            // show submenu
        }
    }

}