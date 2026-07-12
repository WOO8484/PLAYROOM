package com.starlight.expedition.core.common

/**
 * 게임명 검색에 공통으로 사용하는 매칭 규칙입니다.
 * 한글을 포함한 일반 텍스트 포함 검색을 수행합니다.
 */
object SearchUtils {

    fun matches(title: String, query: String): Boolean {
        val normalizedQuery = query.normalizedForSearch()
        if (normalizedQuery.isEmpty()) {
            return true
        }
        return title.normalizedForSearch().contains(normalizedQuery)
    }
}
