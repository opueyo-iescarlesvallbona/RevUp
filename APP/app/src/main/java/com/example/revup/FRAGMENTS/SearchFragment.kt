package com.example.revup.FRAGMENTS

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.revup.ADAPTERS.ViewPagerAdapter
import com.example.revup._DATACLASS.SearchViewModel
import com.example.revup.databinding.SearchFragmentMainactivityBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.launch


class SearchFragment : Fragment() {
    lateinit var binding: SearchFragmentMainactivityBinding
    private lateinit var viewModel: SearchViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(SearchViewModel::class.java)
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

        val initialFragments = listOf(BlankFragment(), BlankFragment())
        val adapter = ViewPagerAdapter(
            initialFragments.toMutableList(),
            requireActivity().supportFragmentManager,
            lifecycle
        )

        binding.searchFragmentMainActivityViewPager.adapter = adapter
        binding.searchFragmentMainActivityViewPager.isUserInputEnabled = false

        TabLayoutMediator(binding.searchFragmentMainActivityTabs, binding.searchFragmentMainActivityViewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Members"
                1 -> "Clubs"
                else -> ""
            }
        }.attach()

        lifecycleScope.launch {
            Handler(Looper.getMainLooper()).postDelayed({
                val realFragments = mutableListOf(MemberSearchFragment(), ClubSearchFragment())
                adapter.updateFragments(realFragments)

                binding.searchFragmentMainActivityTabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                    override fun onTabSelected(tab: TabLayout.Tab?) {
                        viewModel.current_tab.value = tab?.position
                    }

                    override fun onTabUnselected(tab: TabLayout.Tab?) {
                    }

                    override fun onTabReselected(tab: TabLayout.Tab?) {
                    }
                })
            }, 0)
        }

        val textWatcher: TextWatcher = object : TextWatcher {
            override fun beforeTextChanged(
                p0: CharSequence?,
                p1: Int,
                p2: Int,
                p3: Int
            ) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.filter.value = s.toString()
            }
            override fun afterTextChanged(p0: Editable?) {}
        }

        binding.searchFragmentMainActivitySearchText.addTextChangedListener(textWatcher)

    }
}