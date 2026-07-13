package com.starlight.expedition.core.model

/**
 * 폴더 검색의 현재 상태입니다. 다이얼로그를 닫거나 탭을 이동해도
 * 이 상태를 소유하는 Repository가 Application 범위이므로 검색은 계속됩니다.
 */
sealed interface GameScanState {
    data object Idle : GameScanState

    data class Scanning(
        val folderId: String,
        val folderName: String,
        val visitedCount: Int,
        val candidateCount: Int,
        val currentName: String?
    ) : GameScanState

    data class Completed(
        val summary: GameScanSummary
    ) : GameScanState

    data class Failed(
        val folderId: String?,
        val message: String,
        val recoverable: Boolean
    ) : GameScanState

    data object Cancelled : GameScanState

    /** 앱 프로세스가 검색 도중 종료되어 이전 실행에서 중단된 상태로 다음 실행을 시작합니다. */
    data class Interrupted(
        val folderId: String?
    ) : GameScanState
}
