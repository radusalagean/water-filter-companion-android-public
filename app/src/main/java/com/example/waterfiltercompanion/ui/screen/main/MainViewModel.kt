package com.example.waterfiltercompanion.ui.screen.main

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.waterfiltercompanion.R
import com.example.waterfiltercompanion.common.date.DateHelper
import com.example.waterfiltercompanion.datapersistence.DataModel
import com.example.waterfiltercompanion.datapersistence.LocalRepository
import com.example.waterfiltercompanion.ui.components.capacityinputdialog.CapacityInputDialogConfig
import com.example.waterfiltercompanion.ui.components.confirmationdialog.ConfirmationDialogConfig
import com.example.waterfiltercompanion.ui.components.infobar.InfoBarMessage
import com.example.waterfiltercompanion.ui.components.infobar.InfoBarType
import com.example.waterfiltercompanion.watercontrol.ConsumeWaterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val dateHelper: DateHelper,
    private val localRepository: LocalRepository,
    private val consumeWaterUseCase: ConsumeWaterUseCase
) : ViewModel() {

    // Global state
    var editMode by mutableStateOf(false)
        private set
    var totalCapacity: Int? by mutableStateOf(null)
        private set
    var remainingCapacity: Int? by mutableStateOf(null)
        private set
    var installedOn: Long? by mutableStateOf(null)
        private set

    // Candidate values
    var totalCapacityCandidate: String? by mutableStateOf(null)
        private set
    var remainingCapacityCandidate: String? by mutableStateOf(null)
        private set
    var installedOnCandidate: Long? by mutableStateOf(null)
        private set

    // Dialogs
    var capacityInputDialogConfig: CapacityInputDialogConfig? by mutableStateOf(null)
        private set
    var confirmationDialogConfig: ConfirmationDialogConfig? by mutableStateOf(null)
        private set

    // Info Bar
    var infoBarMessage: InfoBarMessage? by mutableStateOf(null)
        private set

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
    val consumeFabVisible: Boolean by derivedStateOf {
        !editMode && remainingCapacity?.let { it >= ConsumeWaterUseCase.UNITS_TO_CONSUME } ?: false
    }

    // Events
    private val eventsChannel = Channel<Event>()
    val eventsFlow = eventsChannel.receiveAsFlow()

    init {
        loadDataAsync()
    }

    private fun loadDataAsync() {
        viewModelScope.launch {
            loadDataSync()
        }
    }

    private suspend fun loadDataSync() {
        localRepository.getData().let { model ->
            totalCapacity = model.totalCapacity
            remainingCapacity = model.remainingCapacity
            installedOn = model.installedOn
        }
    }

    fun onEdit() {
        syncCandidateValues()
        editMode = true
    }

    fun onCancel() {
        leaveEditMode()
    }

    fun leaveEditMode() {
        editMode = false
        clearCandidateValues()
    }

    fun onSave() {
        val tc = totalCapacityCandidate?.toIntOrNull()
        val rc = remainingCapacityCandidate?.toIntOrNull()
        val io = installedOnCandidate
        if (tc == null || rc == null || io == null ||
            !areCapacityValuesValid(tc = tc, rc = rc) ||
            io > System.currentTimeMillis()) {
            infoBarMessage = InfoBarMessage(
                type = InfoBarType.ERROR,
                textStringRes = R.string.message_invalid_input,
                displayTimeSeconds = 5L
            )
            return
        }
        viewModelScope.launch {
            val dataModel = DataModel(
                totalCapacity = totalCapacityCandidate?.toIntOrNull(),
                remainingCapacity = remainingCapacityCandidate?.toIntOrNull(),
                installedOn = installedOnCandidate
            )
            localRepository.setData(dataModel)
            loadDataSync()
            leaveEditMode()
            infoBarMessage = InfoBarMessage(
                type = InfoBarType.INFO,
                textStringRes = R.string.message_data_saved
            )
        }
    }

    fun onClearData() {
        confirmationDialogConfig = ConfirmationDialogConfig(
            titleStringRes = R.string.clear_data_confirmation_dialog_title,
            onCancel = ::onClearDataCancel,
            onConfirm = ::onClearDataConfirm
        )
    }

    fun onClearDataCancel() {
        confirmationDialogConfig = null
    }

    fun onClearDataConfirm() {
        viewModelScope.launch {
            localRepository.clearData()
            loadDataSync()
            confirmationDialogConfig = null
            leaveEditMode()
            infoBarMessage = InfoBarMessage(
                type = InfoBarType.WARN,
                textStringRes = R.string.message_data_cleared
            )
        }
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

    fun onInfoBarMessageTimeout() {
        infoBarMessage = null
    }

    fun onConsume() {
        viewModelScope.launch {
            consumeWaterUseCase(currentCapacity = remainingCapacity)
            remainingCapacity = localRepository.getRemainingCapacity()
            infoBarMessage = InfoBarMessage(
                type = InfoBarType.INFO,
                textStringRes = R.string.message_consumed,
                args = listOf(ConsumeWaterUseCase.UNITS_TO_CONSUME)
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