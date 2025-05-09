package com.example.revup.ACTIVITIES

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.revup.ADAPTERS.ClubDetailsMembersAdapter
import com.example.revup.ADAPTERS.ClubSearchAdapter
import com.example.revup.R
import com.example.revup._API.RevupCrudAPI
import com.example.revup._DATACLASS.Club
import com.example.revup._DATACLASS.FormatDate
import com.example.revup.databinding.ActivityClubDetailsBinding

class ClubDetailsActivity : AppCompatActivity() {
    lateinit var binding : ActivityClubDetailsBinding
    val apiRevUp = RevupCrudAPI()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityClubDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val club = intent.getParcelableExtra<Club>("club")

        if (club != null){
            try {
                //TODO get image
                binding.clubDetailsActivityName.text = club.name
                binding.clubDetailsActivityDate.text = FormatDate(club.creationDate).toString()
                if(club.description != null){
                    binding.clubDetailsActivityDescription.text = club.description.toString()
                }else{
                    binding.clubDetailsActivityDescription.text = ""
                }

                val founder = apiRevUp.getMemberById(club.founder, this)
                if(founder != null){
                    binding.clubDetailsActivityFounder.text = founder.name
                }

                val members = apiRevUp.getMembersByClub(club.id, this)
                if(members != null){
                    val recyclerView = binding.clubDetailsActivityMembersRecyclerView
                    recyclerView.adapter = ClubDetailsMembersAdapter(members)
                    recyclerView.layoutManager = LinearLayoutManager(this)
                }
            }catch (e: Exception){
                Toast.makeText(this, "Error on club details. $e.message", Toast.LENGTH_SHORT).show()
            }
        }
    }
}