package com.starlight.expedition.core.data.scanner

import com.starlight.expedition.core.model.ClassificationConfidence
import com.starlight.expedition.core.model.Platform
import com.starlight.expedition.core.model.PlatformClassification

/**
 * 확장자와 상위 폴더 힌트로 플랫폼을 판별합니다(실행지시서 13절).
 * 압축 해제, 해시 계산, 파일 내용 읽기를 하지 않는 순수 로직입니다.
 */
object PlatformClassifier {

    /** 확장자만으로 높은 신뢰도 판별이 가능한 표입니다(13.1절). */
    private val highConfidenceExtensions: Map<String, Platform> = mapOf(
        "nes" to Platform.NES,
        "sfc" to Platform.SNES,
        "smc" to Platform.SNES,
        "gb" to Platform.GAME_BOY,
        "gbc" to Platform.GAME_BOY_COLOR,
        "gba" to Platform.GAME_BOY_ADVANCE,
        "nds" to Platform.NINTENDO_DS,
        "3ds" to Platform.NINTENDO_3DS,
        "cci" to Platform.NINTENDO_3DS,
        "n64" to Platform.NINTENDO_64,
        "z64" to Platform.NINTENDO_64,
        "v64" to Platform.NINTENDO_64,
        "cso" to Platform.PSP,
        "gcm" to Platform.GAMECUBE,
        "wbfs" to Platform.WII,
        "wia" to Platform.WII,
        "sms" to Platform.MASTER_SYSTEM,
        "gg" to Platform.GAME_GEAR,
        "md" to Platform.GENESIS,
        "gen" to Platform.GENESIS,
        "gdi" to Platform.DREAMCAST,
        "cdi" to Platform.DREAMCAST,
        "pce" to Platform.PC_ENGINE,
        "ngp" to Platform.NEO_GEO_POCKET,
        "ngc" to Platform.NEO_GEO_POCKET
    )

    private val isoHints = setOf(Platform.PSP, Platform.PLAYSTATION_2, Platform.GAMECUBE, Platform.WII)
    private val chdHints = setOf(
        Platform.PLAYSTATION_1, Platform.PLAYSTATION_2, Platform.DREAMCAST,
        Platform.SEGA_SATURN, Platform.SEGA_CD, Platform.PC_ENGINE, Platform.ARCADE
    )
    private val cueHints = setOf(Platform.PLAYSTATION_1, Platform.SEGA_SATURN, Platform.SEGA_CD, Platform.PC_ENGINE)

    /**
     * @param fileName 검사 대상 파일명(확장자 포함).
     * @param parentFolderNames 루트에서 가까운 순서의 상위 폴더명 목록.
     * @param hasMatchingCueSibling 같은 폴더에 동일 기본 이름의 `.cue`가 있는지 여부(`.bin` 판별에 사용).
     */
    fun classify(
        fileName: String,
        parentFolderNames: List<String>,
        hasMatchingCueSibling: Boolean = false
    ): PlatformClassification {
        val extension = fileName.substringAfterLast('.', "").lowercase()

        highConfidenceExtensions[extension]?.let { platform ->
            return PlatformClassification(
                platform = platform,
                confidence = ClassificationConfidence.HIGH,
                reason = "확장자 .$extension",
                launchable = true
            )
        }

        return when (extension) {
            "iso" -> classifyWithFolderHint(parentFolderNames, isoHints, "확장자 .iso", "모호한 .iso이고 폴더 힌트 없음")
            "chd" -> classifyWithFolderHint(parentFolderNames, chdHints, "확장자 .chd", "모호한 .chd이고 폴더 힌트 없음")
            "cue" -> classifyWithFolderHint(parentFolderNames, cueHints, "확장자 .cue", "모호한 .cue이고 폴더 힌트 없음")
            "pbp" -> classifyPbp(parentFolderNames)
            "bin" -> classifyBin(parentFolderNames, hasMatchingCueSibling)
            "zip", "7z" -> classifyArchive(extension, parentFolderNames)
            "cia" -> PlatformClassification(
                platform = Platform.NINTENDO_3DS,
                confidence = ClassificationConfidence.HIGH,
                reason = "Nintendo 3DS 설치 패키지",
                launchable = false
            )
            else -> PlatformClassification(
                platform = Platform.UNKNOWN,
                confidence = ClassificationConfidence.UNKNOWN,
                reason = if (extension.isEmpty()) "확장자 없음" else "알 수 없는 확장자 .$extension",
                launchable = false
            )
        }
    }

