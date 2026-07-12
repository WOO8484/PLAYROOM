package com.starlight.expedition.core.data.local

import com.starlight.expedition.core.model.Game
import com.starlight.expedition.core.model.GameGenre
import com.starlight.expedition.core.model.Platform
import kotlinx.datetime.Clock
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.minutes

/**
 * 1차 개발용 로컬 샘플 게임 데이터입니다. 실행지시서 16절에 정의된 6개 게임을 제공합니다.
 * 즐겨찾기 여부는 여기서 제공하는 기본값이며, 실제 화면에는 FavoritesRepository에 저장된
 * 값과 결합한 결과가 반영됩니다.
 */
object SampleGameData {

    const val CONTINUE_GAME_ID = "starlight-odyssey"

    private val now = Clock.System.now()

    val games: List<Game> = listOf(
        Game(
            id = CONTINUE_GAME_ID,
            titleKo = "별빛 모험대",
            originalTitle = "Starlight Odyssey",
            genre = GameGenre.RPG,
            platform = Platform.NATIVE,
            descriptionLines = listOf(
                "별이 사라진 밤하늘을 되찾기 위해",
                "떠나는 따뜻한 판타지 모험입니다."
            ),
            coverResName = "cover_starlight",
            isFavorite = true,
            lastPlayedAt = now - 12.minutes,
            totalPlayMinutes = 612,
            playCount = 34,
            progressLabel = "자동 저장 완료"
        ),
        Game(
            id = "hero-legend",
            titleKo = "용사의 전설",
            originalTitle = "Legend of the Hero",
            genre = GameGenre.RPG,
            platform = Platform.NATIVE,
            descriptionLines = listOf(
                "고대 신화를 따라 떠나는",
                "판타지 모험 RPG입니다."
            ),
            coverResName = "cover_hero_legend",
            isFavorite = true,
            lastPlayedAt = now - 1.days,
            totalPlayMinutes = 480,
            playCount = 21,
            progressLabel = "챕터 4"
        ),
        Game(
            id = "light-puzzle",
            titleKo = "빛의 퍼즐",
            originalTitle = null,
            genre = GameGenre.PUZZLE,
            platform = Platform.NATIVE,
            descriptionLines = listOf(
                "반짝이는 조각을 맞춰 나가는",
                "편안한 두뇌 퍼즐 게임입니다."
            ),
            coverResName = "cover_hero_legend",
            isFavorite = true,
            lastPlayedAt = now - 3.days,
            totalPlayMinutes = 190,
            playCount = 40,
            progressLabel = "18단계"
        ),
        Game(
            id = "galaxy-rescue",
            titleKo = "은하 구조대",
            originalTitle = null,
            genre = GameGenre.ACTION,
            platform = Platform.NATIVE,
            descriptionLines = listOf(
                "별과 행성을 지키며 전진하는",
                "경쾌한 우주 액션 게임입니다."
            ),
            coverResName = "cover_hero_legend",
            isFavorite = true,
            lastPlayedAt = now - 6.days,
            totalPlayMinutes = 260,
            playCount = 18,
            progressLabel = "최고 점수 8,420"
        ),
        Game(
            id = "dragon-tower",
            titleKo = "드래곤 타워",
            originalTitle = null,
            genre = GameGenre.CASUAL,
            platform = Platform.NATIVE,
            descriptionLines = listOf(
                "신비한 탑을 오르며 성장하는",
                "가볍게 즐기는 모험 게임입니다."
            ),
            coverResName = "cover_hero_legend",
            isFavorite = true,
            lastPlayedAt = now - 9.days,
            totalPlayMinutes = 95,
            playCount = 9,
            progressLabel = "보상 대기 중"
        ),
        Game(
            id = "moonlight-run",
            titleKo = "달빛 달리기",
            originalTitle = null,
            genre = GameGenre.ACTION,
            platform = Platform.NATIVE,
            descriptionLines = listOf(
                "달빛이 내린 길을 가로지르는",
                "산뜻한 러너 액션 게임입니다."
            ),
            coverResName = "cover_hero_legend",
            isFavorite = false,
            lastPlayedAt = null,
            totalPlayMinutes = 0,
            playCount = 0,
            progressLabel = null
        )
    )

    /** 기본 즐겨찾기 목록입니다. 최초 실행 시 이 값으로 DataStore를 초기화합니다. */
    val defaultFavoriteIds: Set<String> = games.filter { it.isFavorite }.map { it.id }.toSet()
}
