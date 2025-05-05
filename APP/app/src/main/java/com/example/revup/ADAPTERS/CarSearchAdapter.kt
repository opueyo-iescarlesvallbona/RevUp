package com.example.revup.ADAPTERS

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.revup.R
import com.example.revup._API.RevupCrudAPI
import com.example.revup._DATACLASS.Car
import com.example.revup._DATACLASS.Member

class CarSearchAdapter(var list: MutableList<Car>): RecyclerView.Adapter<CarSearchAdapter.ViewHolder>() {
    val apiRevUp = RevupCrudAPI()

    class ViewHolder(val vista: View): RecyclerView.ViewHolder(vista) {
        val picture = vista.findViewById<ImageView>(R.id.cardview_carSearch_picture)
        val carName = vista.findViewById<TextView>(R.id.cardview_carSearch_name)
        val memberName = vista.findViewById<TextView>(R.id.cardview_carSearch_memberName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout = LayoutInflater.from(parent.context)
        return ViewHolder(layout.inflate(R.layout.cardview_car_search, parent, false))
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //holder.picture.setImageResource()
        holder.carName.setText("test") // take model and brand
        //holder.memberName.setText() get member name
        holder.vista.findViewById<ImageView>(R.id.cardview_carSearch_picture).setOnClickListener{
            // go to car details
        }
        holder.vista.findViewById<ImageButton>(R.id.cardview_carSearch_optionsButton).setOnClickListener{
            // show submenu
        }
    }

}