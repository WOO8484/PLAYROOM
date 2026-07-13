package com.starlight.expedition.core.data.local

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey

internal object PreferencesKeys {
    val DARK_MODE_ENABLED = booleanPreferencesKey("dark_mode_enabled")
    val SOUND_ENABLED = booleanPreferencesKey("sound_enabled")
    val AUTO_SAVE_ENABLED = booleanPreferencesKey("auto_save_enabled")
    val FAVORITE_GAME_IDS = stringSetPreferencesKey("favorite_game_ids")

    /** 등록 폴더 목록을 [GameFolderListDto] JSON 문자열로 저장하는 키입니다. */
    val GAME_FOLDERS_JSON = stringPreferencesKey("game_folders_json")
}
