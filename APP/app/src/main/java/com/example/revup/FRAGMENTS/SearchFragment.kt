package com.example.revup.FRAGMENTS

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.revup.ADAPTERS.ViewPagerAdapter
import com.example.revup.R
import com.example.revup.databinding.SearchFragmentMainactivityBinding
import com.google.android.material.tabs.TabLayoutMediator

class SearchFragment : Fragment() {
    lateinit var binding: SearchFragmentMainactivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SearchFragmentMainactivityBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val listOfFragments = listOf(MemberSearchFragment(), CarSearchFragment())
        var adapter = ViewPagerAdapter(
            listOfFragments,
            activity?.supportFragmentManager!!,
            lifecycle
        )

        binding.searchFragmentMainActivityViewPager.adapter = adapter
        binding.searchFragmentMainActivityViewPager.isUserInputEnabled = false

        TabLayoutMediator(binding.searchFragmentMainActivityTabs, binding.searchFragmentMainActivityViewPager){ tab, position ->
            tab.text = when (position){
                0 -> "Member"
                1 -> "Car"
                else -> ""
            }
        }.attach()
    }
}