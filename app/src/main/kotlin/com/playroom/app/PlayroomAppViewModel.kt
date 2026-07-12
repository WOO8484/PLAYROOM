package com.playroom.app

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import com.playroom.core.model.AppSkin
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

private const val PREFS_NAME = "playroom_prefs"
private const val KEY_HAS_SEEN_ONBOARDING = "has_seen_onboarding"

/**
 * App-wide state that outlives any single tab: whether onboarding has been
 * shown before (persisted — the only real file/storage write this whole
 * UI-test build performs, matching work order §12) and the active
 * [AppSkin] (kept in memory only; nothing else here touches disk).
 */
class PlayroomAppViewModel(application: Application) : AndroidViewModel(application) {

    private val prefs = application.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    private val _hasSeenOnboarding = MutableStateFlow(prefs.getBoolean(KEY_HAS_SEEN_ONBOARDING, false))
    val hasSeenOnboarding: StateFlow<Boolean> = _hasSeenOnboarding.asStateFlow()

    private val _skin = MutableStateFlow(AppSkin.LIGHT)
    val skin: StateFlow<AppSkin> = _skin.asStateFlow()

    fun markOnboardingSeen() {
        prefs.edit().putBoolean(KEY_HAS_SEEN_ONBOARDING, true).apply()
        _hasSeenOnboarding.value = true
    }

    fun replayOnboarding() {
        prefs.edit().putBoolean(KEY_HAS_SEEN_ONBOARDING, false).apply()
        _hasSeenOnboarding.value = false
    }

    fun setSkin(newSkin: AppSkin) {
        _skin.value = newSkin
    }
}
