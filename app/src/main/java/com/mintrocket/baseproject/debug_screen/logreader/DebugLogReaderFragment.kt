package com.mintrocket.baseproject.debug_screen.logreader

import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.core.view.children
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import androidx.transition.TransitionSet
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.chip.Chip
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.GenericItem
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.mikepenz.fastadapter.diff.FastAdapterDiffUtil
import com.mintrocket.baseproject.R
import com.mintrocket.baseproject.databinding.DialogDebugDetailLogLineBinding
import com.mintrocket.baseproject.databinding.FragmentDebugLogreaderBinding
import com.mintrocket.baseproject.debug_screen.logreader.data.LogCatLine
import com.mintrocket.uicore.copyToClipboard
import com.mintrocket.uicore.getColorByAttr
import com.mintrocket.uicore.onClick
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.ldralighieri.corbind.recyclerview.scrollEvents


class DebugLogReaderFragment : Fragment(R.layout.fragment_debug_logreader) {

    private val linesAdapter = ItemAdapter<GenericItem>()
    private val fastAdapter = FastAdapter.with(linesAdapter)

    private val binding by viewBinding<FragmentDebugLogreaderBinding>()

    private val viewModel by viewModel<LogReaderViewModel>()

    private val lineClickListener = { line: LogCatLine ->
        showDetail(line)
    }

    private val lineLongClickListener = { line: LogCatLine ->
        showContextMenu(line)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initObservers()
    }

    private fun initView() {
        initSearch()
        initLogsRecycler()
        initControls()
        initFilterContainer()
    }

    private fun initLogsRecycler() {
        binding.rvLogs.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, true)
            adapter = fastAdapter
            itemAnimator = DefaultItemAnimator().apply {
                supportsChangeAnimations = false
            }
        }
        binding.rvLogs.scrollEvents()
            .map { getFirstOffset() <= 0 }
            .distinctUntilChanged()
            .onEach {
                binding.btAlignStart.setIconTint(it)
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun initSearch() {
        with(binding) {
            etSearch.addTextChangedListener {
                viewModel.setQuery(it?.toString().orEmpty())
                rvLogs.scrollToPosition(0)
                btClearSearch.isInvisible = it.isNullOrEmpty()
            }

            btClearSearch.onClick {
                etSearch.text = null
            }
        }
    }

    private fun initControls() {
        with(binding) {
            btAlignStart.onClick {
                rvLogs.scrollToPosition(0)
            }

            btSuggestionAnalytics.onClick {
                etSearch.setText(btSuggestionAnalytics.text)
            }

            btFilter.onClick {
                TransitionManager.beginDelayedTransition(root, AutoTransition().apply {
                    ordering = TransitionSet.ORDERING_TOGETHER
                })
                val filterVisible = !clFilterContainer.root.isVisible
                clFilterContainer.root.isVisible = filterVisible
                btFilter.setIconTint(filterVisible)
            }

            btDeleteLogs.onClick {
                viewModel.onDeleteAllLogsClick()
            }
        }
    }

    private fun initFilterContainer() {
        with(binding.clFilterContainer) {
            swLatestSession.setOnCheckedChangeListener { _, isChecked ->
                viewModel.setOnlyLatestPid(isChecked)
            }
            cgLogLevels.children.filterIsInstance<Chip>().forEach { chip ->
                chip.onClick {
                    val levels = cgLogLevels.checkedChipIds.mapNotNull { idToLogLevel(it) }
                    viewModel.setFilterLevels(levels)
                }
            }
        }
    }

    private fun initObservers() {
        viewModel.filterFlow.onEach {
            with(binding.clFilterContainer) {
                swLatestSession.isChecked = it.onlyLatestPid
                cgLogLevels.clearCheck()
                it.levels
                    .mapNotNull { logLevelToId(it) }
                    .forEach {
                        cgLogLevels.check(it)
                    }
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.progress.onEach {
            binding.pbLoading.isVisible = it
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.logLines
            .map { logs ->
                logs.map {
                    ItemDebugLogLine(
                        it,
                        lineClickListener,
                        lineLongClickListener
                    )
                }
            }
            .flowOn(Dispatchers.IO)
            .onEach {
                val needScrollToFirst = getFirstOffset() <= 0
                FastAdapterDiffUtil.set(linesAdapter, it)
                if (needScrollToFirst) {
                    binding.rvLogs.scrollToPosition(0)
                }
            }
            .flowOn(Dispatchers.Main)
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun showContextMenu(line: LogCatLine) {
        val items = listOf(
            "Copy all" to {
                val text = "${line.level}, ${line.date}, ${line.tag}\n${line.message}"
                requireContext().copyToClipboard("log", text)
            },
            "Copy message" to {
                requireContext().copyToClipboard("log", line.message)
            }
        )
        MaterialAlertDialogBuilder(requireContext())
            .setItems(items.map { it.first }.toTypedArray()) { _, which ->
                items[which].second.invoke()
            }
            .show()
    }

    private fun showDetail(line: LogCatLine) {
        val binding = DialogDebugDetailLogLineBinding.inflate(LayoutInflater.from(requireContext()))
        binding.tvMeta.text =
            "Level:${line.toLevelName()} · Date:${line.formatDate()} · PID:${line.pid} · TID:${line.tid} · Tag:${line.tag}"
        binding.tvMessage.text = line.message
        binding.tvMeta.setTextColor(line.toLevelColor(binding.tvMeta.context))
        binding.root.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        MaterialAlertDialogBuilder(requireContext())
            .setView(binding.root)
            .show()
    }

    private fun logLevelToId(level: Int): Int? = when (level) {
        Log.DEBUG -> R.id.btLogLevelDebug
        Log.ERROR -> R.id.btLogLevelError
        Log.INFO -> R.id.btLogLevelInfo
        Log.WARN -> R.id.btLogLevelWarning
        Log.VERBOSE -> R.id.btLogLevelVerbose
        Log.ASSERT -> R.id.btLogLevelAssert
        else -> null
    }

    private fun idToLogLevel(id: Int): Int? = when (id) {
        R.id.btLogLevelDebug -> Log.DEBUG
        R.id.btLogLevelError -> Log.ERROR
        R.id.btLogLevelInfo -> Log.INFO
        R.id.btLogLevelWarning -> Log.WARN
        R.id.btLogLevelVerbose -> Log.VERBOSE
        R.id.btLogLevelAssert -> Log.ASSERT
        else -> null
    }

    private fun ImageButton.setIconTint(active: Boolean) {
        val color = if (active) {
            context.getColorByAttr(R.attr.colorPrimary)
        } else {
            context.getColorByAttr(android.R.attr.textColorSecondary)
        }
        imageTintList = ColorStateList.valueOf(color)
    }

    private fun getFirstOffset(): Int {
        return (binding.rvLogs.layoutManager as? LinearLayoutManager?)?.run {
            val offset = binding.rvLogs.computeVerticalScrollOffset()
            val range = binding.rvLogs.computeVerticalScrollRange()
            val extent = binding.rvLogs.computeVerticalScrollExtent()
            if (reverseLayout) {
                range - offset - extent
            } else {
                offset
            }
        } ?: 0
    }
}