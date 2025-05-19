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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.revup.ADAPTERS.ClubChatAdapter
import com.example.revup.R
import com.example.revup._API.RevupCrudAPI
import com.example.revup._DATACLASS.ChatViewModel
import com.example.revup._DATACLASS.Club
import com.example.revup._DATACLASS.Member
import com.example.revup._DATACLASS.current_user
import com.example.revup.databinding.FragmentClubChatBinding

class ClubChatFragment : Fragment() {
    lateinit var binding: FragmentClubChatBinding
    val apiRevUp = RevupCrudAPI()
    private lateinit var viewModel: ChatViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentClubChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        viewModel = ViewModelProvider(requireActivity()).get(ChatViewModel::class.java)

        viewModel.filter.observe(viewLifecycleOwner, Observer { text ->
            if(viewModel.current_tab.value == 1){
                try {
                    var clubs = apiRevUp.getClubsByMember(current_user!!.id!!, requireContext())
                    Log.i("currClubs", clubs.toString())
                    var clubList: MutableList<Club>
                    if(clubs!=null){
                        clubList = clubs.filter { it.name.contains(text) }.toMutableList()
                    }else{
                        clubList = mutableListOf()
                    }


                    val recyclerView = binding.clubChatFragmentChatFragmentMainActivityRecyclerView

                    recyclerView.adapter = ClubChatAdapter(clubList)
                    recyclerView.layoutManager = GridLayoutManager(requireView().context, 2)
                }catch (e: Exception){
                    Toast.makeText(requireContext(), "Cant load club list", Toast.LENGTH_SHORT).show()
                }
            }
        })

    }
}