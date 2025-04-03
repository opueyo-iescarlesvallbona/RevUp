package com.example.revup.FRAGMENTS

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.example.revup.ADAPTERS.HomeFragment_ViewPagerAdapter
import com.example.revup.databinding.HomeFragmentMainactivityBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class HomeFragment : Fragment() {
    lateinit var binding : HomeFragmentMainactivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = HomeFragmentMainactivityBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val listOfFragments = listOf(LocationHomeFragment(), LikesHomeFragment())
        var adapter = HomeFragment_ViewPagerAdapter(
            listOfFragments,
            activity?.supportFragmentManager!!,
            lifecycle
        )

        binding.homeFragmentMainActivityViewPager.adapter = adapter

        TabLayoutMediator(binding.homeFragmentMainActivityTabs, binding.homeFragmentMainActivityViewPager){ tab, position ->
            tab.text = when (position){
                0 -> "Location"
                1 -> "Likes"
                else -> ""
            }
        }.attach()

    }
}