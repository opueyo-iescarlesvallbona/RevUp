package com.example.revup.FRAGMENTS

import android.graphics.Rect
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.revup.ADAPTERS.EventsRoutesListAdapter
import com.example.revup.ADAPTERS.MemberDetailsCarsCarouselAdapter
import com.example.revup._API.RevupCrudAPI
import com.example.revup._DATACLASS.Car
import com.example.revup._DATACLASS.curr_member
import com.example.revup._DATACLASS.current_user
import com.example.revup.databinding.FragmentMemberDetailsCarsBinding
import com.google.android.material.carousel.CarouselLayoutManager
import com.google.android.material.carousel.CarouselSnapHelper
import com.google.android.material.carousel.CarouselStrategy
import com.google.android.material.carousel.FullScreenCarouselStrategy
import com.google.android.material.carousel.HeroCarouselStrategy
import com.google.android.material.carousel.UncontainedCarouselStrategy

class MemberDetailsCarsFragment : Fragment() {
    lateinit var binding: FragmentMemberDetailsCarsBinding
    private val apiRevUp = RevupCrudAPI()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentMemberDetailsCarsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var cars: MutableList<Car>? = mutableListOf()
        try {
            cars = apiRevUp.getCarsByMember(curr_member!!.id!!, requireContext())
            val recyclerView = binding.memberDetailsCarsFragmentRecyclerView
            recyclerView.visibility = View.VISIBLE
            if(cars != null && cars.isNotEmpty()){
                for(car in cars){
                    car.model = apiRevUp.getModelById(car.modelId, requireContext())
                    car.model!!.brand = apiRevUp.getBrand(car.model!!.idBrand, requireContext())
                }
            }else if(curr_member == current_user){
                cars = mutableListOf()
            }else{
                cars = mutableListOf(Car(id = -2,-1,-1,-1,-1.1))
            }
            if(curr_member == current_user){
                cars.add(Car(id = -1,-1,-1,-1,-1.1))
            }
            val snapHelper = CarouselSnapHelper()
            snapHelper.attachToRecyclerView(recyclerView)
            recyclerView.adapter = MemberDetailsCarsCarouselAdapter(cars)
            var layoutManager = CarouselLayoutManager()
            recyclerView.layoutManager = layoutManager
        }catch (e: Exception){
            Toast.makeText(requireContext(), "Error on getting cars. $e.message", Toast.LENGTH_SHORT).show()
        }
    }

}