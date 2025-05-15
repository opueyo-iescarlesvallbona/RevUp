package com.example.revup.ADAPTERS

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Space
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.revup.ACTIVITIES.CarDetailsActivity
import com.example.revup.ACTIVITIES.MemberDetailsActivity
import com.example.revup.ACTIVITIES.PostDetailsActivity
import com.example.revup.R
import com.example.revup._API.RevupCrudAPI
import com.example.revup._DATACLASS.Car
import com.example.revup._DATACLASS.curr_car
import com.example.revup._DATACLASS.curr_post
import com.example.revup._DATACLASS.current_user
import com.example.revup._DATACLASS.image_path
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class MemberDetailsCarsCarouselAdapter(var list: MutableList<Car>): RecyclerView.Adapter<MemberDetailsCarsCarouselAdapter.ViewHolder>() {
    val apiRevUp = RevupCrudAPI()

    class ViewHolder(val vista: View): RecyclerView.ViewHolder(vista) {
        val picture = vista.findViewById<ImageView>(R.id.cardview_memberDetailsCarCarousel_picture)
        val model = vista.findViewById<TextView>(R.id.cardview_memberDetailsCarCarousel_carModel)
        val deleteButton = vista.findViewById<ImageButton>(R.id.cardview_memberDetailsCarCarousel_deleteButton)
        val editButton = vista.findViewById<ImageButton>(R.id.cardview_memberDetailsCarCarousel_editButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout = LayoutInflater.from(parent.context)
        return ViewHolder(layout.inflate(R.layout.cardview_member_details_car_carousel, parent, false))
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(list[position].id == -1){
            holder.picture.setImageResource(R.drawable.baseline_add_24)
            holder.model.visibility = View.GONE
            holder.editButton.visibility = View.GONE
            holder.deleteButton.visibility = View.GONE
            holder.vista.setOnClickListener{
                val intent = Intent(holder.vista.context, CarDetailsActivity::class.java)
                intent.putExtra("editable", true)
                curr_car = null
                holder.vista.context.startActivity(intent)
            }
            return
        }
        Glide.with(holder.vista.context).load(image_path+list[position].picture).into(holder.picture)
        holder.model.setText(list[position].model!!.brand!!.name + " - " + list[position].model!!.modelName)
        holder.vista.setOnClickListener{
            val intent = Intent(holder.vista.context, CarDetailsActivity::class.java)
            intent.putExtra("editable", false)
            curr_car = list[position]
            holder.vista.context.startActivity(intent)
        }
        if(list[position].memberId == current_user!!.id){
            holder.editButton.setOnClickListener{
                val intent = Intent(holder.vista.context, CarDetailsActivity::class.java)
                intent.putExtra("editable", true)
                curr_car = list[position]
                holder.vista.context.startActivity(intent)
            }

            holder.deleteButton.setOnClickListener{
                try{
                    MaterialAlertDialogBuilder(holder.vista.context)
                        .setTitle("Delete ${list[position].model!!.modelName}")
                        .setMessage("You are going to delete ${list[position].model!!.modelName}. Are you sure?")
                        .setPositiveButton("Delete") { dialog, _ ->
                            var result = apiRevUp.deleteCar(list[position].id!!, holder.vista.context)
                            if(result){
                                list.remove(list[position])
                                notifyItemRemoved(position)
                                notifyItemRangeChanged(position, list.size)
                                dialog.dismiss()
                            }else{
                                throw Exception("Error on unfollowing")
                            }
                            dialog.dismiss()
                        }
                        .setNegativeButton("Cancel") { dialog, _ ->
                            dialog.dismiss()
                        }
                        .show()
                }catch(e: Exception){
                    Toast.makeText(holder.vista.context, "Error on deleting car. $e.message", Toast.LENGTH_SHORT).show()
                }
            }
        }else{
            holder.editButton.visibility = View.GONE
            holder.deleteButton.visibility = View.GONE
        }
    }

}