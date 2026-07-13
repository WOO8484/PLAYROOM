package com.starlight.expedition.core.data.scanner

/**
 * 게임 후보 검색에서 제외할 파일 확장자·폴더명 규칙입니다(실행지시서 15절).
 */
object ScanExclusionRules {

    val excludedFileExtensions: Set<String> = setOf(
        "sav", "srm", "state", "st", "auto", "bak", "tmp", "log", "txt", "nfo", "ini", "cfg",
        "xml", "json", "db", "sqlite",
        "cht", "ips", "bps", "ups", "xdelta",
        "png", "jpg", "jpeg", "webp", "gif", "bmp",
        "mp3", "wav", "flac", "ogg",
        "mp4", "mkv", "avi"
    )

    val excludedFolderNames: Set<String> = setOf(
        "saves", "save", "states", "savestates", "cheats", "bios", "system",
        "textures", "texture", "screenshots", "captures", "logs", "cache", "shader", "shaders"
    )

    val coverFolderNames: Set<String> = setOf(
        "cover", "covers", "boxart", "boxarts", "artwork", "images", "thumbnails"
    )

    val imageExtensions: Set<String> = setOf("png", "jpg", "jpeg", "webp")

    fun isExcludedFolderName(name: String): Boolean =
        isHidden(name) || name.lowercase() in excludedFolderNames

    fun isCoverFolderName(name: String): Boolean = name.lowercase() in coverFolderNames

    fun isExcludedFileExtension(extension: String): Boolean = extension.lowercase() in excludedFileExtensions

    fun isImageExtension(extension: String): Boolean = extension.lowercase() in imageExtensions

    fun isHidden(name: String): Boolean = name.startsWith(".")

    /** BIOS 폴더 밖에 있는 BIOS 파일명 패턴을 걸러내기 위한 보조 검사입니다. */
    fun looksLikeBiosFileName(name: String): Boolean = name.lowercase().contains("bios")
}
