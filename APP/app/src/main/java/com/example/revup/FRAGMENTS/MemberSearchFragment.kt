package com.example.revup.FRAGMENTS

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.revup.ADAPTERS.EventsRoutesListAdapter
import com.example.revup.ADAPTERS.MemberSearchAdapter
import com.example.revup.R
import com.example.revup._DATACLASS.Member
import com.example.revup.databinding.FragmentMemberSearchBinding

class MemberSearchFragment : Fragment() {
    lateinit var binding: FragmentMemberSearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMemberSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // test data
        val member1 = Member(
            name = "test1",
            dateOfBirth = "",
            loginDate = ""
        )
        val member2 = Member(
            name = "test2",
            dateOfBirth = "",
            loginDate = ""
        )
        val memberList = mutableListOf(member1, member2)

        val recyclerView = binding.memberSearchFragmentSearchFragmentMainActivityRecyclerView
        recyclerView.adapter = MemberSearchAdapter(memberList)
        recyclerView.layoutManager = GridLayoutManager(requireView().context, 2)
    }

}