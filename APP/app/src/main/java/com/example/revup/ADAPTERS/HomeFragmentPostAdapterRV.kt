package com.example.revup.ADAPTERS

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.example.revup.ACTIVITIES.EventDetailsActivity
import com.example.revup.ACTIVITIES.PostDetailsActivity
import com.example.revup.R
import com.example.revup._API.RevupCrudAPI
import com.example.revup._DATACLASS.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.time.LocalDateTime
import java.util.*
import java.util.concurrent.TimeUnit

class HomeFragmentPostAdapterRV(var list: MutableList<Post>) : RecyclerView.Adapter<HomeFragmentPostAdapterRV.ViewHolder>() {
    val apiRevUp = RevupCrudAPI()

    companion object {
        const val VIEW_TYPE_TEXT = 0
        const val VIEW_TYPE_IMAGE = 1
        const val VIEW_TYPE_ROUTE = 2
    }

    class ViewHolder(val vista: View) : RecyclerView.ViewHolder(vista), OnMapReadyCallback {
        val user = vista.findViewById<TextView>(R.id.cardview_post_mainactivity_username)
        val content = vista.findViewById<TextView>(R.id.cardview_post_mainactivity_contentText)
        val timeAgo = vista.findViewById<TextView>(R.id.cardview_post_mainactivity_timeAgo)
        val image = vista.findViewById<ImageView>(R.id.cardview_post_mainactivity_contentImage)
        val userImage = vista.findViewById<ImageView>(R.id.cardview_post_mainactivity_userPhoto)
        val likeDetails = vista.findViewById<ImageButton>(R.id.cardview_postdetails_like)
        val like = vista.findViewById<ImageButton>(R.id.cardview_post_mainactivity_likeButton)
        val animation = vista.findViewById<LottieAnimationView>(R.id.animation)
        val follow = vista.findViewById<TextView>(R.id.cardview_post_mainactivity_following)

        val mapView = vista.findViewById<MapView?>(R.id.cardview_post_mainactivity_contentRoute)
        var mMap: GoogleMap? = null
        var waypoints: MutableList<LatLng> = mutableListOf()

        val commentTextBtn = vista.findViewById<ImageButton>(R.id.cardview_post_mainactivity_commentButton)
        val commentText = vista.findViewById<TextInputEditText>(R.id.cardview_post_mainactivity_commentText)

        val spacer = vista.findViewById<View>(R.id.cardview_imagepost_spacer)

        var currentPost: Post? = null

