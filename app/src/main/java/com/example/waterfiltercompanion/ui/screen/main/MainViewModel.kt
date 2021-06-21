package com.example.waterfiltercompanion.ui.screen.main

import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import com.example.waterfiltercompanion.common.date.DateHelper
import com.example.waterfiltercompanion.ui.components.capacityinputdialog.CapacityInputDialogConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import com.example.waterfiltercompanion.R

@HiltViewModel
class MainViewModel @Inject constructor(
    val dateHelper: DateHelper
) : ViewModel() {

    // Global state
    var editMode by mutableStateOf(false)
    var totalCapacity: Int? by mutableStateOf(null)
    var remainingCapacity: Int? by mutableStateOf(null)
    var installedOn: Long? by mutableStateOf(null)

    // Dialogs
    var capacityInputDialogConfig: CapacityInputDialogConfig? by mutableStateOf(null)

    // Derived states
    val installedOnFormatted: String? by derivedStateOf {
        installedOn?.let { dateHelper.getFormattedDate(it) }
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

    init {
        loadData()
    }

    fun loadData() {
        totalCapacity = 200
        remainingCapacity = 500
        installedOn = 1622494800000L
    }

    fun onEdit() {
        editMode = true
    }

    fun onCancel() {
        editMode = false
    }

    fun onSave() {

    }

    fun onClearData() {

    }

    fun onTotalCapacityClick() {
        capacityInputDialogConfig = CapacityInputDialogConfig(
            titleStringRes = R.string.capacity_input_dialog_total_title,
            initialInput = totalCapacity?.toString(),
            onSubmit = {},
            onDismiss = {
                capacityInputDialogConfig = null
            }
        )
    }

    fun onRemainingCapacityClick() {
        capacityInputDialogConfig = CapacityInputDialogConfig(
            titleStringRes = R.string.capacity_input_dialog_remaining_title,
            initialInput = remainingCapacity?.toString(),
            onSubmit = {},
            onDismiss = {
                capacityInputDialogConfig = null
            }
        )
    }

    fun onInstalledOnClick() {
        Log.d("MainViewModel", "onInstalledOnClick")
    }

    private fun areCapacityValuesValid(tc: Int, rc: Int): Boolean {
        return tc > 0 && rc > 0 && tc >= rc
    }
}