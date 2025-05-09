package com.example.revup.FRAGMENTS

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.revup.ADAPTERS.EventsRoutesListAdapter
import com.example.revup.databinding.FragmentMemberDetailsCarsBinding

class MemberDetailsCarsFragment : Fragment() {
    lateinit var bindig: FragmentMemberDetailsCarsBinding
    private val memberId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        bindig = FragmentMemberDetailsCarsBinding.inflate(inflater, container, false)
        return bindig.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // carrousel
    }

    companion object {
        fun newInstance(memberId: Int): MemberDetailsCarsFragment {
            val fragment = MemberDetailsCarsFragment()
            val args = Bundle()
            args.putInt("member_id", memberId)
            fragment.arguments = args
            return fragment
        }
    }

}