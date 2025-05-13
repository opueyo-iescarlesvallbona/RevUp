package com.example.revup.FRAGMENTS

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.revup.ADAPTERS.MemberDetailsCarsCarouselAdapter
import com.example.revup.ADAPTERS.MemberDetailsPostsAdapter
import com.example.revup._API.RevupCrudAPI
import com.example.revup._DATACLASS.Member
import com.example.revup._DATACLASS.Post
import com.example.revup._DATACLASS.curr_member
import com.example.revup.databinding.FragmentMemberDetailsPostsBinding
import com.google.android.material.carousel.CarouselLayoutManager
import com.google.android.material.carousel.CarouselSnapHelper

class MemberDetailsPostsFragment : Fragment() {
    lateinit var binding: FragmentMemberDetailsPostsBinding
    private val apiRevUp = RevupCrudAPI()

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

        var posts: MutableList<Post>? = mutableListOf()
        try {
            posts = apiRevUp.getPostsByMemberId(curr_member!!.id, requireContext())
            val recyclerView = binding.memberDetailsPostsFragmentRecyclerView
            recyclerView.visibility = View.VISIBLE
            if (posts != null && posts.isNotEmpty()){
                for (post in posts){
                    post.member = curr_member
                }
            }else{
                posts = mutableListOf(Post(-1,"",-1,"","","",-1,-1,-1,-1))
                recyclerView.visibility = View.GONE
            }
            recyclerView.adapter = MemberDetailsPostsAdapter(posts)
            recyclerView.layoutManager = LinearLayoutManager(requireView().context)
        }catch (e: Exception){
            Toast.makeText(requireContext(), "Error on club details. $e.message", Toast.LENGTH_SHORT).show()
        }
    }

}