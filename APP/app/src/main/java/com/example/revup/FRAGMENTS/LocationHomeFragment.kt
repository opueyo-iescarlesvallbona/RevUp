package com.example.revup.FRAGMENTS

import android.location.Location
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.revup.ADAPTERS.HomeFragmentPostAdapterRV
import com.example.revup.R
import com.example.revup._API.RevupCrudAPI
import com.example.revup._DATACLASS.Post
import com.example.revup._DATACLASS.current_user
import com.example.revup.databinding.LocationHomefragmentMainactivityBinding

class LocationHomeFragment : Fragment() {
    lateinit var binding : LocationHomefragmentMainactivityBinding
    val apiRevUp = RevupCrudAPI()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = LocationHomefragmentMainactivityBinding.inflate(layoutInflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = binding.locationHomeFragmentMainActivityRecyclerView
        try {
            var list = apiRevUp.getPostsByLocation(current_user!!.locationId, requireView().context)//añadir por fecha

            for(p: Post in list!!){
                p.member = apiRevUp.getMemberById(p.memberId, requireView().context)
                p.liked = apiRevUp.getPostIsLikedByMember(current_user!!.id, p.id, requireView().context)!!
            }
            recyclerView.adapter = HomeFragmentPostAdapterRV(list!!)
            recyclerView.layoutManager = LinearLayoutManager(requireView().context)
        }catch (e: Exception){
            Toast.makeText(requireView().context, e.toString(), Toast.LENGTH_SHORT).show()
        }
    }
}