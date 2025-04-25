package com.example.revup.FRAGMENTS

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.revup.ADAPTERS.ViewPagerAdapter
import com.example.revup.databinding.EventsFragmentMainactivityBinding
import com.google.android.material.tabs.TabLayoutMediator

class EventsFragment : Fragment() {
    lateinit var binding : EventsFragmentMainactivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = EventsFragmentMainactivityBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val listOfFragments = listOf(MapEventsFragment(), ListEventsFragment(), ListRoutesFragment())
        var adapter = ViewPagerAdapter(
            listOfFragments,
            activity?.supportFragmentManager!!,
            lifecycle
        )

        binding.eventsFragmentMainActivityViewPager.adapter = adapter
        binding.eventsFragmentMainActivityViewPager.isUserInputEnabled = false

        TabLayoutMediator(binding.eventsFragmentMainActivityTabs, binding.eventsFragmentMainActivityViewPager){ tab, position ->
            tab.text = when (position){
                0 -> "Map"
                1 -> "Events"
                2 -> "Routes"
                else -> ""
            }
        }.attach()

    }
}