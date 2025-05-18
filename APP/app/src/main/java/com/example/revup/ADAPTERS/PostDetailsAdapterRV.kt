package com.example.revup.ADAPTERS

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.Calendar
import java.util.Date
import java.util.concurrent.TimeUnit

class PostDetailsAdapterRV(var list: MutableList<PostComment>, var post: Post): RecyclerView.Adapter<PostDetailsAdapterRV.ViewHolder>() {
    val apiRevUp = RevupCrudAPI()
    companion object {
        const val VIEW_TYPE_COMMENT = 0
        const val VIEW_TYPE_POST_TEXT = 1
        const val VIEW_TYPE_POST_IMAGE = 2
        const val VIEW_TYPE_POST_ROUTE = 3
    }

    fun update(newItems: MutableList<PostComment>){
        list = newItems
        notifyDataSetChanged()
    }

    class ViewHolder(val vista: View): RecyclerView.ViewHolder(vista), OnMapReadyCallback {
        val user = vista.findViewById<TextView>(R.id.cardview_post_mainactivity_username)
        val userImage = vista.findViewById<ImageView>(R.id.cardview_post_mainactivity_userPhoto)
        val content = vista.findViewById<TextView>(R.id.cardview_post_mainactivity_contentText)
        val timeAgo = vista.findViewById<TextView>(R.id.cardview_post_mainactivity_timeAgo)
        val image = vista.findViewById<ImageView>(R.id.cardview_post_mainactivity_contentImage)
        val animation = vista.findViewById<LottieAnimationView>(R.id.animation_like_imagepost_details)
        val like = vista.findViewById<ImageButton>(R.id.cardview_postdetails_like)
        val follow = vista.findViewById<TextView>(R.id.cardview_post_mainactivity_following)

        //COMMENTS

        val commentText = vista.findViewById<TextView>(R.id.cardview_post_postdetails_commenttext)
        val commentTimeAgo = vista.findViewById<TextView>(R.id.cardview_post_postdetails_commenttimeAgo)
        val commentUser = vista.findViewById<TextView>(R.id.cardview_post_postdetails_commentusername)
        val commentUserPhoto = vista.findViewById<ImageView>(R.id.cardview_post_postdetails_commentuserPhoto)
        val delete = vista.findViewById<ImageView>(R.id.cardview_post_postdetails_commentdelete)

        //ROUTE
        val mapView = vista.findViewById<MapView?>(R.id.cardview_post_mainactivity_contentRoute)
        var mMap: GoogleMap? = null
        var waypoints: MutableList<LatLng> = mutableListOf()

        override fun onMapReady(map: GoogleMap) {
            mMap = map
            mMap?.uiSettings?.isZoomControlsEnabled = false
            drawRoute(map, waypoints)

            map.setOnMapClickListener {
                curr_post = (vista.context as? AppCompatActivity)?.let {
                    (it as? HomeFragmentPostAdapterRV)?.list?.get(position!!.toInt())
                }

                val intent = Intent(vista.context, PostDetailsActivity::class.java)
                vista.context.startActivity(intent)
            }

            val central = LatLng(
                (waypoints.first().latitude + waypoints.last().latitude) / 2,
                (waypoints.first().longitude + waypoints.last().longitude) / 2
            )

            map.addMarker(MarkerOptions().position(waypoints.first()).title(""))
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(central, 13.0f))

            map.addMarker(MarkerOptions().position(waypoints.last()).title(""))
        }

        private fun drawRoute(map: GoogleMap, coordenades: MutableList<LatLng>) {
            val polyLineOptions = PolylineOptions()
            for (cord in coordenades) {
                polyLineOptions.add(cord)
            }
            (vista.context as? Activity)?.runOnUiThread {
                map.addPolyline(polyLineOptions)
            }
        }

