package com.mintrocket.mobile.screens.toggle_show_case

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.mintrocket.data.feature_toggling.features.LocalFeatures
import com.mintrocket.mobile.R
import com.mintrocket.mobile.databinding.FragmentTogglingShowCaseBinding
import org.koin.android.ext.android.inject

class ToggleShowCaseFragment : Fragment(R.layout.fragment_toggling_show_case) {

    companion object {
        fun newInstance() = ToggleShowCaseFragment()
    }

    private val localFeatures by inject<LocalFeatures>()

    private val binding by viewBinding<FragmentTogglingShowCaseBinding>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ivFeatureImage.isVisible = localFeatures.isImageOneEnabled()
        binding.btFeature.isVisible = localFeatures.isShowButtonEnabled()
    }
}