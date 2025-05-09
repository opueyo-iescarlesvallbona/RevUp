package com.example.revup.ADAPTERS

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.revup.ACTIVITIES.MemberDetailsActivity
import com.example.revup.R
import com.example.revup._API.RevupCrudAPI
import com.example.revup._DATACLASS.FormatDate
import com.example.revup._DATACLASS.Post

class MemberDetailsPostsAdapter(var list: MutableList<Post>): RecyclerView.Adapter<MemberDetailsPostsAdapter.ViewHolder>() {
    val apiRevUp = RevupCrudAPI()

    class ViewHolder(val vista: View): RecyclerView.ViewHolder(vista) {
        val postType = vista.findViewById<ImageButton>(R.id.cardview_listEventsRoutes_mapButton)
        val title = vista.findViewById<TextView>(R.id.cardview_listEventsRoutes_title)
        val date = vista.findViewById<TextView>(R.id.cardview_listEventsRoutes_date)
        val deleteButton = vista.findViewById<ImageButton>(R.id.cardview_listEventsRoutes_deleteButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout = LayoutInflater.from(parent.context)
        return ViewHolder(layout.inflate(R.layout.cardview_list_events_routes, parent, false))
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.title.setText(list[position].title)
        holder.date.setText(FormatDate(list[position].postDate).toString())
    }

}