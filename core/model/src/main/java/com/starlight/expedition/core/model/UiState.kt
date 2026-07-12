package com.starlight.expedition.core.model

/**
 * 화면 상태를 로딩 / 성공(빈 결과 포함) / 오류로 표현하는 공통 래퍼입니다.
 */
sealed interface UiState<out T> {
    data object Loading : UiState<Nothing>
    data class Success<T>(val data: T) : UiState<T>
    data class Error(val message: String) : UiState<Nothing>
}
