package com.example.waterfiltercompanion.ui.screen.main

import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.waterfiltercompanion.common.date.DateHelper
import com.example.waterfiltercompanion.ui.components.capacityinputdialog.CapacityInputDialogConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import com.example.waterfiltercompanion.R
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

@HiltViewModel
class MainViewModel @Inject constructor(
    val dateHelper: DateHelper
) : ViewModel() {

    // Global state
    var editMode by mutableStateOf(false)
    var totalCapacity: Int? by mutableStateOf(null)
    var remainingCapacity: Int? by mutableStateOf(null)
    var installedOn: Long? by mutableStateOf(null)

    // Candidate values
    var totalCapacityCandidate: String? by mutableStateOf(null)
    var remainingCapacityCandidate: String? by mutableStateOf(null)
    var installedOnCandidate: Long? by mutableStateOf(null)

    // Dialogs
    var capacityInputDialogConfig: CapacityInputDialogConfig? by mutableStateOf(null)

    // Derived states
    val installedOnFormatted: String? by derivedStateOf {
        installedOn?.let { dateHelper.getFormattedDate(it) }
    }
    val installedOnCandidateFormatted: String? by derivedStateOf {
        installedOnCandidate?.let { dateHelper.getFormattedDate(it) }
    }
    val daysInUse: Int? by derivedStateOf {
        installedOn?.let { dateHelper.getDaysSince(it) }
    }
    val waterFill: Float by derivedStateOf {
        val tc = totalCapacity
        val rc = remainingCapacity
        if (tc != null && rc != null && areCapacityValuesValid(tc = tc, rc = rc)) {
            rc.toFloat() / tc.toFloat()
        } else 0f
    }

    // Events
    private val eventsChannel = Channel<Event>()
    val eventsFlow = eventsChannel.receiveAsFlow()

    init {
        loadData()
    }

    fun loadData() {
        totalCapacity = 200
        remainingCapacity = 500
        installedOn = 1622494800000L
    }

    fun onEdit() {
        syncCandidateValues()
        editMode = true
    }

    fun onCancel() {
        editMode = false
        clearCandidateValues()
    }

    fun onSave() {

    }

    fun onClearData() {

    }

    fun onTotalCapacityClick() {
        capacityInputDialogConfig = CapacityInputDialogConfig(
            titleStringRes = R.string.capacity_input_dialog_total_title,
            initialInput = totalCapacity?.toString(),
            onSubmit = {
                totalCapacityCandidate = it
                capacityInputDialogConfig = null
            },
            onDismiss = {
                capacityInputDialogConfig = null
            }
        )
    }

    fun onRemainingCapacityClick() {
        capacityInputDialogConfig = CapacityInputDialogConfig(
            titleStringRes = R.string.capacity_input_dialog_remaining_title,
            initialInput = remainingCapacity?.toString(),
            onSubmit = {
                remainingCapacityCandidate = it
                capacityInputDialogConfig = null
            },
            onDismiss = {
                capacityInputDialogConfig = null
            }
        )
    }

    fun onInstalledOnClick() {
        viewModelScope.launch {
            eventsChannel.send(
                Event.ShowDatePicker(currentDateTimestamp = installedOnCandidate) { timestamp ->
                    installedOnCandidate = timestamp
                }
            )
        }
    }

    private fun syncCandidateValues() {
        totalCapacityCandidate = totalCapacity?.toString()
        remainingCapacityCandidate = remainingCapacity?.toString()
        installedOnCandidate = installedOn
    }

    private fun clearCandidateValues() {
        totalCapacityCandidate = null
        remainingCapacityCandidate = null
        installedOnCandidate = null
    }

    private fun areCapacityValuesValid(tc: Int, rc: Int): Boolean {
        return tc > 0 && rc > 0 && tc >= rc
    }

    sealed class Event {
        class ShowDatePicker(val currentDateTimestamp: Long?, val cb: (Long) -> Unit) : Event()
    }
}