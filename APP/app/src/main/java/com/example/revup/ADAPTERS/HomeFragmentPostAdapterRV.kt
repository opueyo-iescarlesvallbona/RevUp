package com.example.revup.ADAPTERS

import android.R.attr.alpha
import android.R.attr.visibility
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Space
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.NotificationCompat.getColor
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.example.revup.ACTIVITIES.PostDetailsActivity
import com.example.revup.R
import com.example.revup._API.RevupCrudAPI
import com.example.revup._DATACLASS.FormatDate
import com.example.revup._DATACLASS.MemberRelation
import com.example.revup._DATACLASS.Post
import com.example.revup._DATACLASS.PostComment
import com.example.revup._DATACLASS.curr_post
import com.example.revup._DATACLASS.current_user
import com.example.revup._DATACLASS.image_path
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import org.w3c.dom.Text
import java.time.LocalDateTime
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
        val likeDetails = vista.findViewById<ImageButton>(R.id.cardview_postdetails_like)
        val like = vista.findViewById<ImageButton>(R.id.cardview_post_mainactivity_likeButton)
        val animation = vista.findViewById<LottieAnimationView>(R.id.animation)
        val follow = vista.findViewById<TextView>(R.id.cardview_post_mainactivity_following)

        val commentImageBtn = vista.findViewById<ImageButton>(R.id.cardview_imagepost_mainactivity_commentButton)
        val commentImage = vista.findViewById<TextInputEditText>(R.id.cardview_imagepost_mainactivity_commentText)

        val commentTextBtn = vista.findViewById<ImageButton>(R.id.cardview_textpost_mainactivity_commentButton)
        val commentText = vista.findViewById<TextInputEditText>(R.id.cardview_textpost_mainactivity_commentText)


        val spacer = vista.findViewById<View>(R.id.cardview_imagepost_spacer)

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
        holder.likeDetails.visibility = View.GONE

        if(position==list.size-1){
            holder.spacer.layoutParams.height = 250
        }
        if(list[position].liked){
            holder.animation.frame = 50
        }


        holder.animation.setAnimationFromUrl("https://lottie.host/c7f572a9-3d2f-4f8e-8b67-64e5f22595d7/eZvXIuktZb.lottie")
        holder.like.setOnClickListener {
            if(list[position].liked){
                apiRevUp.postUnLike(current_user!!.id, list[position].id!!, holder.vista.context)
                Toast.makeText(holder.vista.context, "UnLiked", Toast.LENGTH_SHORT).show()
                holder.animation.speed = -1f

                holder.like.animate()
                    .setDuration(100)
                    .withEndAction {
                        holder.like.visibility = View.VISIBLE
                    }
                holder.animation.playAnimation()

                list[position].liked = false
            }else{
                apiRevUp.postLike(current_user!!.id, list[position].id!!, holder.vista.context)
                Toast.makeText(holder.vista.context, "Liked", Toast.LENGTH_SHORT).show()
                holder.animation.speed = 1f
                holder.animation.playAnimation()
                holder.like.animate()
                    .setDuration(200)
                    .withEndAction {
                        holder.like.visibility = View.GONE
                    }
                list[position].liked = true

            }
        }

        Glide.with(holder.vista.context).load(image_path+list[position].member!!.profilePicture).circleCrop().into(holder.userImage)
        holder.user.setText(list[position].member!!.membername)
        if(getItemViewType(position) == VIEW_TYPE_TEXT){
            holder.content.setText(list[position].description)
            holder.commentTextBtn.setOnClickListener {
                val comment = PostComment(postId = list[position].id!!, memberId = current_user!!.id, commentContent = holder.commentText.text.toString(), datetime = LocalDateTime.now().toString())
                if(apiRevUp.postComments(comment, holder.vista.context)){
                    Toast.makeText(holder.vista.context, "Comment uploaded", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(holder.vista.context, "There was an error uploading the comment", Toast.LENGTH_SHORT).show()
                }
                holder.commentText.setText("")

            }
        }else if(getItemViewType(position) == VIEW_TYPE_IMAGE){
            Glide.with(holder.vista.context).load(image_path+list[position].picture).into(holder.image)
            holder.commentImageBtn.setOnClickListener {
                val comment = PostComment(postId = list[position].id!!, memberId = current_user!!.id, commentContent = holder.commentImage.text.toString(), datetime = LocalDateTime.now().toString())
                if (apiRevUp.postComments(comment, holder.vista.context)){
                    Toast.makeText(holder.vista.context, "Comment uploaded", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(holder.vista.context, "There was an error uploading the comment", Toast.LENGTH_SHORT).show()
                }
                holder.commentImage.setText("")
            }
        }

        holder.timeAgo.setText(getTimeAgo(FormatDate(list[position].postDate.toString())))

        holder.vista.setOnClickListener{
            val intent = Intent(holder.vista.context, PostDetailsActivity::class.java)
            curr_post = list[position]
            holder.vista.context.startActivity(intent)

        }
        var memberRelation: MemberRelation? = null

        var member_relations = apiRevUp.getMemberRelationsByMemberId(current_user!!.id, holder.vista.context)
        if (member_relations != null){
            val member_relation = member_relations!!.find{it.memberId2 == list[position].member!!.id}
            if(member_relation != null) {
                memberRelation = member_relation
                holder.follow.setText("Following")
                //holder.follow.setTextColor(resources.getColor(R.color.memberRelation_Friend))
            }else{
                holder.follow.setText("Follow Up")
                //holder.follow.setTextColor(resources.getColor(R.color.memberRelation_NoFriend))
            }
        }else{
            holder.follow.setText("Follow Up")
            //holder.follow.setTextColor(resources.getColor(R.color.memberRelation_NoFriend))
        }
        if(list[position].member!!.id==current_user!!.id){
            holder.follow.visibility = View.GONE
        }else{
            holder.follow.visibility = View.VISIBLE
        }

        holder.follow.setOnClickListener {
            if(holder.follow.text == "Follow Up"){
                try{
                    apiRevUp.postMemberRelation(MemberRelation(current_user!!.id, list[position].member!!.id, 1), holder.vista.context)
                    holder.follow.setText("Following")
                    //holder.follow.setTextColor(getColor(R.color.memberRelation_Friend))
                    memberRelation = MemberRelation(current_user!!.id, list[position].member!!.id, 1)
                }catch(e: Exception){
                    Toast.makeText(holder.vista.context, "Error on following. $e.message", Toast.LENGTH_SHORT).show()
                }
            }else{
                try{
                    if(memberRelation != null){
                        MaterialAlertDialogBuilder(holder.vista.context)
                            .setTitle("Unfollow ${list[position].member!!.membername}")
                            .setMessage("You are going to unfollow ${list[position].member!!.membername}. Are you sure?")
                            .setPositiveButton("Delete") { dialog, _ ->
                                var result = apiRevUp.deleteMemberRelation(current_user!!.id, memberRelation!!.memberId2, holder.vista.context)
                                if(result){
                                    holder.follow.setText("Follow Up")
                                    //holder.follow.setTextColor(resources.getColor(R.color.memberRelation_NoFriend))
                                }else{
                                    throw Exception("Error on unfollowing")
                                }
                                dialog.dismiss()
                            }
                            .setNegativeButton("Cancel") { dialog, _ ->
                                dialog.dismiss()
                            }
                            .show()
                    }else{
                        Toast.makeText(holder.vista.context, "Error on unfollowing", Toast.LENGTH_SHORT).show()
                    }
                }catch(e: Exception){
                    Toast.makeText(holder.vista.context, "Error on unfollowing. $e.message", Toast.LENGTH_SHORT).show()
                }
            }
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