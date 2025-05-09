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
import com.example.revup.ADAPTERS.MemberSearchAdapter
import com.example.revup.R
import com.example.revup._API.RevupCrudAPI
import com.example.revup._DATACLASS.Member
import com.example.revup._DATACLASS.SearchViewModel
import com.example.revup.databinding.FragmentMemberSearchBinding

class MemberSearchFragment : Fragment() {
    lateinit var binding: FragmentMemberSearchBinding
    val apiRevUp = RevupCrudAPI()
    private lateinit var viewModel: SearchViewModel

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

        binding.memberSearchFragmentSearchFragmentMainActivityMemberNameButton.isChecked = true

        binding.memberSearchFragmentSearchFragmentMainActivityToggleButton.addOnButtonCheckedListener { faTogglebutton, checkedId, isChecked ->
            viewModel.filter.observe(viewLifecycleOwner, Observer { text ->
                if(viewModel.current_tab.value == 0){
                    createRecyclerViewer(text)
                }
            })
        }

        viewModel = ViewModelProvider(requireActivity()).get(SearchViewModel::class.java)

        viewModel.filter.observe(viewLifecycleOwner, Observer { text ->
            if(viewModel.current_tab.value == 0){
                createRecyclerViewer(text)
            }
        })
    }

    fun createRecyclerViewer(text: String){
        var memberList: MutableList<Member>? = mutableListOf()
        if (binding.memberSearchFragmentSearchFragmentMainActivityMemberNameButton.isChecked){
            try {
                memberList = apiRevUp.getMembersByMemberName(text, requireContext())
            }catch (e: Exception){
                memberList = mutableListOf()
                Toast.makeText(requireContext(), "Cant load member list", Toast.LENGTH_SHORT).show()
            }
        }else{
            try {
                memberList = apiRevUp.getMembersByCarName(text, requireContext())
            }catch (e: Exception){
                memberList = mutableListOf()
                Toast.makeText(requireContext(), "Cant load member list", Toast.LENGTH_SHORT).show()
            }
        }
        try {
            val recyclerView = binding.memberSearchFragmentSearchFragmentMainActivityRecyclerView
            recyclerView.adapter = MemberSearchAdapter(memberList!!)
            recyclerView.layoutManager = GridLayoutManager(requireView().context, 2)
        }catch (e: Exception){
            Toast.makeText(requireContext(), "Cant load member list", Toast.LENGTH_SHORT).show()
        }
    }

}