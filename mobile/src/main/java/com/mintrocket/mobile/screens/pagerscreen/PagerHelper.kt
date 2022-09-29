package com.mintrocket.mobile.screens.pagerscreen

import androidx.annotation.IdRes
import com.mintrocket.mobile.R
import com.mintrocket.mobile.navigation.ContentListScreen
import com.mintrocket.mobile.navigation.HomeScreen
import com.mintrocket.mobile.navigation.ToggleShowCaseScreen
import com.mintrocket.navigation.screens.BaseAppScreen

data class PageItem(
    @IdRes val menuId: Int,
    val scopeName: String
)

object PagerHelper {

    const val SCOPE_NAME_ONE = "scope_one"
    const val SCOPE_NAME_TWO = "scope_two"
    const val SCOPE_NAME_THREE = "scope_three"
    const val SCOPE_NAME_FOUR = "scope_four"
    const val SCOPE_NAME_FIVE = "scope_five"

    private val pages by lazy {
        listOf(
            PageItem(R.id.menu_pager_one, SCOPE_NAME_ONE),
            PageItem(R.id.menu_pager_two, SCOPE_NAME_TWO),
            PageItem(R.id.menu_pager_three, SCOPE_NAME_THREE),
            PageItem(R.id.menu_pager_four, SCOPE_NAME_FOUR),
            PageItem(R.id.menu_pager_five, SCOPE_NAME_FIVE)
        )
    }

    fun getPagesCount() = pages.size

    fun getPageForPosition(position: Int): PageItem {
        val menuId = pages.getOrNull(position)
        return requireNotNull(menuId) { "Unknown page for position $position" }
    }

    fun getPositionForMenuItemId(id: Int): Int {
        val position = pages.indexOfFirst { it.menuId == id }
        require(position != -1) { "Unknown page for menu id $id" }
        return position
    }

    fun getPositionForScopeName(scopeName: String): Int {
        val position = pages.indexOfFirst { it.scopeName == scopeName }
        require(position != -1) { "Unknown page for scope name $scopeName" }
        return position
    }

    fun getScreenForScopeName(scopeName: String): BaseAppScreen {
        val screen = when (scopeName) {
            SCOPE_NAME_ONE -> HomeScreen()
            SCOPE_NAME_TWO -> ToggleShowCaseScreen()
            SCOPE_NAME_THREE -> ContentListScreen("List three")
            SCOPE_NAME_FOUR -> ContentListScreen("List four")
            SCOPE_NAME_FIVE -> ContentListScreen("List five")
            else -> null
        }
        return requireNotNull(screen) { "Unknown screen for scope name $scopeName" }
    }

}