    private fun classifyWithFolderHint(
        parentFolderNames: List<String>,
        allowed: Set<Platform>,
        matchedReasonPrefix: String,
        unmatchedReason: String
    ): PlatformClassification {
        val hint = PlatformRule.findPlatformFromPathHints(parentFolderNames, allowed)
        return if (hint != null) {
            PlatformClassification(
                platform = hint,
                confidence = ClassificationConfidence.MEDIUM,
                reason = "$matchedReasonPrefix + 상위 폴더 힌트",
                launchable = true
            )
        } else {
            PlatformClassification(
                platform = Platform.UNKNOWN,
                confidence = ClassificationConfidence.LOW,
                reason = unmatchedReason,
                launchable = false
            )
        }
    }

    private fun classifyPbp(parentFolderNames: List<String>): PlatformClassification {
        val pspHint = PlatformRule.findPlatformFromPathHints(parentFolderNames, setOf(Platform.PSP))
        if (pspHint != null) {
            return PlatformClassification(Platform.PSP, ClassificationConfidence.HIGH, "PSP 폴더 힌트", launchable = true)
        }
        val ps1Hint = PlatformRule.findPlatformFromPathHints(parentFolderNames, setOf(Platform.PLAYSTATION_1))
        if (ps1Hint != null) {
            return PlatformClassification(
                Platform.PLAYSTATION_1,
                ClassificationConfidence.MEDIUM,
                "PS1/PSX 폴더 힌트",
                launchable = true
            )
        }
        return PlatformClassification(Platform.UNKNOWN, ClassificationConfidence.LOW, "PBP이고 폴더 힌트 없음", launchable = false)
    }

    private fun classifyBin(parentFolderNames: List<String>, hasMatchingCueSibling: Boolean): PlatformClassification {
        if (hasMatchingCueSibling) {
            // 정상 흐름에서는 GameFileGrouping이 이 파일을 companion으로 흡수하므로
            // classify까지 도달하지 않아야 하지만, 방어적으로 같은 판정을 반환합니다.
            return PlatformClassification(
                Platform.UNKNOWN,
                ClassificationConfidence.UNKNOWN,
                ".cue의 동반 파일",
                launchable = false
            )
        }
        val genesisHint = PlatformRule.findPlatformFromPathHints(parentFolderNames, setOf(Platform.GENESIS))
        if (genesisHint != null) {
            return PlatformClassification(Platform.GENESIS, ClassificationConfidence.MEDIUM, "Genesis 폴더 힌트", launchable = true)
        }
        return PlatformClassification(Platform.UNKNOWN, ClassificationConfidence.LOW, "모호한 .bin", launchable = false)
    }

    private fun classifyArchive(extension: String, parentFolderNames: List<String>): PlatformClassification {
        if (PlatformRule.hasArcadeHint(parentFolderNames)) {
            return PlatformClassification(
                Platform.ARCADE,
                ClassificationConfidence.MEDIUM,
                "MAME/아케이드 폴더 힌트",
                launchable = true
            )
        }
        return PlatformClassification(
            Platform.UNKNOWN,
            ClassificationConfidence.UNKNOWN,
            "분류 근거 없는 .$extension (자동 등록 제외)",
            launchable = false
        )
    }

    /** 아케이드 힌트 없는 zip/7z처럼, 판별은 되었지만 게임으로 자동 등록하지 않아야 하는 경우입니다. */
    fun isAutoRegisterExcluded(fileName: String, classification: PlatformClassification): Boolean {
        val extension = fileName.substringAfterLast('.', "").lowercase()
        return extension in setOf("zip", "7z") && classification.platform == Platform.UNKNOWN
    }
}
