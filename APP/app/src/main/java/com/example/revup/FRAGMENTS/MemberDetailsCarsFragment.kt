package com.example.revup.FRAGMENTS

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.revup.ADAPTERS.EventsRoutesListAdapter
import com.example.revup.databinding.FragmentMemberDetailsCarsBinding

class MemberDetailsCarsFragment(memberId: Int) : Fragment() {
    lateinit var bindig: FragmentMemberDetailsCarsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val memberId = arguments?.getInt("member_id")
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
        private const val ARG_MEMBER_ID = "member_id"

        fun newInstance(memberId: Int): MemberDetailsCarsFragment {
            val fragment = MemberDetailsCarsFragment(memberId)
            val args = Bundle()
            args.putInt(ARG_MEMBER_ID, memberId)
            fragment.arguments = args
            return fragment
        }
    }

}