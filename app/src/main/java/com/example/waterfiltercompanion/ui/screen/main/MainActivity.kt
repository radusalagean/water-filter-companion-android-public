package com.example.waterfiltercompanion.ui.screen.main

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import com.example.waterfiltercompanion.common.dialog.MaterialDialogHelper
import com.example.waterfiltercompanion.ui.theme.WaterFilterCompanionTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var materialDialogHelper: MaterialDialogHelper

    private val viewModel: MainViewModel by viewModels()

    private val eventsFlow by lazy {
        viewModel.eventsFlow.onEach { event ->
            when(event) {
                is MainViewModel.Event.ShowDatePicker ->
                    materialDialogHelper.requestDateAsTimestamp(
                        currentDateTimestamp = event.currentDateTimestamp,
                        cb = event.cb
                    )
            }
        }
    }
    private var eventsJob: Job? = null

    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { MainScreen(viewModel) }
    }

    override fun onStart() {
        super.onStart()
        eventsJob = eventsFlow.launchIn(lifecycleScope)
    }

    override fun onStop() {
        super.onStop()
        eventsJob?.cancel()
        eventsJob = null
    }
}
