package com.example.revup.ACTIVITIES

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
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
import com.example.revup._DATACLASS.recreated
import com.example.revup._DATACLASS.toSimpleDateString
import com.example.revup.databinding.ActivityClubDetailsBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.time.LocalDate
import java.time.LocalDateTime

class ClubDetailsActivity : AppCompatActivity() {
    lateinit var binding : ActivityClubDetailsBinding
    val apiRevUp = RevupCrudAPI()
    var members: MutableList<Member>? = null
    var recyclerView: RecyclerView? = null

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
        recreated = true

        binding.clubDetailsActivityBackButton.setOnClickListener{
            this.onBackPressed()
            curr_club = null
            val returnIntent = Intent()
            setResult(RESULT_OK, returnIntent)
            finish()
        }

        binding.clubDetailsActivityBackButtonText.setOnClickListener {
            this.onBackPressed()
            curr_club = null
            val returnIntent = Intent()
            setResult(RESULT_OK, returnIntent)
            finish()
        }

        binding.clubDetailsActivityMemberRelationWithClub.setOnClickListener{
            if(binding.clubDetailsActivityMemberRelationWithClub.text == "Join in"){
                try{
                    apiRevUp.postMemberClub(MemberClub(current_user!!.id!!, curr_club!!.id!!, 3, LocalDate.now().toString()), this)
                    binding.clubDetailsActivityMemberRelationWithClub.setText("Joined in")
                    binding.clubDetailsActivityMemberRelationWithClub.setTextColor(resources.getColor(R.color.memberRelation_Friend))
                    (recyclerView!!.adapter as ClubDetailsMembersAdapter).list.add(current_user!!)
                    recyclerView!!.adapter!!.notifyDataSetChanged()
                }catch(e: Exception){
                    Toast.makeText(this, "Error on joining in. $e.message", Toast.LENGTH_SHORT).show()
                }
            }else if(binding.clubDetailsActivityMemberRelationWithClub.text == "Joined in"){
                try{
                    if(current_user!!.id!! in members!!.map { it.id!! }){
                        MaterialAlertDialogBuilder(this)
                            .setTitle("Leave ${club!!.name}")
                            .setMessage("You are going to leave ${club!!.name}. Are you sure?")
                            .setPositiveButton("Leave") { dialog, _ ->
                                var result = apiRevUp.deleteMemberClub(current_user!!.id!!, curr_club!!.id!!, this)
                                if(result){
                                    binding.clubDetailsActivityMemberRelationWithClub.setText("Join in")
                                    binding.clubDetailsActivityMemberRelationWithClub.setTextColor(resources.getColor(R.color.memberRelation_NoFriend))
                                    (recyclerView!!.adapter as ClubDetailsMembersAdapter).list.remove(current_user)
                                    recyclerView!!.adapter!!.notifyDataSetChanged()
                                }else{
                                    throw Exception("Error on leaving club")
                                }
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
            }else if(binding.clubDetailsActivityMemberRelationWithClub.text == "Edit club"){
                val intent = Intent(this, EditClubActivity::class.java)
                startActivity(intent)
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

                members = apiRevUp.getMembersByClub(club.id!!, this)
                if(members != null){
                    recyclerView = binding.clubDetailsActivityMembersRecyclerView
                    recyclerView!!.adapter = ClubDetailsMembersAdapter(members!!)
                    recyclerView!!.layoutManager = LinearLayoutManager(this)

                    if (current_user!!.id!! in members!!.map { it.id!! }){
                        binding.clubDetailsActivityMemberRelationWithClub.setText("Joined in")
                        binding.clubDetailsActivityMemberRelationWithClub.setTextColor(resources.getColor(R.color.memberRelation_Friend))
                    }else{
                        binding.clubDetailsActivityMemberRelationWithClub.setText("Join in")
                        binding.clubDetailsActivityMemberRelationWithClub.setTextColor(resources.getColor(R.color.memberRelation_NoFriend))
                    }
                }

                val founder = apiRevUp.getMemberById(club.founder, this)
                if(founder != null){
                    binding.clubDetailsActivityFounder.text = "Founder: ${founder.name}"
                    if(founder.id == current_user!!.id){
                        binding.clubDetailsActivityMemberRelationWithClub.setText("Edit club")
                        binding.clubDetailsActivityMemberRelationWithClub.setTextColor(resources.getColor(R.color.memberRelation_NoFriend))
                    }
                }
            }catch (e: Exception){
                Toast.makeText(this, "Error on club details. $e.message", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val member = (recyclerView!!.adapter as ClubDetailsMembersAdapter).list[item.groupId]
        val memberClub = apiRevUp.getMemberClub(member!!.id!!, curr_club!!.id!!, this)
        when (item.itemId) {
            1 -> {
                memberClub!!.roleType = 2
                var change = apiRevUp.putMemberClub(memberClub, this)
                if(change != null){
                    Toast.makeText(this, "Promoted to Administrador", Toast.LENGTH_SHORT).show()
                    recyclerView!!.adapter!!.notifyDataSetChanged()
                    return true
                }
                Toast.makeText(this, "Error on changing role", Toast.LENGTH_SHORT).show()
                return false
            }
            2 -> {
                memberClub!!.roleType = 3
                var change = apiRevUp.putMemberClub(memberClub, this)
                if(change != null){
                    Toast.makeText(this, "Promoted to Administrador", Toast.LENGTH_SHORT).show()
                    recyclerView!!.adapter!!.notifyDataSetChanged()
                    return true
                }
                Toast.makeText(this, "Error on changing role", Toast.LENGTH_SHORT).show()
                return false
            }
        }
        return super.onContextItemSelected(item)
    }

    override fun onStart() {
        super.onStart()
        if(!recreated){
            recreate()
        }
    }
}