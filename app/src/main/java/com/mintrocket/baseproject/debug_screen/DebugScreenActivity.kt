package com.mintrocket.baseproject.debug_screen

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import by.kirich1409.viewbindingdelegate.viewBinding
import com.mintrocket.baseproject.R
import com.mintrocket.baseproject.databinding.ActivityDebugScreenBinding
import com.mintrocket.baseproject.debug_screen.environment.EnvironmentFragment
import com.mintrocket.baseproject.debug_screen.features.DebugFeaturesFragment
import com.mintrocket.baseproject.debug_screen.logreader.DebugLogReaderFragment

class DebugScreenActivity : AppCompatActivity(R.layout.activity_debug_screen) {

    companion object {
        fun newIntent(context: Context) = Intent(context, DebugScreenActivity::class.java)
    }

    private val binding by viewBinding<ActivityDebugScreenBinding>()

    private val menusIds = listOf(
        R.id.menu_debug_pager_features,
        R.id.menu_debug_pager_environment,
        R.id.menu_debug_pager_logreader
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        with(binding.vpDebug) {
            adapter = PagerDebugAdapter(menusIds, this@DebugScreenActivity)
            orientation = ViewPager2.ORIENTATION_HORIZONTAL
            isUserInputEnabled = false
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    val id = menusIds[position]
                    binding.bnvDebug.selectedItemId = id
                }
            })
        }
        binding.bnvDebug.setOnItemSelectedListener {
            val position = menusIds.indexOf(it.itemId)
            binding.vpDebug.currentItem = position
            return@setOnItemSelectedListener true
        }
    }

    class PagerDebugAdapter(
        private val menuIds: List<Int>,
        activity: AppCompatActivity
    ) : FragmentStateAdapter(activity) {

        override fun getItemCount(): Int = menuIds.size

        override fun createFragment(position: Int): Fragment = when (menuIds[position]) {
            R.id.menu_debug_pager_features -> DebugFeaturesFragment()
            R.id.menu_debug_pager_environment -> EnvironmentFragment()
            R.id.menu_debug_pager_logreader -> DebugLogReaderFragment()
            else -> throw IllegalStateException("unknown screen for position $position")
        }
    }
}