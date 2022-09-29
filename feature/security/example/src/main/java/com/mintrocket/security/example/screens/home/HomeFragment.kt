package com.mintrocket.security.example.screens.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.mintrocket.navigation.navigator.ApplicationNavigator
import com.mintrocket.security.example.R
import com.mintrocket.security.example.databinding.FragmentHomeBinding
import com.mintrocket.security.example.screens.SecuritySettingsScreen
import org.koin.android.ext.android.inject

class HomeFragment : Fragment(R.layout.fragment_home) {

    private val navigator by inject<ApplicationNavigator>()

    private val binding by viewBinding<FragmentHomeBinding>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.settings.setOnClickListener {
            navigator.navigateTo(SecuritySettingsScreen())
        }
    }
}