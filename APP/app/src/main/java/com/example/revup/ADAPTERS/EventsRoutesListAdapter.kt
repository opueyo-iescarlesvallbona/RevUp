package com.example.revup.ADAPTERS

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.revup.R
import com.example.revup._API.RevupCrudAPI
import com.example.revup._DATACLASS.ClubEvent
import com.example.revup._DATACLASS.FormatDate
import com.example.revup._DATACLASS.Route
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter

class EventsRoutesListAdapter<T>(var list: MutableList<T>): RecyclerView.Adapter<EventsRoutesListAdapter.ViewHolder>() {
    val apiRevUp = RevupCrudAPI()

    class ViewHolder(val vista: View): RecyclerView.ViewHolder(vista) {
        val title = vista.findViewById<TextView>(R.id.cardview_listEventsRoutes_title)
        val date = vista.findViewById<TextView>(R.id.cardview_listEventsRoutes_date)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout = LayoutInflater.from(parent.context)
        return ViewHolder(layout.inflate(R.layout.cardview_list_events_routes, parent, false))
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dateFormat = SimpleDateFormat("dd-MM-yyyy")
        val item = list[position]
        when (item) {
            is Route -> {
                holder.title.setText(item.name)
                holder.date.setText(dateFormat.format(FormatDate(item.datetime!!)))
            }
            is ClubEvent -> {
                holder.title.setText(item.name)
                if (dateFormat.format(FormatDate(item.startDate)).equals(dateFormat.format(FormatDate(item.endDate)))){
                    holder.date.setText(dateFormat.format(FormatDate(item.startDate)))
                }else{
                    holder.date.setText(dateFormat.format(FormatDate(item.startDate)) + " - " + dateFormat.format(FormatDate(item.endDate)))
                }
            }
        }
        holder.vista.findViewById<ImageButton>(R.id.cardview_listEventsRoutes_mapButton).setOnClickListener{
            // go to map tab
        }
        holder.vista.findViewById<ImageButton>(R.id.cardview_listEventsRoutes_deleteButton).setOnClickListener{
            when(item){
                is Route -> {
                    MaterialAlertDialogBuilder(holder.vista.context)
                        .setTitle("Delete Route")
                        .setMessage("Do you want to delete this route?")
                        .setPositiveButton("Delete") { dialog, _ ->
                            apiRevUp.deleteRoute(item.id, holder.vista.context)
                            list.remove(item)
                            notifyItemRemoved(position)
                            notifyItemRangeChanged(position, list.size)
                            dialog.dismiss()
                        }
                        .setNegativeButton("Cancel") { dialog, _ ->
                            dialog.dismiss()
                        }
                        .show()
                }
                is ClubEvent -> {
                    MaterialAlertDialogBuilder(holder.vista.context) // use "this" if inside Activity
                        .setTitle("Delete Event")
                        .setMessage("Do you want to delete this event?")
                        .setPositiveButton("Delete") { dialog, _ ->
                            apiRevUp.deleteEvent(item.id, holder.vista.context)
                            list.remove(item)
                            notifyItemRemoved(position)
                            notifyItemRangeChanged(position, list.size)
                            dialog.dismiss()
                        }
                        .setNegativeButton("Cancel") { dialog, _ ->
                            dialog.dismiss()
                        }
                        .show()

                }
            }
        }
    }

}