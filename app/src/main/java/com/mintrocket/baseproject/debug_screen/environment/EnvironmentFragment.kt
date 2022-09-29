package com.mintrocket.baseproject.debug_screen.environment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.GenericItem
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.mikepenz.fastadapter.diff.FastAdapterDiffUtil
import com.mintrocket.baseproject.R
import com.mintrocket.baseproject.databinding.FragmentDebugEnvironmentBinding
import com.mintrocket.baseproject.debug_screen.environment.data.EnvironmentEntry
import com.mintrocket.uicore.observeEvent
import com.mintrocket.uicore.onClick
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.system.exitProcess


class EnvironmentFragment : Fragment(R.layout.fragment_debug_environment) {

    companion object {
        private const val TAG_ENV_ADD = "env_add"
    }

    private val binding by viewBinding<FragmentDebugEnvironmentBinding>()
    private val viewModel by viewModel<EnvironmentViewModel>()

    private val environmentsAdapter = ItemAdapter<GenericItem>()
    private val fastAdapter = FastAdapter.with(environmentsAdapter)

    private val entryClickListener = { item: EnvironmentEntry ->
        viewModel.onSelect(item)
    }
    private val entryEditClickListener = { item: EnvironmentEntry ->
        showAddDialog(item)
    }
    private val entryDeleteClickListener = { item: EnvironmentEntry ->
        viewModel.onDelete(item)
    }
    private val entryAddClickListener = {
        showAddDialog(null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvEnvironments.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = fastAdapter
            itemAnimator = DefaultItemAnimator().apply {
                supportsChangeAnimations = false
            }
        }

        binding.btApply.onClick {
            triggerRebirth(requireContext())
        }

        viewModel.screenState.onEach { state ->
            val items = state.entries.map {
                ItemEnvironment(
                    data = it,
                    selected = it.id == state.selectedEntryId,
                    clickListener = entryClickListener,
                    editClickListener = entryEditClickListener,
                    deleteClickListener = entryDeleteClickListener
                )
            } + ItemEnvironmentAdd(entryAddClickListener)
            FastAdapterDiffUtil[environmentsAdapter] = items
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.errorEvent.observeEvent(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }
    }

    private fun triggerRebirth(context: Context) {
        context.packageManager
            .getLaunchIntentForPackage(context.packageName)
            ?.let { Intent.makeRestartActivityTask(it.component) }
            ?.also { context.startActivity(it) }
        exitProcess(0)
    }

    private fun showAddDialog(entry: EnvironmentEntry?) {
        EnvironmentAddFragment.newInstance(entry).show(childFragmentManager, TAG_ENV_ADD)
    }
}