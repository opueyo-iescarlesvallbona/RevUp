package com.example.revup.FRAGMENTS

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.revup.ADAPTERS.EventsRoutesListAdapter
import com.example.revup.ADAPTERS.HomeFragmentPostAdapterRV
import com.example.revup.ADAPTERS.ViewPagerAdapter
import com.example.revup.R
import com.example.revup._API.RevupCrudAPI
import com.example.revup._DATACLASS.ClubEvent
import com.example.revup._DATACLASS.FormatDate
import com.example.revup._DATACLASS.current_user
import com.example.revup.databinding.FragmentListEventsBinding
import com.google.android.material.tabs.TabLayoutMediator
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.util.Date
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes

class ListEventsFragment : Fragment() {
    lateinit var binding: FragmentListEventsBinding
    val apiRevUp = RevupCrudAPI()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListEventsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView_current =
            binding.listEventsFragmentEventsFragmentMainActivityRecyclerViewCurrent
        val recyclerView_past = binding.listEventsFragmentEventsFragmentMainActivityRecyclerViewPast
        try {
            val clubs = apiRevUp.getClubsByMember(current_user!!.id!!, requireContext())
            var events: MutableList<ClubEvent> = mutableListOf()
            if (clubs != null) {
                for (club in clubs) {
                    var cevents = apiRevUp.getAllEventsByClub(club.id!!, requireContext())

                    for (event in cevents!!) {
                        events.add(event)
                    }
                }
            }
            var localdatetime_now = Date.from(LocalDateTime.now()!!.atZone(ZoneId.systemDefault()).toInstant())
            var past_events = events.filter { FormatDate(it.endDate).before(localdatetime_now) }.toMutableList()
            var current_events = events.filter { FormatDate(it.endDate).after(localdatetime_now) }.toMutableList()

            recyclerView_current.adapter = EventsRoutesListAdapter(current_events)
            recyclerView_current.layoutManager = LinearLayoutManager(requireView().context)
            recyclerView_past.adapter = EventsRoutesListAdapter(past_events)
            recyclerView_past.layoutManager = LinearLayoutManager(requireView().context)
        } catch (e: Exception) {
            Log.i("ERROR", e.toString())
        }
    }

}