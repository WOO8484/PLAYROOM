package com.starlight.expedition.core.data.scanner

/**
 * 묶음 파일 처리 후 게임 1개로 취급할 대표 파일과 동반 파일 묶음입니다(실행지시서 20절).
 */
data class GameCandidateGroup(
    val representative: ScannedFileEntry,
    val companions: List<ScannedFileEntry>,
    val discIndex: Int?,
    val discCount: Int?,
    val groupKey: String?
)
