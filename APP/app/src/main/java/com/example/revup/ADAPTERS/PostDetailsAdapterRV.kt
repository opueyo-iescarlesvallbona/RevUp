package com.example.revup.ADAPTERS

import android.graphics.Bitmap
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.revup.R
import com.example.revup._API.RevupCrudAPI
import com.example.revup._DATACLASS.FormatDate
import com.example.revup._DATACLASS.Post
import com.example.revup._DATACLASS.PostComment
import com.example.revup._DATACLASS.current_user
import com.example.revup._DATACLASS.image_path
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.util.Calendar
import java.util.Date
import java.util.concurrent.TimeUnit

class PostDetailsAdapterRV(var list: MutableList<PostComment>, var post: Post): RecyclerView.Adapter<PostDetailsAdapterRV.ViewHolder>() {
    val apiRevUp = RevupCrudAPI()
    companion object {
        const val VIEW_TYPE_COMMENT = 0
        const val VIEW_TYPE_POST_TEXT = 1
        const val VIEW_TYPE_POST_IMAGE = 2
    }

    class ViewHolder(val vista: View): RecyclerView.ViewHolder(vista) {
        val user = vista.findViewById<TextView>(R.id.cardview_post_mainactivity_username)
        val userImage = vista.findViewById<ImageView>(R.id.cardview_post_mainactivity_userPhoto)
        val content = vista.findViewById<TextView>(R.id.cardview_post_mainactivity_contentText)
        val timeAgo = vista.findViewById<TextView>(R.id.cardview_post_mainactivity_timeAgo)
        val image = vista.findViewById<ImageView>(R.id.cardview_post_mainactivity_contentImage)

        //COMETNS

        val commentText = vista.findViewById<TextView>(R.id.cardview_post_postdetails_commenttext)
        val commentTimeAgo = vista.findViewById<TextView>(R.id.cardview_post_postdetails_commenttimeAgo)
        val commentUser = vista.findViewById<TextView>(R.id.cardview_post_postdetails_commentusername)
        val commentUserPhoto = vista.findViewById<ImageView>(R.id.cardview_post_postdetails_commentuserPhoto)
        val delete = vista.findViewById<ImageView>(R.id.cardview_post_postdetails_commentdelete)
    }

    override fun getItemViewType(position: Int): Int {
        return if (position==0) {
            if (post.postType==1){
                VIEW_TYPE_POST_TEXT
            }else{
                VIEW_TYPE_POST_IMAGE
            }
        } else {
            VIEW_TYPE_COMMENT
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout = LayoutInflater.from(parent.context)
        return when (viewType) {
            VIEW_TYPE_POST_IMAGE -> {
                val view = layout.inflate(R.layout.cardview_imagepost_mainactivity, parent, false)
                ViewHolder(view)
            }
            VIEW_TYPE_POST_TEXT -> {
                val view = layout.inflate(R.layout.cardview_textpost_mainactivity, parent, false)
                ViewHolder(view)
            }
            else -> {
                val view = layout.inflate(R.layout.cardview_comment, parent, false)
                ViewHolder(view)
            }
        }
    }

    override fun getItemCount() = list.size+1

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(getItemViewType(position) == VIEW_TYPE_POST_IMAGE||getItemViewType(position) == VIEW_TYPE_POST_TEXT){

            //val member = apiRevUp.getMemberById(post.memberId, holder.vista.context)
            val member = post.member
            holder.user.setText(member!!.name)
            if(getItemViewType(position) == VIEW_TYPE_POST_TEXT){
                holder.content.setText(post.description)
                val layout = holder.vista.findViewById<LinearLayout>(R.id.cardview_text_post_commentlayout)
                layout.visibility = View.GONE
            }else{
                Glide.with(holder.vista.context).load(image_path+post.picture).into(holder.image)
                val layout = holder.vista.findViewById<LinearLayout>(R.id.cardview_image_post_commentlayout)
                layout.visibility = View.GONE
            }


            if(member!!.profilePicture!=null){
                Glide.with(holder.vista.context).load(image_path+member!!.profilePicture).circleCrop().into(holder.userImage)
            }else{
                //DEFAULT PHOTO
            }


        }else{
            val pos = position-1

            if(current_user!!.id!=list[pos].memberId){
                holder.delete.visibility = View.INVISIBLE
            }else{
                holder.delete.setOnClickListener {
                    MaterialAlertDialogBuilder(holder.vista.context)
                        .setTitle("Delete Comment")
                        .setMessage("Do you want to delete this comment?")
                        .setPositiveButton("Delete") { dialog, _ ->
                            apiRevUp.deleteComment(list[pos].id, holder.vista.context)
                            list.removeAt(pos)
                            notifyItemRemoved(position)
                            notifyItemRangeChanged(pos, list.size)
                            dialog.dismiss()
                        }
                        .setNegativeButton("Cancel") { dialog, _ ->
                            dialog.dismiss()
                        }
                        .show()

                }
            }


            //val member = apiRevUp.getMemberById(list[pos].memberId, holder.vista.context)
            val member = list[pos].member

            Glide.with(holder.vista.context).load(image_path+member!!.profilePicture).circleCrop().into(holder.commentUserPhoto)

            holder.commentUser.setText(member.membername.toString())
            holder.commentText.setText(list[pos].commentContent)
            holder.commentTimeAgo.setText(getTimeAgo(FormatDate(list[pos].datetime.toString())))
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