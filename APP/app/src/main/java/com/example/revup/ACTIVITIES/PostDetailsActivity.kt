package com.example.revup.ACTIVITIES

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.revup.ADAPTERS.PostDetailsAdapterRV
import com.example.revup.R
import com.example.revup._API.RevupCrudAPI
import com.example.revup._DATACLASS.PostComment
import com.example.revup._DATACLASS.curr_post
import com.example.revup.databinding.ActivityPostDetailsBinding

class PostDetailsActivity : AppCompatActivity() {
    val apiRevUp = RevupCrudAPI()
    lateinit var binding: ActivityPostDetailsBinding
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
            binding.PostDetailsActivityRecyclerView.adapter =
                PostDetailsAdapterRV(post.postComments.toMutableList(), post)
            binding.PostDetailsActivityRecyclerView.layoutManager =
                LinearLayoutManager(this)
        } else {
            Toast.makeText(applicationContext, "Error getting post", Toast.LENGTH_SHORT)
                .show()
        }


    }
}
