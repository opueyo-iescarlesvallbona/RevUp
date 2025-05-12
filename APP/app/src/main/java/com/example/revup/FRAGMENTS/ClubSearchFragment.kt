package com.example.revup.FRAGMENTS

import android.os.Bundle
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
import com.example.revup.ADAPTERS.ClubSearchAdapter
import com.example.revup.R
import com.example.revup._API.RevupCrudAPI
import com.example.revup._DATACLASS.Club
import com.example.revup._DATACLASS.Member
import com.example.revup._DATACLASS.SearchViewModel
import com.example.revup.databinding.FragmentClubSearchBinding

class ClubSearchFragment : Fragment() {
    lateinit var binding: FragmentClubSearchBinding
    val apiRevUp = RevupCrudAPI()
    private lateinit var viewModel: SearchViewModel
    var clubList: MutableList<Club>? = null
    var clubListFiltered: MutableList<Club> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentClubSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        clubList = apiRevUp.getClubsByName("", requireContext())
        viewModel = ViewModelProvider(requireActivity()).get(SearchViewModel::class.java)

        viewModel.filter.observe(viewLifecycleOwner, Observer { text ->
            if(viewModel.current_tab.value == 1){
                try {
                    val recyclerView = binding.clubSearchFragmentSearchFragmentMainActivityRecyclerView
                    if (clubList == null){
                        clubListFiltered = mutableListOf()
                    }else{
                        clubListFiltered = clubList!!.filter { it.name!!.contains(text) } as MutableList<Club>
                        if (clubListFiltered.isEmpty()){
                            clubListFiltered = mutableListOf()
                        }
                    }
                    recyclerView.adapter = ClubSearchAdapter(clubListFiltered)
                    recyclerView.layoutManager = GridLayoutManager(requireView().context, 2)
                }catch (e: Exception){
                    Toast.makeText(requireContext(), "Cant load club list", Toast.LENGTH_SHORT).show()
                }
            }
        })

    }
}