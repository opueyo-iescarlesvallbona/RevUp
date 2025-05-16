package com.example.revup.ACTIVITIES

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.revup.R
import com.example.revup._API.RevupCrudAPI
import com.example.revup._DATACLASS.Post
import com.example.revup._DATACLASS.current_user
import com.example.revup.databinding.ActivityAddTextPostBinding
import java.time.LocalDateTime


class AddTextPostActivity : AppCompatActivity() {
    lateinit var binding : ActivityAddTextPostBinding
    var apiRevUp = RevupCrudAPI()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAddTextPostBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.addTextPostActivityBtnCancel.setOnClickListener{
            this.onBackPressed()
            val returnIntent = Intent()
            setResult(RESULT_OK, returnIntent)
            finish()
        }

        binding.addTextPostActivityBtnSave.setOnClickListener{
            var post = checkParams()
            if(post != null){
                try{
                    var result = apiRevUp.postPost(post, null, this)
                    if(result){
                        Toast.makeText(this, "Post created", Toast.LENGTH_LONG).show()
                        val returnIntent = Intent()
                        setResult(RESULT_OK, returnIntent)
                        finish()
                    }
                }catch (e: Exception){
                    Toast.makeText(this, "Error posting post. ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }

    }

    fun checkParams(): Post? {
        var title: String
        var description: String


        if(binding.addTextPostActivityTitle.text.toString() != ""){
            title = binding.addTextPostActivityTitle.text.toString()
        }else{
            Toast.makeText(this, "Please introduce a title", Toast.LENGTH_LONG).show()
            return null
        }

        if(binding.addTextPostActivityDescription.text.toString() != ""){
            description = binding.addTextPostActivityDescription.text.toString()
        }else{
            Toast.makeText(this, "Please introduce a description", Toast.LENGTH_LONG).show()
            return null
        }

        return Post(id = null, title = title, description = description, postDate = LocalDateTime.now().toString(), postType = 3, memberId = current_user!!.id)
    }
}