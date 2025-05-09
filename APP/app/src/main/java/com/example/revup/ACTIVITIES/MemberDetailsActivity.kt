package com.example.revup.ACTIVITIES

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.revup.ADAPTERS.ViewPagerAdapter
import com.example.revup.FRAGMENTS.MemberDetailsCarsFragment
import com.example.revup.FRAGMENTS.MemberDetailsPostsFragment
import com.example.revup.R
import com.example.revup._API.RevupCrudAPI
import com.example.revup._DATACLASS.Member
import com.example.revup.databinding.ActivityMemberDetailsBinding
import com.google.android.material.tabs.TabLayoutMediator

class MemberDetailsActivity : AppCompatActivity() {
    lateinit var binding : ActivityMemberDetailsBinding
    val apiRevUp = RevupCrudAPI()

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

        if (member != null){
            try {
                //TODO get image
                binding.memberDetailsActivityMemberName.text = member.name
                binding.memberDetailsActivityName.text = member.name
                binding.clubDetailsActivityDescription.text = member.description

                val location = apiRevUp.getLocationById(member.locationId, this)
                if(location != null){
                    binding.memberDetailsActivityLocation.text = location.municipality
                }

                val listOfFragments = listOf(MemberDetailsCarsFragment(member.id), MemberDetailsPostsFragment(member.id))
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

            }catch (e: Exception){
                Toast.makeText(this, "Error on club details. $e.message", Toast.LENGTH_SHORT).show()
            }
        }
    }
}