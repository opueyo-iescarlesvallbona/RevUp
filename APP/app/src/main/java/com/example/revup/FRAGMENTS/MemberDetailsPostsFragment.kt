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

class MemberDetailsPostsFragment(memberId: Int) : Fragment() {
    lateinit var binding: FragmentMemberDetailsPostsBinding
    val apiRevUp = RevupCrudAPI()

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

//        try {
//            val posts = apiRevUp.getPosts(memberId, requireContext())
//            val recyclerView = binding.memberDetailsPostsActivityRecyclerView
//            recyclerView.adapter = MemberDetailsPostsAdapter(current_events!!)
//            recyclerView.layoutManager = LinearLayoutManager(requireView().context)
//        }catch (e: Exception){
//            Toast.makeText(this, "Error on club details. $e.message", Toast.LENGTH_SHORT).show()
//        }
    }

    companion object {
        private const val ARG_MEMBER_ID = "member_id"

        fun newInstance(memberId: Int): MemberDetailsCarsFragment {
            val fragment = MemberDetailsCarsFragment(memberId)
            val args = Bundle()
            args.putInt(ARG_MEMBER_ID, memberId)
            fragment.arguments = args
            return fragment
        }
    }

}