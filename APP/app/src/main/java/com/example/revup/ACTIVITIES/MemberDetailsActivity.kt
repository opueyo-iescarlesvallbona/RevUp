package com.example.revup.ACTIVITIES

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.revup.ADAPTERS.ViewPagerAdapter
import com.example.revup.FRAGMENTS.MemberDetailsCarsFragment
import com.example.revup.FRAGMENTS.MemberDetailsPostsFragment
import com.example.revup.R
import com.example.revup._API.RevupCrudAPI
import com.example.revup._DATACLASS.Member
import com.example.revup._DATACLASS.MemberRelation
import com.example.revup._DATACLASS.current_user
import com.example.revup._DATACLASS.image_path
import com.example.revup.databinding.ActivityMemberDetailsBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.tabs.TabLayoutMediator

class MemberDetailsActivity : AppCompatActivity() {
    lateinit var binding : ActivityMemberDetailsBinding
    val apiRevUp = RevupCrudAPI()
    var memberRelation: MemberRelation? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMemberDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val member = intent.getParcelableExtra<Member>("member")

        binding.memberDetailsActivityBackButton.setOnClickListener{
            this.onBackPressed()
            val returnIntent = Intent()
            setResult(RESULT_OK, returnIntent)
            finish()
        }

        binding.memberDetailsActivityBackButtonText.setOnClickListener {
            this.onBackPressed()
            val returnIntent = Intent()
            setResult(RESULT_OK, returnIntent)
            finish()
        }

        binding.memberDetailsActivityMemberRelation.setOnClickListener {
            if(binding.memberDetailsActivityMemberRelation.text == "Follow Up"){
                try{
                    apiRevUp.postMemberRelation(MemberRelation(current_user!!.id, member!!.id, 1), this)
                    binding.memberDetailsActivityMemberRelation.setText("Following")
                    binding.memberDetailsActivityMemberRelation.setTextColor(resources.getColor(R.color.memberRelation_Friend))
                    memberRelation = MemberRelation(current_user!!.id, member!!.id, 1)
                }catch(e: Exception){
                    Toast.makeText(this, "Error on following. $e.message", Toast.LENGTH_SHORT).show()
                }
            }else{
                try{
                    if(memberRelation != null){
                        MaterialAlertDialogBuilder(this)
                            .setTitle("Unfollow ${member!!.membername}")
                            .setMessage("You are going to unfollow ${member!!.membername}. Are you sure?")
                            .setPositiveButton("Delete") { dialog, _ ->
                                var result = apiRevUp.deleteMemberRelation(current_user!!.id, memberRelation!!.memberId2, this)
                                if(result){
                                    binding.memberDetailsActivityMemberRelation.setText("Follow Up")
                                    binding.memberDetailsActivityMemberRelation.setTextColor(resources.getColor(R.color.memberRelation_NoFriend))
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
                        Toast.makeText(this, "Error on unfollowing", Toast.LENGTH_SHORT).show()
                    }
                }catch(e: Exception){
                    Toast.makeText(this, "Error on unfollowing. $e.message", Toast.LENGTH_SHORT).show()
                }
            }
        }

        if (member != null){
            try {
                if(member.profilePicture != null && member.profilePicture != ""){
                    Glide.with(this).load(image_path+member.profilePicture).circleCrop().into(binding.memberDetailsActivityPicture)
                }else{
                    binding.memberDetailsActivityPicture.setImageResource(R.drawable.baseline_account_circle_24)
                }
                binding.memberDetailsActivityMemberName.text = member.name
                binding.memberDetailsActivityName.text = member.name
                binding.clubDetailsActivityDescription.text = member.description

                val location = apiRevUp.getLocationById(member.locationId, this)
                if(location != null){
                    binding.memberDetailsActivityLocation.text = location.municipality
                }

                val listOfFragments = listOf(
                    MemberDetailsCarsFragment.newInstance(member.id),
                    MemberDetailsPostsFragment.newInstance(member.id)
                )
                var adapter = ViewPagerAdapter(
                    listOfFragments,
                    this.supportFragmentManager,
                    lifecycle
                )

                binding.clubDetailsActivityViewPager.adapter = adapter
                TabLayoutMediator(binding.clubDetailsActivityTabs, binding.clubDetailsActivityViewPager){ tab, position ->
                    tab.text = when (position){
                        0 -> "Cars"
                        1 -> "Posts"
                        else -> ""
                    }
                }.attach()

                var member_relations = apiRevUp.getMemberRelationsByMemberId(current_user!!.id, this)
                if (member_relations != null){
                    val member_relation = member_relations!!.find{it.memberId2 == member.id}
                    if(member_relation != null) {
                        memberRelation = member_relation
                        binding.memberDetailsActivityMemberRelation.setText("Following")
                        binding.memberDetailsActivityMemberRelation.setTextColor(resources.getColor(R.color.memberRelation_Friend))
                    }else{
                        binding.memberDetailsActivityMemberRelation.setText("Follow Up")
                        binding.memberDetailsActivityMemberRelation.setTextColor(resources.getColor(R.color.memberRelation_NoFriend))
                    }
                }else{
                    binding.memberDetailsActivityMemberRelation.setText("Follow Up")
                    binding.memberDetailsActivityMemberRelation.setTextColor(resources.getColor(R.color.memberRelation_NoFriend))
                }



            }catch (e: Exception){
                Toast.makeText(this, "Error on club details. $e.message", Toast.LENGTH_SHORT).show()
            }
        }
    }
}