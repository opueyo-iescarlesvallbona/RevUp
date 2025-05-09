package com.example.revup.FRAGMENTS

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.revup.ADAPTERS.MemberDetailsPostsAdapter
import com.example.revup._API.RevupCrudAPI
import com.example.revup.databinding.FragmentMemberDetailsPostsBinding

class MemberDetailsPostsFragment : Fragment() {
    lateinit var binding: FragmentMemberDetailsPostsBinding
    val apiRevUp = RevupCrudAPI()
    private var memberId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentMemberDetailsPostsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        try {
            var posts = apiRevUp.getPostsByMemberId(memberId, requireContext())
            val recyclerView = binding.memberDetailsPostsActivityRecyclerView
            if (posts == null) posts = mutableListOf()
            recyclerView.adapter = MemberDetailsPostsAdapter(posts)
            recyclerView.layoutManager = LinearLayoutManager(requireView().context)
        }catch (e: Exception){
            Toast.makeText(requireContext(), "Error on club details. $e.message", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        fun newInstance(memberId: Int): MemberDetailsCarsFragment {
            val fragment = MemberDetailsCarsFragment()
            val args = Bundle()
            args.putInt("member_id", memberId)
            fragment.arguments = args
            return fragment
        }
    }

}