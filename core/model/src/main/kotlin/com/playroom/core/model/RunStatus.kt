package com.playroom.core.model

/** Visual severity used to color a [RunStatus] badge on a game card. */
enum class RunStatusType {
    READY,
    WARNING,
    ERROR,
}

/** Whether a game can be launched right now, and the label shown to the user. */
data class RunStatus(
    val label: String,
    val type: RunStatusType,
)
