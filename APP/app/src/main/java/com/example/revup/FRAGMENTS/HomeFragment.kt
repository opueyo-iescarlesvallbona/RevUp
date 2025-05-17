package com.example.revup.FRAGMENTS

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.example.revup.ADAPTERS.ViewPagerAdapter
import com.example.revup.databinding.HomeFragmentMainactivityBinding
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.launch

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

        val initialFragments = listOf(BlankFragment(), BlankFragment(), BlankFragment())
        val adapter = ViewPagerAdapter(
            initialFragments.toMutableList(),
            requireActivity().supportFragmentManager,
            lifecycle
        )

        binding.homeFragmentMainActivityViewPager.adapter = adapter

        TabLayoutMediator(binding.homeFragmentMainActivityTabs, binding.homeFragmentMainActivityViewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Following"
                1 -> "Location"
                2 -> "Likes"
                else -> ""
            }
        }.attach()

        lifecycleScope.launch {
            Handler(Looper.getMainLooper()).postDelayed({
                val realFragments = mutableListOf(FollowingHomeFragment(), LocationHomeFragment(), LikesHomeFragment())
                adapter.updateFragments(realFragments)
            }, 0)
        }
    }
}