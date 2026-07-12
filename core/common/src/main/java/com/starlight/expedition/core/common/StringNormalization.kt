package com.starlight.expedition.core.common

/**
 * 검색어와 게임명을 비교하기 전에 공통으로 적용하는 정규화 규칙입니다.
 * 대소문자를 구분하지 않고 앞뒤 공백을 제거합니다.
 */
fun String.normalizedForSearch(): String = trim().lowercase()
