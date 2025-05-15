package com.example.revup.ACTIVITIES

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.revup.ADAPTERS.ClubDetailsMembersAdapter
import com.example.revup.ADAPTERS.ClubSearchAdapter
import com.example.revup.R
import com.example.revup._API.RevupCrudAPI
import com.example.revup._DATACLASS.Club
import com.example.revup._DATACLASS.FormatDate
import com.example.revup._DATACLASS.Member
import com.example.revup._DATACLASS.MemberClub
import com.example.revup._DATACLASS.MemberRelation
import com.example.revup._DATACLASS.curr_club
import com.example.revup._DATACLASS.current_user
import com.example.revup._DATACLASS.image_path
import com.example.revup._DATACLASS.toSimpleDateString
import com.example.revup.databinding.ActivityClubDetailsBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class ClubDetailsActivity : AppCompatActivity() {
    lateinit var binding : ActivityClubDetailsBinding
    val apiRevUp = RevupCrudAPI()
    var members: MutableList<Member>? = null

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
        val club = curr_club

        binding.clubDetailsActivityBackButton.setOnClickListener{
            this.onBackPressed()
            val returnIntent = Intent()
            setResult(RESULT_OK, returnIntent)
            finish()
        }

        binding.clubDetailsActivityBackButtonText.setOnClickListener {
            this.onBackPressed()
            val returnIntent = Intent()
            setResult(RESULT_OK, returnIntent)
            finish()
        }

        binding.clubDetailsActivityMemberRelationWithClub.setOnClickListener{
            if(binding.clubDetailsActivityMemberRelationWithClub.text == "Join in"){
                try{
                    //apiRevUp.postMember(MemberRelation(current_user!!.id, member!!.id, 1), this)
                    binding.clubDetailsActivityMemberRelationWithClub.setText("Joined in")
                    binding.clubDetailsActivityMemberRelationWithClub.setTextColor(resources.getColor(R.color.memberRelation_Friend))
                    //memberRelation = MemberRelation(current_user!!.id, member!!.id, 1)
                }catch(e: Exception){
                    Toast.makeText(this, "Error on joining in. $e.message", Toast.LENGTH_SHORT).show()
                }
            }else if(binding.clubDetailsActivityMemberRelationWithClub.text == "Joined in"){
                try{
                    if(current_user!! in members!!){
                        MaterialAlertDialogBuilder(this)
                            .setTitle("Leave ${club!!.name}")
                            .setMessage("You are going to leave ${club!!.name}. Are you sure?")
                            .setPositiveButton("Leave") { dialog, _ ->
//                                var result = apiRevUp.deleteMemberRelation(current_user!!.id, memberRelation!!.memberId2, this)
//                                if(result){
//                                    binding.memberDetailsActivityMemberRelation.setText("Follow Up")
//                                    binding.memberDetailsActivityMemberRelation.setTextColor(resources.getColor(R.color.memberRelation_NoFriend))
//                                }else{
//                                    throw Exception("Error on unfollowing")
//                                }
                                dialog.dismiss()
                            }
                            .setNegativeButton("Cancel") { dialog, _ ->
                                dialog.dismiss()
                            }
                            .show()
                    }else{
                        Toast.makeText(this, "Error on leaving", Toast.LENGTH_SHORT).show()
                    }
                }catch(e: Exception){
                    Toast.makeText(this, "Error on leaving. $e.message", Toast.LENGTH_SHORT).show()
                }
            }
        }

        if (club != null){
            try {
                curr_club = club
                if(club.picture != null && club.picture != ""){
                    Glide.with(this).load(image_path+club.picture).circleCrop().into(binding.clubDetailsActivityPicture)
                }else{
                    binding.clubDetailsActivityPicture.setImageResource(R.drawable.baseline_account_circle_24)
                }
                binding.clubDetailsActivityName.text = club.name
                binding.clubDetailsActivityDate.text = "Creation date: ${toSimpleDateString(FormatDate(club.creationDate))}"
                if(club.description != null){
                    binding.clubDetailsActivityDescription.text = club.description.toString()
                }else{
                    binding.clubDetailsActivityDescription.text = ""
                }

                val founder = apiRevUp.getMemberById(club.founder, this)
                if(founder != null){
                    binding.clubDetailsActivityFounder.text = "Founder: ${founder.name}"
                }

                val members = apiRevUp.getMembersByClub(club.id!!, this)
                if(members != null){
                    val recyclerView = binding.clubDetailsActivityMembersRecyclerView
                    recyclerView.adapter = ClubDetailsMembersAdapter(members)
                    recyclerView.layoutManager = LinearLayoutManager(this)

                    if (current_user!! in members){
                        //TODO check member role, could be blocked
                        binding.clubDetailsActivityMemberRelationWithClub.setText("Joined in")
                        binding.clubDetailsActivityMemberRelationWithClub.setTextColor(resources.getColor(R.color.memberRelation_Friend))
                    }else{
                        binding.clubDetailsActivityMemberRelationWithClub.setText("Join in")
                        binding.clubDetailsActivityMemberRelationWithClub.setTextColor(resources.getColor(R.color.memberRelation_NoFriend))
                    }
                }
            }catch (e: Exception){
                Toast.makeText(this, "Error on club details. $e.message", Toast.LENGTH_SHORT).show()
            }
        }
    }
}