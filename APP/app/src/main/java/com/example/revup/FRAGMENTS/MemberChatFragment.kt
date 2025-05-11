package com.example.revup.FRAGMENTS

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.revup.ADAPTERS.EventsRoutesListAdapter
import com.example.revup.ADAPTERS.MemberChatAdapter
import com.example.revup.R
import com.example.revup._API.RevupCrudAPI
import com.example.revup._DATACLASS.ChatViewModel
import com.example.revup._DATACLASS.Member
import com.example.revup._DATACLASS.current_user
import com.example.revup.databinding.FragmentMemberChatBinding

class MemberChatFragment : Fragment() {
    lateinit var binding: FragmentMemberChatBinding
    val apiRevUp = RevupCrudAPI()
    private lateinit var viewModel: ChatViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMemberChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity()).get(ChatViewModel::class.java)

        viewModel.filter.observe(viewLifecycleOwner, Observer { text ->
            try {
                val friends = apiRevUp.getFriends(current_user!!.id, requireContext())

                var memberList: MutableList<Member>
                if(friends!=null){
                    memberList = friends.filter { it.membername!!.contains(text) }.toMutableList()
                }else{
                    memberList = mutableListOf()
                }

                val recyclerView = binding.memberChatFragmentChatFragmentMainActivityRecyclerView

                recyclerView.adapter = MemberChatAdapter(memberList)
                recyclerView.layoutManager = GridLayoutManager(requireView().context, 2)
            }catch (e: Exception){
                Toast.makeText(requireContext(), "Cant load member list", Toast.LENGTH_SHORT).show()
            }
        })
    }

}