        fun loadMap(wayp: MutableList<LatLng>, post: Post) {
            waypoints = wayp
            mapView?.onCreate(null)
            mapView?.onResume()
            mapView?.getMapAsync(this)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position==0) {
            if (post.postType==1){
                VIEW_TYPE_POST_TEXT
            }else if(post.postType==2){
                VIEW_TYPE_POST_IMAGE
            }else{
                VIEW_TYPE_POST_ROUTE
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
            VIEW_TYPE_POST_ROUTE -> {
                val view = layout.inflate(R.layout.cardview_routepost_mainactivity, parent, false)
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
        if(getItemViewType(position) == VIEW_TYPE_POST_IMAGE||getItemViewType(position) == VIEW_TYPE_POST_TEXT||getItemViewType(position)==VIEW_TYPE_POST_ROUTE){
            if(post.liked){
                holder.animation.frame = 50
            }

            var memberRelation: MemberRelation? = null

            var member_relations = apiRevUp.getMemberRelationsByMemberId(current_user!!.id, holder.vista.context)
            if (member_relations != null){
                Log.i("MEMBER_RELATIONS", list.size.toString()+"    "+position)
                val member_relation = member_relations!!.find{it.memberId2 == post.member!!.id}
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
            if(post.member!!.id==current_user!!.id){
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


            holder.animation.setAnimationFromUrl("https://lottie.host/c7f572a9-3d2f-4f8e-8b67-64e5f22595d7/eZvXIuktZb.lottie")
            holder.like.setOnClickListener {
                if(post.liked){
                    apiRevUp.postUnLike(current_user!!.id, post.id!!, holder.vista.context)
                    Toast.makeText(holder.vista.context, "UnLiked", Toast.LENGTH_SHORT).show()
                    holder.animation.speed = -1f

                    holder.like.animate()
                        .setDuration(100)
                        .withEndAction {
                            holder.like.visibility = View.VISIBLE
                        }
                    holder.animation.playAnimation()

                    post.liked = false
                    curr_post!!.liked = false
                }else{
                    apiRevUp.postLike(current_user!!.id, post.id!!, holder.vista.context)
                    Toast.makeText(holder.vista.context, "Liked", Toast.LENGTH_SHORT).show()
                    holder.animation.speed = 1f
                    holder.animation.playAnimation()
                    holder.like.animate()
                        .setDuration(200)
                        .withEndAction {
                            holder.like.visibility = View.GONE
                        }
                    post.liked = true
                    curr_post!!.liked = true

                }
            }
            val member = post.member
            holder.user.setText(member!!.membername)
            if(getItemViewType(position) == VIEW_TYPE_POST_TEXT){
                holder.content.setText(post.description)
            }else if(getItemViewType(position)==VIEW_TYPE_POST_IMAGE){
                Glide.with(holder.vista.context).load(image_path+post.picture).into(holder.image)
            }else{
                val route = try {
                    apiRevUp.getRoute(post.routeId!!, holder.vista.context)
                } catch (e: Exception) {
                    null
                }
                if (route != null) {
                    val type = object : TypeToken<MutableList<LatLng>>() {}.type
                    val wayp: MutableList<LatLng> = Gson().fromJson(route.waypoints, type)
                    holder.loadMap(wayp, post)
                }
            }

            val layout = holder.vista.findViewById<LinearLayout>(R.id.cardview_post_commentlayout)
            layout.visibility = View.GONE


            if(member!!.profilePicture!=null){
                Glide.with(holder.vista.context).load(image_path+member!!.profilePicture).circleCrop().into(holder.userImage)
            }else{
                holder.userImage.setImageResource(R.drawable.default_profile_picture)
            }

            try{
                holder.timeAgo.setText(getTimeAgo(FormatDate(post.postDate.toString())))
            }catch(e: Exception){
                Toast.makeText(holder.vista.context, "Error getting time ago", Toast.LENGTH_SHORT).show()
            }


        }else{
            val pos = position-1

            if(current_user!!.id!=list[pos].memberId){
                holder.delete.visibility = View.INVISIBLE
            }else {
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

            val member = list[pos].member

            if(member!!.profilePicture!=null){
                Glide.with(holder.vista.context).load(image_path+member!!.profilePicture).circleCrop().into(holder.commentUserPhoto)
            }else{
                holder.commentUserPhoto.setImageResource(R.drawable.default_profile_picture)
            }


            holder.commentUser.setText(member.membername.toString())
            holder.commentText.setText(list[pos].commentContent)
            Log.i("COMMENT", list[pos].id.toString())
            try{
                holder.commentTimeAgo.setText(getTimeAgo(FormatDate(list[pos].datetime.toString())))
            }catch(e: Exception){
                Toast.makeText(holder.vista.context, "Error getting time ago", Toast.LENGTH_SHORT).show()
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