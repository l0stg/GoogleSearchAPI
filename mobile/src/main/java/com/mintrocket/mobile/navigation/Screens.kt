package com.mintrocket.mobile.navigation

import com.mintrocket.mobile.screens.contentlist.ContentListFragment
import com.mintrocket.mobile.screens.home.HomeFragment
import com.mintrocket.mobile.screens.pagerscreen.PagerFragment
import com.mintrocket.mobile.screens.toggle_show_case.ToggleShowCaseFragment
import com.mintrocket.navigation.screens.FragmentScreen

class HomeScreen : FragmentScreen() {
    override fun createFragment() = HomeFragment.newInstance()
}

class ContentListScreen(val title: String) : FragmentScreen() {
    override fun createFragment() = ContentListFragment.newInstance(title)
}

class PagerScreen(val screenScope: String? = null) : FragmentScreen() {
    override fun createFragment() = PagerFragment.newInstance(screenScope)
}

class ToggleShowCaseScreen : FragmentScreen() {
    override fun createFragment() = ToggleShowCaseFragment.newInstance()
}