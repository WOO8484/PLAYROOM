package com.starlight.expedition

import android.content.Context
import com.starlight.expedition.core.data.repository.FavoritesRepository
import com.starlight.expedition.core.data.repository.FavoritesRepositoryImpl
import com.starlight.expedition.core.data.repository.GameRepository
import com.starlight.expedition.core.data.repository.GameRepositoryImpl
import com.starlight.expedition.core.data.repository.RecommendationRepository
import com.starlight.expedition.core.data.repository.RecommendationRepositoryImpl
import com.starlight.expedition.core.data.repository.SettingsRepository
import com.starlight.expedition.core.data.repository.SettingsRepositoryImpl

/**
 * 이번 1차 개발에서는 Hilt를 사용하지 않으므로, 이 단순한 컨테이너가
 * Repository 구현체를 한 곳에서 생성해 각 화면의 ViewModel에 생성자로 전달합니다.
 */
class AppContainer(context: Context) {

    val favoritesRepository: FavoritesRepository = FavoritesRepositoryImpl(context.applicationContext)
    val gameRepository: GameRepository = GameRepositoryImpl(favoritesRepository)
    val settingsRepository: SettingsRepository = SettingsRepositoryImpl(context.applicationContext)
    val recommendationRepository: RecommendationRepository = RecommendationRepositoryImpl()
}
