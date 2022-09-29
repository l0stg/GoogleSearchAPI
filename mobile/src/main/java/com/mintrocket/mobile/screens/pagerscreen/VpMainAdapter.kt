package com.mintrocket.mobile.screens.pagerscreen

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class VpMainAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount() = PagerHelper.getPagesCount()

    override fun createFragment(position: Int): Fragment {
        val scopeName = PagerHelper.getPageForPosition(position).scopeName
        return PageContainerFragment.newInstance(scopeName)
    }
}