        override fun onMapReady(map: GoogleMap) {
            mMap = map
            mMap?.uiSettings?.isZoomControlsEnabled = false
            drawRoute(map, waypoints)

            map.setOnMapClickListener {
                curr_post = currentPost

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
        val post = list[position]
        return when (post.postType) {
            3 -> VIEW_TYPE_ROUTE
            2 -> VIEW_TYPE_IMAGE
            else -> VIEW_TYPE_TEXT
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout = LayoutInflater.from(parent.context)
        val view = when (viewType) {
            VIEW_TYPE_IMAGE -> layout.inflate(R.layout.cardview_imagepost_mainactivity, parent, false)
            VIEW_TYPE_ROUTE -> layout.inflate(R.layout.cardview_routepost_mainactivity, parent, false)
            else -> layout.inflate(R.layout.cardview_textpost_mainactivity, parent, false)
        }
        return ViewHolder(view)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.currentPost = list[position]
        holder.likeDetails.visibility = View.GONE

        if (position == list.size - 1) {
            holder.spacer.layoutParams.height = 250
        }

        if (list[position].liked) {
            holder.animation.frame = 50
        }

        holder.animation.setAnimationFromUrl("https://lottie.host/c7f572a9-3d2f-4f8e-8b67-64e5f22595d7/eZvXIuktZb.lottie")
        holder.like.setOnClickListener {
            if (list[position].liked) {
                apiRevUp.postUnLike(current_user!!.id, list[position].id!!, holder.vista.context)
                Toast.makeText(holder.vista.context, "UnLiked", Toast.LENGTH_SHORT).show()
                holder.animation.speed = -1f
                holder.animation.playAnimation()
                holder.like.visibility = View.VISIBLE
                list[position].liked = false
            } else {
                apiRevUp.postLike(current_user!!.id, list[position].id!!, holder.vista.context)
                Toast.makeText(holder.vista.context, "Liked", Toast.LENGTH_SHORT).show()
                holder.animation.speed = 1f
                holder.animation.playAnimation()
                holder.like.visibility = View.GONE
                list[position].liked = true
            }
        }

        Glide.with(holder.vista.context).load(image_path + list[position].member!!.profilePicture).circleCrop().into(holder.userImage)
        holder.user.text = list[position].member!!.membername

        when (getItemViewType(position)) {
            VIEW_TYPE_TEXT -> {
                holder.content.text = list[position].description
            }
            VIEW_TYPE_IMAGE -> {
                Glide.with(holder.vista.context).load(image_path + list[position].picture).into(holder.image)
            }
            VIEW_TYPE_ROUTE -> {
                val route = try {
                    apiRevUp.getRoute(list[position].routeId!!, holder.vista.context)
                } catch (e: Exception) {
                    null
                }
                if (route != null) {
                    val type = object : TypeToken<MutableList<LatLng>>() {}.type
                    val wayp: MutableList<LatLng> = Gson().fromJson(route.waypoints, type)
                    holder.loadMap(wayp, list[position])
                }
            }
        }

        holder.commentTextBtn.setOnClickListener {
            val comment = PostComment(
                postId = list[position].id!!,
                memberId = current_user!!.id,
                commentContent = holder.commentText.text.toString(),
                datetime = LocalDateTime.now().toString()
            )
            if (apiRevUp.postComments(comment, holder.vista.context)) {
                Toast.makeText(holder.vista.context, "Comment uploaded", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(holder.vista.context, "There was an error uploading the comment", Toast.LENGTH_SHORT).show()
            }
            holder.commentText.setText("")
        }

        holder.timeAgo.text = getTimeAgo(FormatDate(list[position].postDate.toString()))

        holder.vista.setOnClickListener {
            val intent = Intent(holder.vista.context, PostDetailsActivity::class.java)
            curr_post = list[position]
            holder.vista.context.startActivity(intent)
        }

        val memberRelations = apiRevUp.getMemberRelationsByMemberId(current_user!!.id, holder.vista.context)
        val isFollowing = memberRelations?.any { it.memberId2 == list[position].member!!.id } == true

        holder.follow.text = if (isFollowing) "Following" else "Follow Up"
        holder.follow.visibility = if (list[position].member!!.id == current_user!!.id) View.GONE else View.VISIBLE

        holder.follow.setOnClickListener {
            if (!isFollowing) {
                try {
                    apiRevUp.postMemberRelation(MemberRelation(current_user!!.id, list[position].member!!.id, 1), holder.vista.context)
                    holder.follow.text = "Following"
                } catch (e: Exception) {
                    Toast.makeText(holder.vista.context, "Error on following. ${e.message}", Toast.LENGTH_SHORT).show()
                }
            } else {
                val relation = memberRelations?.find { it.memberId2 == list[position].member!!.id }
                if (relation != null) {
                    MaterialAlertDialogBuilder(holder.vista.context)
                        .setTitle("Unfollow ${list[position].member!!.membername}")
                        .setMessage("You are going to unfollow ${list[position].member!!.membername}. Are you sure?")
                        .setPositiveButton("Delete") { dialog, _ ->
                            if (apiRevUp.deleteMemberRelation(current_user!!.id, relation.memberId2, holder.vista.context)) {
                                holder.follow.text = "Follow Up"
                            } else {
                                Toast.makeText(holder.vista.context, "Error on unfollowing", Toast.LENGTH_SHORT).show()
                            }
                            dialog.dismiss()
                        }
                        .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
                        .show()
                } else {
                    Toast.makeText(holder.vista.context, "Error on unfollowing", Toast.LENGTH_SHORT).show()
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

        val calendarPost = Calendar.getInstance().apply { time = postDate }
        val calendarCurrent = Calendar.getInstance().apply { time = currentDate }

        val years = calendarCurrent.get(Calendar.YEAR) - calendarPost.get(Calendar.YEAR)
        val months = calendarCurrent.get(Calendar.MONTH) - calendarPost.get(Calendar.MONTH).let { if (it < 0) it + 12 else it }

        return when {
            years > 0 -> "$years year${if (years > 1) "s" else ""} ago"
            months > 0 -> "$months month${if (months > 1) "s" else ""} ago"
            days >= 1 -> "$days day${if (days > 1) "s" else ""} ago"
            hours >= 1 -> "$hours hour${if (hours > 1) "s" else ""} ago"
            minutes >= 1 -> "$minutes minute${if (minutes > 1) "s" else ""} ago"
            else -> "just now"
        }
    }
}
