package com.example.revup.FRAGMENTS

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.revup.ADAPTERS.EventsRoutesListAdapter
import com.example.revup.ADAPTERS.HomeFragmentPostAdapterRV
import com.example.revup.R
import com.example.revup._API.RevupCrudAPI
import com.example.revup._DATACLASS.current_user
import com.example.revup.databinding.FragmentListRoutesBinding

class ListRoutesFragment : Fragment() {
    lateinit var binding : FragmentListRoutesBinding
    val apiRevUp = RevupCrudAPI()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListRoutesBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = binding.listRoutesFragmentEventsFragmentMainActivityRecyclerView
        try {
            var list = apiRevUp.getAllRoutesByMember(current_user!!.id!!, requireView().context)
            recyclerView.adapter = EventsRoutesListAdapter(list!!)
            recyclerView.layoutManager = LinearLayoutManager(requireView().context)
        }catch (e: Exception){
            Log.i("ERROR", e.toString())
        }
    }
}