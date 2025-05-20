package com.example.revup.FRAGMENTS

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.revup.ADAPTERS.HomeFragmentPostAdapterRV
import com.example.revup.R
import com.example.revup._API.RevupCrudAPI
import com.example.revup._DATACLASS.Post
import com.example.revup._DATACLASS.current_user
import com.example.revup._DATACLASS.recreated
import com.example.revup.databinding.LikesHomefragmentMainactivityBinding

class LikesHomeFragment : Fragment() {
    lateinit var binding : LikesHomefragmentMainactivityBinding
    val apiRevUp = RevupCrudAPI()
    var recyclerView: RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = LikesHomefragmentMainactivityBinding.inflate(layoutInflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = binding.likesHomeFragmentMainActivityRecyclerView
        try {
            var list = apiRevUp.getPostsByLikes(requireView().context)//añadir por fecha

            for(p: Post in list!!){
                p.member = apiRevUp.getMemberById(p.memberId, requireView().context)
                p.liked = apiRevUp.getPostIsLikedByMember(current_user!!.id!!, p.id!!, requireView().context)!!
            }
            recyclerView!!.adapter = HomeFragmentPostAdapterRV(list!!)
            recyclerView!!.layoutManager = LinearLayoutManager(requireView().context)
        }catch (e: Exception){
            Toast.makeText(requireView().context, e.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onResume() {
        super.onResume()
        if(!recreated){
            if(recyclerView != null){
                recyclerView!!.adapter!!.notifyDataSetChanged()
                recreated = true
            }
        }
    }
}