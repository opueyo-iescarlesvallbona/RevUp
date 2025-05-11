package com.example.revup.ADAPTERS

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.revup.ACTIVITIES.PostDetailsActivity
import com.example.revup.R
import com.example.revup._API.RevupCrudAPI
import com.example.revup._DATACLASS.FormatDate
import com.example.revup._DATACLASS.Post
import com.example.revup._DATACLASS.curr_post
import com.example.revup._DATACLASS.image_path
import org.w3c.dom.Text
import java.util.Calendar
import java.util.Date
import java.util.concurrent.TimeUnit

class HomeFragmentPostAdapterRV(var list: MutableList<Post>): RecyclerView.Adapter<HomeFragmentPostAdapterRV.ViewHolder>() {
    val apiRevUp = RevupCrudAPI()
    companion object {
        const val VIEW_TYPE_TEXT = 0
        const val VIEW_TYPE_IMAGE = 1
        const val VIEW_TYPE_ROUTE = 2
    }

    class ViewHolder(val vista: View): RecyclerView.ViewHolder(vista) {
        val user = vista.findViewById<TextView>(R.id.cardview_post_mainactivity_username)
        val content = vista.findViewById<TextView>(R.id.cardview_post_mainactivity_contentText)
        val timeAgo = vista.findViewById<TextView>(R.id.cardview_post_mainactivity_timeAgo)
        val image = vista.findViewById<ImageView>(R.id.cardview_post_mainactivity_contentImage)
        val userImage = vista.findViewById<ImageView>(R.id.cardview_post_mainactivity_userPhoto)
    }

    override fun getItemViewType(position: Int): Int {
        val post = list[position]
        return when(post.postType) {
            1 -> VIEW_TYPE_TEXT
            2 -> VIEW_TYPE_IMAGE
            else -> VIEW_TYPE_ROUTE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout = LayoutInflater.from(parent.context)
        return when (viewType) {
            VIEW_TYPE_IMAGE -> {
                val view = layout.inflate(R.layout.cardview_imagepost_mainactivity, parent, false)
                ViewHolder(view)
            }
            else -> {
                val view = layout.inflate(R.layout.cardview_textpost_mainactivity, parent, false)
                ViewHolder(view)
            }
        }
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //val member = apiRevUp.getMemberById(list[position].memberId, holder.vista.context)
        Glide.with(holder.vista.context).load(image_path+list[position].member!!.profilePicture).circleCrop().into(holder.userImage)
        holder.user.setText(list[position].member!!.name)
        if(getItemViewType(position) == VIEW_TYPE_TEXT){
            holder.content.setText(list[position].description)
        }else if(getItemViewType(position) == VIEW_TYPE_IMAGE){
            Glide.with(holder.vista.context).load(image_path+list[position].picture).into(holder.image)
        }

        holder.timeAgo.setText(getTimeAgo(FormatDate(list[position].postDate.toString())))

        holder.vista.setOnClickListener{
            val intent = Intent(holder.vista.context, PostDetailsActivity::class.java)
            curr_post = list[position]
            holder.vista.context.startActivity(intent)

        }
    }

    fun getTimeAgo(postDate: Date): String {
        val currentDate = Date()

        val diffInMillis = currentDate.time - postDate.time

        val days = TimeUnit.MILLISECONDS.toDays(diffInMillis)
        val hours = TimeUnit.MILLISECONDS.toHours(diffInMillis) % 24
        val minutes = TimeUnit.MILLISECONDS.toMinutes(diffInMillis) % 60

        val calendarPost = Calendar.getInstance()
        calendarPost.time = postDate
        val calendarCurrent = Calendar.getInstance()
        calendarCurrent.time = currentDate

        val years = calendarCurrent.get(Calendar.YEAR) - calendarPost.get(Calendar.YEAR)
        val months = calendarCurrent.get(Calendar.MONTH) - calendarPost.get(Calendar.MONTH)

        val adjustedMonths = if (months < 0) {
            months + 12
        } else {
            months
        }

        return when {
            years > 0 -> "$years year${if (years > 1) "s" else ""} ago"
            adjustedMonths > 0 -> "$adjustedMonths month${if (adjustedMonths > 1) "s" else ""} ago"
            days >= 1 -> "$days day${if (days > 1) "s" else ""} ago"
            hours >= 1 -> "$hours hour${if (hours > 1) "s" else ""} ago"
            minutes >= 1 -> "$minutes minute${if (minutes > 1) "s" else ""} ago"
            else -> "just now"
        }
    }
}