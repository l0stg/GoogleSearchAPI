package com.mintrocket.mobile.navigation

import com.mintrocket.mobile.screens.contentlist.ContentListFragment
import com.mintrocket.navigation.screens.FragmentScreen


class ContentListScreen : FragmentScreen() {
    override fun createFragment() = ContentListFragment()
}
