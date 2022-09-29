package com.mintrocket.mobile.screens.pagerscreen

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import by.kirich1409.viewbindingdelegate.viewBinding
import com.mintrocket.mobile.R
import com.mintrocket.mobile.databinding.FragmentPagerBinding
import com.mintrocket.navigation.BackButtonListener
import com.mintrocket.uicore.getExtra
import com.mintrocket.uicore.withArgs
import org.koin.androidx.viewmodel.ext.android.viewModel

class PagerFragment : Fragment(R.layout.fragment_pager), BackButtonListener {

    companion object {
        private const val EXTRA_SCREEN_SCOPE = "extra_screen_scope"

        fun newInstance(screenScope: String?) = PagerFragment().withArgs(
            EXTRA_SCREEN_SCOPE to screenScope
        )
    }

    private val viewModel by viewModel<PagerViewModel>()

    private val binding by viewBinding<FragmentPagerBinding>()

    private val pagesHistory = LinkedHashSet<Int>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding.vpMain) {
            adapter = VpMainAdapter(this@PagerFragment)
            orientation = ViewPager2.ORIENTATION_HORIZONTAL
            offscreenPageLimit = PagerHelper.getPagesCount()
            isUserInputEnabled = false
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    val menuId = PagerHelper.getPageForPosition(position).menuId
                    binding.bnvMain.selectedItemId = menuId
                }
            })
        }

        binding.bnvMain.setOnItemSelectedListener { item ->
            val position = PagerHelper.getPositionForMenuItemId(item.itemId)
            pagesHistory.remove(position)
            pagesHistory.add(binding.vpMain.currentItem)
            binding.vpMain.setCurrentItem(position, false)
            true
        }

        binding.vpMain.setOnApplyWindowInsetsListener { _, insets ->
            childFragmentManager.fragments.forEach { it.view?.onApplyWindowInsets(insets) }
            insets
        }

        //navigate to extra position only once
        getExtra<String>(EXTRA_SCREEN_SCOPE)?.also {
            val position = PagerHelper.getPositionForScopeName(it)
            binding.vpMain.setCurrentItem(position, false)
            arguments?.remove(EXTRA_SCREEN_SCOPE)
        }
    }

    override fun onBackPressed(): Boolean {
        val stackHandled = (childFragmentManager.fragments.lastOrNull { it.isResumed }
                as? BackButtonListener)?.onBackPressed() ?: false

        if (!stackHandled) {
            pagesHistory.lastOrNull()?.let {
                pagesHistory.remove(it)
                binding.vpMain.setCurrentItem(it, false)
                return true
            }
        }

        return stackHandled
    }
}