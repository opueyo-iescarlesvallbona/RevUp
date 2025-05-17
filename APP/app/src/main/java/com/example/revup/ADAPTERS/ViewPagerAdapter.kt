package com.example.revup.ADAPTERS

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(
    private var fragments: MutableList<Fragment>,
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    private var fragmentIds = fragments.map { it.hashCode().toLong() }.toMutableList()

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment = fragments[position]

    override fun getItemId(position: Int): Long {
        return fragmentIds[position]
    }

    override fun containsItem(itemId: Long): Boolean {
        return fragmentIds.contains(itemId)
    }

    fun updateFragments(newFragments: MutableList<Fragment>) {
        fragments = newFragments
        fragmentIds = newFragments.map { it.hashCode().toLong() }.toMutableList()
        notifyDataSetChanged()
    }
}