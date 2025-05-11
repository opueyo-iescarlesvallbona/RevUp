package com.example.revup.FRAGMENTS

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.revup.ADAPTERS.ViewPagerAdapter
import com.example.revup._DATACLASS.ChatViewModel
import com.example.revup.databinding.ChatsFragmentMainactivityBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator



class ChatsFragment : Fragment() {
    lateinit var binding: ChatsFragmentMainactivityBinding
    private lateinit var viewModel: ChatViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(ChatViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ChatsFragmentMainactivityBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val listOfFragments = listOf(MemberChatFragment(), ClubChatFragment())
        var adapter = ViewPagerAdapter(
            listOfFragments,
            activity?.supportFragmentManager!!,
            lifecycle
        )

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

        binding.chatFragmentMainActivitySearchText.addTextChangedListener(textWatcher)

        binding.chatFragmentMainActivityViewPager.adapter = adapter
        binding.chatFragmentMainActivityViewPager.isUserInputEnabled = false

        TabLayoutMediator(binding.chatFragmentMainActivityTabs, binding.chatFragmentMainActivityViewPager){ tab, position ->
            tab.text = when (position){
                0 -> "Members"
                1 -> "Clubs"
                else -> ""
            }
        }.attach()

        binding.chatFragmentMainActivityTabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                viewModel.current_tab.value = tab?.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })

    }
}