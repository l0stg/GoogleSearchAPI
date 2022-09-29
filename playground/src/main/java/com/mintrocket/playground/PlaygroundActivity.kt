package com.mintrocket.playground

import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.mintrocket.playground.databinding.ActivityPlaygroundBinding
import com.mintrocket.playground.fragments.DialogsShowcaseFragment

class PlaygroundActivity : AppCompatActivity() {

    val pages = listOf(
        SamplePageItem(R.string.dialogs, DialogsShowcaseFragment())
    )

    private val binding by viewBinding<ActivityPlaygroundBinding>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playground)

        binding.vpPlayground.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount() = pages.size

            override fun createFragment(position: Int) =
                pages[position].fragment
        }

        TabLayoutMediator(binding.tabPlayground, binding.vpPlayground) { tab, position ->
            tab.text = getString(pages[position].titleRes)
        }.attach()
    }
}

data class SamplePageItem(
    @StringRes
    val titleRes: Int,
    val fragment: Fragment
)