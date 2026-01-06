package com.example.run.presentation.active_run

import com.example.presentation.ui.UIText

sealed interface ActiveRunEvent {
    data class Error(val error: UIText): ActiveRunEvent
    data object RunSaved: ActiveRunEvent
}