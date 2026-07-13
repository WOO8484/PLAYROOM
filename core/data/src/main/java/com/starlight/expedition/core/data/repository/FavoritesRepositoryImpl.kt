package com.starlight.expedition.core.data.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import com.starlight.expedition.core.data.local.PreferencesKeys
import com.starlight.expedition.core.data.local.starlightDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FavoritesRepositoryImpl(
    private val context: Context
) : FavoritesRepository {

    override fun observeFavoriteIds(): Flow<Set<String>> =
        context.starlightDataStore.data.map { preferences ->
            preferences[PreferencesKeys.FAVORITE_GAME_IDS] ?: emptySet()
        }

    override suspend fun setFavorite(gameId: String, favorite: Boolean) {
        context.starlightDataStore.edit { preferences ->
            val current = preferences[PreferencesKeys.FAVORITE_GAME_IDS] ?: emptySet()
            preferences[PreferencesKeys.FAVORITE_GAME_IDS] = if (favorite) {
                current + gameId
            } else {
                current - gameId
            }
        }
    }
}
