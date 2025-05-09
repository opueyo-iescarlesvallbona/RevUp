package com.example.revup.ADAPTERS

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.revup.ACTIVITIES.ClubDetailsActivity
import com.example.revup.R
import com.example.revup._API.RevupCrudAPI
import com.example.revup._DATACLASS.Car
import com.example.revup._DATACLASS.Club

class ClubSearchAdapter(var list: MutableList<Club>): RecyclerView.Adapter<ClubSearchAdapter.ViewHolder>() {
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
        //holder.picture.setImageResource()
        holder.carName.setText(list[position].name)
        holder.vista.findViewById<ImageView>(R.id.cardview_clubSearch_picture).setOnClickListener{
            val intent = Intent(holder.vista.context, ClubDetailsActivity::class.java)
            intent.putExtra("club", list[position])
            holder.vista.context.startActivity(intent)
        }
        holder.vista.setOnClickListener{
            val intent = Intent(holder.vista.context, ClubDetailsActivity::class.java)
            intent.putExtra("club", list[position])
            holder.vista.context.startActivity(intent)
        }
    }

}