package com.starlight.expedition.core.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json

/**
 * 등록 폴더 목록을 기존 DataStore에 JSON 문자열로 저장합니다(실행지시서 22.1절).
 * 게임 수가 많아질 수 있는 게임 목록 자체는 [GameLibraryFileStore]가 별도 파일로 저장합니다.
 */
class GameFolderPreferences(private val context: Context) {

    private val json = Json { ignoreUnknownKeys = true }

    fun observeFolders(): Flow<List<GameFolderDto>> =
        context.starlightDataStore.data.map { preferences ->
            val raw = preferences[PreferencesKeys.GAME_FOLDERS_JSON] ?: return@map emptyList()
            try {
                json.decodeFromString(GameFolderListDto.serializer(), raw).folders
            } catch (e: SerializationException) {
                emptyList()
            } catch (e: IllegalArgumentException) {
                emptyList()
            }
        }

    suspend fun saveFolders(folders: List<GameFolderDto>) {
        val encoded = json.encodeToString(GameFolderListDto.serializer(), GameFolderListDto(folders))
        context.starlightDataStore.edit { preferences ->
            preferences[PreferencesKeys.GAME_FOLDERS_JSON] = encoded
        }
    }
}
