package com.example.revup.FRAGMENTS

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.example.revup.ADAPTERS.CarSearchAdapter
import com.example.revup.ADAPTERS.MemberSearchAdapter
import com.example.revup.R
import com.example.revup._DATACLASS.Car
import com.example.revup._DATACLASS.Member
import com.example.revup.databinding.FragmentCarSearchBinding

class CarSearchFragment : Fragment() {
    lateinit var binding: FragmentCarSearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCarSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // test data
        val car1 = Car(1,1,1)
        val car2 = Car(2,2,2)
        val carList = mutableListOf(car1, car2)

        val recyclerView = binding.carSearchFragmentSearchFragmentMainActivityRecyclerView
        recyclerView.adapter = CarSearchAdapter(carList)
        recyclerView.layoutManager = GridLayoutManager(requireView().context, 2)
    }
}