package com.example.revup.ACTIVITIES

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.revup.ADAPTERS.PostDetailsAdapterRV
import com.example.revup.FRAGMENTS.LikesHomeFragment
import com.example.revup.R
import com.example.revup._API.RevupCrudAPI
import com.example.revup._DATACLASS.PostComment
import com.example.revup._DATACLASS.curr_post
import com.example.revup._DATACLASS.current_user
import com.example.revup._DATACLASS.hideKeyboard
import com.example.revup.databinding.ActivityPostDetailsBinding
import okhttp3.internal.notify
import java.time.LocalDateTime

class PostDetailsActivity : AppCompatActivity() {
    val apiRevUp = RevupCrudAPI()
    lateinit var binding: ActivityPostDetailsBinding
    lateinit var adapter: PostDetailsAdapterRV
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPostDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val imeVisible = insets.isVisible(WindowInsetsCompat.Type.ime())
            val imeInsets = insets.getInsets(WindowInsetsCompat.Type.ime())
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())

            v.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                if (imeVisible) imeInsets.bottom else systemBars.bottom
            )

            insets
        }

        binding.PostDetailsActivityBackButton.setOnClickListener {
            this.onBackPressed()
            val returnIntent = Intent()
            setResult(RESULT_OK, returnIntent)
            finish()
        }
        val post = curr_post

        binding.activityPostdetailsCommentButton.setOnClickListener {
            val comment = PostComment(postId = post!!.id, memberId = current_user!!.id, commentContent = binding.activityPostdetailsCommentText.text.toString(), datetime = LocalDateTime.now().toString())
            val uploaded = apiRevUp.postComments(comment, this)
            binding.activityPostdetailsCommentText.setText("")
            hideKeyboard(this)
            if(uploaded){
                comment.member = current_user
                post.postComments.add(comment)
                adapter.update(post.postComments.toMutableList())
                if(post.postComments.size>0){
                    binding.PostDetailsActivityRecyclerView.scrollToPosition(post.postComments.size-1)
                }
                Toast.makeText(this, "Comment uploaded", Toast.LENGTH_SHORT).show()
            }else {
                Toast.makeText(this, "There was an error uploading the comment", Toast.LENGTH_SHORT).show()
            }
        }




        if (post != null) {
            var comments: MutableList<PostComment>? = null
            try {
                comments = apiRevUp.getCommentsByPostId(post.id, this)

            } catch (e: Exception) {
                Toast.makeText(this, "Error getting comments", Toast.LENGTH_SHORT).show()
            }

            if (comments == null) {
                comments = mutableListOf()
            }
            post.postComments = comments.toMutableSet()
            for(c: PostComment in post.postComments){
                c.member = apiRevUp.getMemberById(c.memberId, this)
            }

            val layoutManager = LinearLayoutManager(this)
            layoutManager.setStackFromEnd(true)
            adapter = PostDetailsAdapterRV(post.postComments.toMutableList(), post)
            binding.PostDetailsActivityRecyclerView.adapter = adapter
            binding.PostDetailsActivityRecyclerView.layoutManager =
                LinearLayoutManager(this)
        } else {
            Toast.makeText(applicationContext, "Error getting post", Toast.LENGTH_SHORT)
                .show()
        }


    }
}
