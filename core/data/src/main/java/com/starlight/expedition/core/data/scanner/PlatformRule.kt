package com.starlight.expedition.core.data.scanner

import com.starlight.expedition.core.model.Platform

/**
 * 폴더명 힌트를 정규화하고 플랫폼으로 매핑하는 표입니다.
 * 대소문자, 공백, `_`, `-`, `.` 차이를 무시하고 한글·영문 별칭을 함께 지원합니다.
 * 여러 코드에 문자열을 흩어놓지 않도록 이 파일 하나에서만 관리합니다.
 */
object PlatformRule {

    private val canonicalHintPlatform: Map<String, Platform> = mapOf(
        "psp" to Platform.PSP,
        "ps1" to Platform.PLAYSTATION_1,
        "ps2" to Platform.PLAYSTATION_2,
        "gamecube" to Platform.GAMECUBE,
        "wii" to Platform.WII,
        "dreamcast" to Platform.DREAMCAST,
        "saturn" to Platform.SEGA_SATURN,
        "segacd" to Platform.SEGA_CD,
        "pcengine" to Platform.PC_ENGINE,
        "arcade" to Platform.ARCADE
    )

    /** 정규화된(공백/구분자 제거, 소문자) 별칭 → 표준 힌트 id */
    private val aliasToCanonical: Map<String, String> = mapOf(
        "psp" to "psp",
        "playstationportable" to "psp",
        "플레이스테이션포터블" to "psp",
        "psproms" to "psp",

        "ps1" to "ps1",
        "psx" to "ps1",
        "playstation" to "ps1",
        "플레이스테이션" to "ps1",

        "ps2" to "ps2",
        "playstation2" to "ps2",
        "플레이스테이션2" to "ps2",

        "gamecube" to "gamecube",
        "gc" to "gamecube",
        "게임큐브" to "gamecube",

        "wii" to "wii",
        "위" to "wii",

        "dreamcast" to "dreamcast",
        "dc" to "dreamcast",
        "드림캐스트" to "dreamcast",

        "saturn" to "saturn",
        "segasaturn" to "saturn",
        "새턴" to "saturn",

        "segacd" to "segacd",
        "megacd" to "segacd",

        "pcengine" to "pcengine",
        "turbografx" to "pcengine",
        "pcenginecd" to "pcengine",

        "mame" to "arcade",
        "fbneo" to "arcade",
        "finalburn" to "arcade",
        "arcade" to "arcade",
        "아케이드" to "arcade"
    )

    /** 대소문자, 공백, `_`, `-`, `.` 차이를 무시하는 정규화입니다. */
    fun normalize(raw: String): String =
        raw.trim().lowercase().filterNot { it == ' ' || it == '_' || it == '-' || it == '.' }

    fun platformForFolderName(rawFolderName: String): Platform? {
        val canonical = aliasToCanonical[normalize(rawFolderName)] ?: return null
        return canonicalHintPlatform[canonical]
    }

    /**
     * 가장 가까운(하위) 폴더부터 확인해 [allowed]에 속하는 첫 힌트를 반환합니다.
     * [parentFolderNames]는 루트에서 가까운 순서(예: ["ROMs", "PSP"])로 전달합니다.
     */
    fun findPlatformFromPathHints(parentFolderNames: List<String>, allowed: Set<Platform>? = null): Platform? {
        for (folder in parentFolderNames.asReversed()) {
            val platform = platformForFolderName(folder) ?: continue
            if (allowed == null || platform in allowed) {
                return platform
            }
        }
        return null
    }

    fun hasArcadeHint(parentFolderNames: List<String>): Boolean =
        findPlatformFromPathHints(parentFolderNames, allowed = setOf(Platform.ARCADE)) != null
}
