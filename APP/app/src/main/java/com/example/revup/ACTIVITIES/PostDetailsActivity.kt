package com.example.revup.ACTIVITIES

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.revup.ADAPTERS.PostDetailsAdapterRV
import com.example.revup.R
import com.example.revup._API.RevupCrudAPI
import com.example.revup._DATACLASS.PostComment
import com.example.revup.databinding.ActivityPostDetailsBinding

class PostDetailsActivity : AppCompatActivity() {
    val apiRevUp = RevupCrudAPI()
    lateinit var binding: ActivityPostDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        binding = ActivityPostDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val postId = intent.getIntExtra("postId", -1)

        if (postId != -1) {
            val post = apiRevUp.getPostById(postId, applicationContext)

            if (post != null) {
                Log.i("Post", postId.toString())
                post.postComments.add(PostComment(0, 5, 5, "HOPA", "2020-02-02"))
                post.postComments.add(PostComment(1, 5, 5, "HOPA2", "2020-02-03"))
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
}
