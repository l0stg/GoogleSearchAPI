package com.mintrocket.uicore

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

fun ViewPager2.setupWithTabLayout(
    fragment: Fragment,
    tabLayout: TabLayout,
    vararg pages: ViewPagerPage
) {
    adapter = object : FragmentStateAdapter(fragment) {
        override fun getItemCount() = pages.size

        override fun createFragment(position: Int) =
            pages[position].fragmentFabric.invoke()
    }

    TabLayoutMediator(tabLayout, this) { tab, position ->
        tab.text = pages[position].tabTitle
    }.attach()
}

data class ViewPagerPage(
    val tabTitle: String,
    val fragmentFabric: () -> Fragment
)