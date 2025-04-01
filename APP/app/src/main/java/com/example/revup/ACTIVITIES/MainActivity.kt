package com.example.revup.ACTIVITIES

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.revup.FRAGMENTS.ChatsFragment
import com.example.revup.FRAGMENTS.EventsFragment
import com.example.revup.FRAGMENTS.HomeFragment
import com.example.revup.FRAGMENTS.SearchFragment
import com.example.revup.R
import com.example.revup.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initFragment(HomeFragment())
        binding.mainActivityBottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {
                    initFragment(HomeFragment())
                    true
                }
                R.id.events -> {
                    initFragment(EventsFragment())
                    true
                }
                R.id.search -> {
                    initFragment(SearchFragment())
                    true
                }
                R.id.chats -> {
                    initFragment(ChatsFragment())
                    true
                }
                else -> {true}
            }
        }

    }

    fun initFragment(fragment: Fragment){
        val transaccio = supportFragmentManager.beginTransaction()
        transaccio.replace(R.id.mainActivity_fragmentContainerView, fragment)
        transaccio.commit()
    }
}