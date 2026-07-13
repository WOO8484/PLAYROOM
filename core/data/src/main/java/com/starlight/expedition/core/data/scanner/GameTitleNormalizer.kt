package com.starlight.expedition.core.data.scanner

/**
 * 파일명에서 표시용 게임명을 만듭니다(실행지시서 19절).
 * 원본 파일명은 호출하는 쪽에서 별도로 보존해야 하며, 이 함수는 표시용 문자열만 반환합니다.
 */
object GameTitleNormalizer {

    private val regionRevVersionPattern = Regex(
        "\\((?:USA|Europe|Japan|Korea|World|Rev\\s?[0-9A-Za-z]+|v[0-9.]+)\\)",
        RegexOption.IGNORE_CASE
    )
    private val bracketTagPattern = Regex("\\[(?:!|b|T\\+Kor)\\]", RegexOption.IGNORE_CASE)
    private val discTagPattern = Regex("\\(?\\b(?:Disc|Disk)\\s?\\d+\\b\\)?", RegexOption.IGNORE_CASE)
    private val cdTagPattern = Regex("\\(?\\bCD\\d+\\b\\)?", RegexOption.IGNORE_CASE)
    private val sideTagPattern = Regex("\\(?\\bSide\\s?[A-Z]\\b\\)?", RegexOption.IGNORE_CASE)
    private val emptyBracketsPattern = Regex("[\\[(]\\s*[])]")
    private val multiSpacePattern = Regex("\\s{2,}")

    /**
     * [fileName]은 확장자를 포함해도, 포함하지 않아도 됩니다.
     */
    fun normalize(fileName: String): String {
        val withoutExtension = fileName.substringBeforeLast('.', fileName)

        var result = withoutExtension
        result = regionRevVersionPattern.replace(result, "")
        result = bracketTagPattern.replace(result, "")
        result = discTagPattern.replace(result, "")
        result = cdTagPattern.replace(result, "")
        result = sideTagPattern.replace(result, "")
        result = emptyBracketsPattern.replace(result, "")
        result = multiSpacePattern.replace(result, " ").trim()
        result = result.trim('-', '_', ' ')

        return result.ifBlank { withoutExtension.trim() }
    }
}
