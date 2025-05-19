package com.example.revup.FRAGMENTS

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.revup.ADAPTERS.ViewPagerAdapter
import com.example.revup._API.ChatService
import com.example.revup._DATACLASS.ChatViewModel
import com.example.revup._DATACLASS.current_user
import com.example.revup.databinding.ChatsFragmentMainactivityBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.launch


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

        val fragments = mutableListOf(MemberChatFragment(), ClubChatFragment())
        val adapter = ViewPagerAdapter(
            fragments,
            childFragmentManager,
            lifecycle
        )
        binding.chatFragmentMainActivityViewPager.adapter = adapter
        binding.chatFragmentMainActivityViewPager.isUserInputEnabled = false

        TabLayoutMediator(binding.chatFragmentMainActivityTabs, binding.chatFragmentMainActivityViewPager) { tab, position ->
            tab.text = if (position == 0) "Members" else "Clubs"
        }.attach()

        binding.chatFragmentMainActivityTabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                viewModel.current_tab.value = tab?.position
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

        binding.chatFragmentMainActivitySearchText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.filter.value = s.toString()
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

}