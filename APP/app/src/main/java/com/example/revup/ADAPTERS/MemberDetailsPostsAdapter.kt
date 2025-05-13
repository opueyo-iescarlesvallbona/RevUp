package com.example.revup.ADAPTERS

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.revup.ACTIVITIES.PostDetailsActivity
import com.example.revup.R
import com.example.revup._API.RevupCrudAPI
import com.example.revup._DATACLASS.FormatDate
import com.example.revup._DATACLASS.Post
import com.example.revup._DATACLASS.curr_post
import com.example.revup._DATACLASS.current_user
import com.example.revup._DATACLASS.toSimpleDateString
import com.google.android.material.dialog.MaterialAlertDialogBuilder

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
        if(list[position].id == -1){
            return
        }
        holder.title.setText(list[position].title)
        holder.date.setText(toSimpleDateString(FormatDate(list[position].postDate)))

        holder.postType.setOnClickListener{
            val intent = Intent(holder.vista.context, PostDetailsActivity::class.java)
            curr_post = list[position]
            holder.vista.context.startActivity(intent)
        }
        holder.vista.setOnClickListener {
            val intent = Intent(holder.vista.context, PostDetailsActivity::class.java)
            curr_post = list[position]
            holder.vista.context.startActivity(intent)
        }
        if(list[position].memberId == current_user!!.id){
            holder.deleteButton.visibility = View.VISIBLE
            holder.deleteButton.setOnClickListener {
                try{
                    MaterialAlertDialogBuilder(holder.vista.context)
                        .setTitle("Delete Post")
                        .setMessage("Do you want to delete this post?")
                        .setPositiveButton("Delete") { dialog, _ ->
                            val result = apiRevUp.deletePost(list[position].id, holder.vista.context)
                            if(result){
                                list.removeAt(position)
                                notifyItemRemoved(position)
                                notifyItemRangeChanged(position, list.size)
                                dialog.dismiss()
                            }else{
                                throw Exception("Error on delete post")
                            }
                        }
                        .setNegativeButton("Cancel") { dialog, _ ->
                            dialog.dismiss()
                        }
                        .show()
                }catch (e: Exception){
                    Toast.makeText(holder.vista.context, "Error on deleting route. $e.message", Toast.LENGTH_SHORT).show()
                }
            }
        }else{
            holder.deleteButton.visibility = View.INVISIBLE
        }
    }

}