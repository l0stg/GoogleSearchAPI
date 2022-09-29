package com.mintrocket.baseproject.debug_screen.environment.data

import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.core.util.PatternsCompat
import com.mintrocket.datacore.AppBuildConfig
import com.mintrocket.datacore.utils.Trigger
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import java.util.*

class EnvironmentRepository(
    private val appBuildConfig: AppBuildConfig,
    private val preferences: SharedPreferences,
    private val moshi: Moshi
) {
    companion object {
        private const val DEFAULT_ID = "default"
        private const val KEY_ENTRY_ID = "debug.env.entry_id"
        private const val KEY_CUSTOM_ENTRIES = "debug.env.custom_entries"
    }

    private val environmentsAdapter by lazy {
        val type = Types.newParameterizedType(List::class.java, EnvironmentEntryJson::class.java)
        moshi.adapter<List<EnvironmentEntryJson>>(type)
    }

    private val defaultEntry = EnvironmentEntry(
        DEFAULT_ID,
        "Default",
        appBuildConfig.baseUrl,
        false
    )

    private val presets = listOf(
        defaultEntry,
        EnvironmentEntry(
            "stage",
            "Stage",
            "https://mintrocket.stage.ru/",
            false
        ),
        EnvironmentEntry(
            "prod",
            "Production",
            "https://mintrocket.prod.ru/",
            false
        )
    )

    private val triggerFlow = MutableStateFlow(Trigger)

    private var savedEntryId: String
        get() = preferences.getString(KEY_ENTRY_ID, DEFAULT_ID) ?: DEFAULT_ID
        set(value) = preferences.edit { putString(KEY_ENTRY_ID, value) }

    private var customEntries: List<EnvironmentEntry>
        get() = preferences.getString(KEY_CUSTOM_ENTRIES, null)
            ?.let { environmentsAdapter.fromJson(it) }
            ?.let { entries ->
                entries.map {
                    EnvironmentEntry(it.id, it.name, it.url, true)
                }
            }
            ?: emptyList()
        set(value) = preferences.edit {
            val jsonEntries = value.map { EnvironmentEntryJson(it.id, it.name, it.url) }
            val jsonString = environmentsAdapter.toJson(jsonEntries)
            putString(KEY_CUSTOM_ENTRIES, jsonString)
        }

    fun observeSelectedEntry(): Flow<EnvironmentEntry> = triggerFlow
        .map { getSelectedEntry() }
        .distinctUntilChanged()

    fun getSelectedEntry(): EnvironmentEntry {
        return getEntries().find { it.id == savedEntryId } ?: defaultEntry
    }

    fun selectEntry(entryId: String) {
        requireEntryContains(entryId)
        savedEntryId = entryId
        triggerFlow.value = Trigger
    }

    fun observeEntries(): Flow<List<EnvironmentEntry>> = triggerFlow
        .map { getEntries() }
        .distinctUntilChanged()

    fun addEntry(name: String, url: String): EnvironmentEntry {
        requireEntryValidInput(name, url)
        val newEntry = EnvironmentEntry(
            id = UUID.randomUUID().toString(),
            name = name,
            url = url,
            editable = true
        )
        customEntries = customEntries + newEntry
        triggerFlow.value = Trigger
        return newEntry
    }

    fun removeEntry(entryId: String) {
        requireEntryCanChanges(entryId)
        customEntries = customEntries.filter { it.id != entryId }
        if (entryId == savedEntryId) {
            savedEntryId = DEFAULT_ID
        }
        triggerFlow.value = Trigger
    }

    fun updateEntry(entryId: String, name: String, url: String) {
        requireEntryCanChanges(entryId)
        requireEntryValidInput(name, url)
        customEntries = customEntries.map {
            if (it.id == entryId) {
                it.copy(name = name, url = url)
            } else {
                it
            }
        }
        triggerFlow.value = Trigger
    }

    private fun getEntries(): List<EnvironmentEntry> = presets + customEntries

    private fun findEntry(entryId: String): EnvironmentEntry? {
        return getEntries().find { it.id == entryId }
    }

    private fun requireEntryValidInput(name: String, url: String) {
        require(name.isNotEmpty()) { "Name is empty" }
        require(url.isNotEmpty()) { "Url is empty" }
        require(PatternsCompat.WEB_URL.matcher(url).matches()) { "Url is not matcher by pattern" }
    }

    private fun requireEntryContains(entryId: String) {
        requireNotNull(findEntry(entryId)) { "Not found entry id" }
    }

    private fun requireEntryCanChanges(entryId: String) {
        val entry = findEntry(entryId)
        requireNotNull(entry) { "Not found entry by id" }
        require(entry.editable) { "Entry is not editable" }
    }
}