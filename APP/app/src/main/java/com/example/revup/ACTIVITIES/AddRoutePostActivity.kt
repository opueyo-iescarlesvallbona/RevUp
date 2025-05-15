package com.example.revup.ACTIVITIES

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.revup.R
import com.example.revup._API.RevupCrudAPI
import com.example.revup._DATACLASS.MemberLocation
import com.example.revup._DATACLASS.Municipality
import com.example.revup._DATACLASS.Post
import com.example.revup._DATACLASS.Route
import com.example.revup._DATACLASS.current_user
import com.example.revup.databinding.ActivityAddRoutePostBinding
import java.time.LocalDateTime

class AddRoutePostActivity : AppCompatActivity() {
    lateinit var binding : ActivityAddRoutePostBinding
    var apiRevUp = RevupCrudAPI()
    var routes: MutableList<Route>? = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAddRoutePostBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.addRoutePostActivityBtnCancel.setOnClickListener{
            this.onBackPressed()
            val returnIntent = Intent()
            setResult(RESULT_OK, returnIntent)
            finish()
        }

        binding.addRoutePostActivityBtnSave.setOnClickListener{
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

        val nameTextFieldRoute: AutoCompleteTextView = binding.addRoutePostActivityRouteText

        try{
            routes = apiRevUp.getAllRoutesByMember(current_user!!.id, this)
            ArrayAdapter(this, android.R.layout.simple_list_item_1, routes!!.map { it.name }).also { adapter ->
                nameTextFieldRoute.setAdapter(adapter)
            }
        }catch (e: Exception){
            Toast.makeText(this, "Error getting routes: $e", Toast.LENGTH_LONG).show()
        }

    }

    fun checkParams(): Post? {
        var title: String? = null
        var description: String? = null
        var routeId: Int? = null


        if(binding.addRoutePostActivityTitle.text.toString() != ""){
            title = binding.addRoutePostActivityTitle.text.toString()
        }else{
            Toast.makeText(this, "Please introduce a title", Toast.LENGTH_LONG).show()
            return null
        }

        if(binding.addRoutePostActivityRouteText.text.toString() == ""){
            Toast.makeText(this, "Please select a route", Toast.LENGTH_LONG).show()
            return null
        }else{
            if(routes != null){
                for(route in routes!!){
                    if(route.name == binding.addRoutePostActivityRouteText.text.toString()){
                        routeId = route.id
                    }
                }
            }
        }

        return Post(id = null, title = title, description = description, postDate = LocalDateTime.now().toString(), routeId = routeId)
    }
}