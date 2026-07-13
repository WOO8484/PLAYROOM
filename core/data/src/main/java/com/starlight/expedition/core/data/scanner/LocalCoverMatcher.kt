package com.starlight.expedition.core.data.scanner

/**
 * 게임명·파일명과 커버 후보 이미지 파일명을 비교해 가장 신뢰도 높은 커버를 찾습니다(실행지시서 23.4절).
 * 위치(같은 폴더 → covers → cover → boxart → artwork → images)를 고르는 일은 스캐너가 담당하고,
 * 이 객체는 이름 비교만 담당합니다.
 */
object LocalCoverMatcher {

    data class CoverCandidate(val fileNameNoExtension: String, val uri: String)

    data class MatchResult(val uri: String, val rank: Int)

    private val frontSuffixPattern = Regex("[-_ ]?\\(?front\\)?$", RegexOption.IGNORE_CASE)

    fun findBestMatch(
        normalizedGameTitle: String,
        romBaseFileName: String,
        candidates: List<CoverCandidate>
    ): MatchResult? {
        if (candidates.isEmpty()) return null

        val normalizedTitle = normalizeForMatch(normalizedGameTitle)
        val normalizedRomBase = normalizeForMatch(romBaseFileName)

        candidates.firstOrNull { normalizeForMatch(it.fileNameNoExtension) == normalizedTitle }
            ?.let { return MatchResult(it.uri, rank = 0) }

        candidates.firstOrNull { normalizeForMatch(it.fileNameNoExtension) == normalizedRomBase }
            ?.let { return MatchResult(it.uri, rank = 1) }

        candidates.firstOrNull {
            val stripped = normalizeForMatch(frontSuffixPattern.replace(it.fileNameNoExtension, ""))
            stripped == normalizedTitle || stripped == normalizedRomBase
        }?.let { return MatchResult(it.uri, rank = 2) }

        candidates.firstOrNull {
            normalizeForMatch(GameTitleNormalizer.normalize(it.fileNameNoExtension)) == normalizedTitle
        }?.let { return MatchResult(it.uri, rank = 3) }

        return null
    }

    private fun normalizeForMatch(value: String): String =
        value.trim().lowercase().filterNot { it == ' ' || it == '_' || it == '-' }
}
