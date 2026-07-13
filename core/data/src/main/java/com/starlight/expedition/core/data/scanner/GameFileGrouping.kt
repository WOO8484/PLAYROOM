package com.starlight.expedition.core.data.scanner

/**
 * 같은 폴더 안의 CUE/BIN, GDI/트랙, M3U/다중 디스크 파일을 게임 1개 단위로 묶습니다(실행지시서 20절).
 * 폴더 밖을 가리키는 참조나 다른 폴더의 동일 이름 파일은 서로 묶지 않습니다.
 */
object GameFileGrouping {

    private val discCompanionExtensions = setOf("cue", "bin", "gdi", "iso", "raw")

    fun group(entries: List<ScannedFileEntry>): List<GameCandidateGroup> {
        val result = mutableListOf<GameCandidateGroup>()

        val byFolder = entries.groupBy { it.folderKey }
        for (folderEntries in byFolder.values) {
            val consumed = mutableSetOf<String>()

            // 1) M3U가 있으면 같은 폴더의 디스크 관련 파일을 전부 동반 파일로 흡수합니다.
            val m3uFiles = folderEntries.filter { it.extensionLower == "m3u" }
            val primaryM3u = m3uFiles.firstOrNull()
            if (primaryM3u != null) {
                val companions = folderEntries.filter {
                    it.documentId != primaryM3u.documentId && it.extensionLower in discCompanionExtensions
                }
                result += GameCandidateGroup(
                    representative = primaryM3u,
                    companions = companions,
                    discIndex = null,
                    discCount = companions.size.takeIf { it > 0 },
                    groupKey = primaryM3u.folderKey
                )
                consumed += primaryM3u.documentId
                consumed += companions.map { it.documentId }
            }

            // 2) GDI: 같은 폴더의 bin/raw 트랙 파일을 흡수합니다.
            val gdiFiles = folderEntries.filter { it.extensionLower == "gdi" && it.documentId !in consumed }
            for (gdi in gdiFiles) {
                val companions = folderEntries.filter {
                    it.documentId != gdi.documentId && it.documentId !in consumed &&
                        (it.extensionLower == "bin" || it.extensionLower == "raw")
                }
                result += GameCandidateGroup(
                    representative = gdi,
                    companions = companions,
                    discIndex = extractDiscIndex(gdi.fileName),
                    discCount = null,
                    groupKey = gdi.folderKey
                )
                consumed += gdi.documentId
                consumed += companions.map { it.documentId }
            }

            // 3) CUE/BIN: 기본 이름이 완전히 같은 bin만 동반 파일로 묶습니다.
            val cueFiles = folderEntries.filter { it.extensionLower == "cue" && it.documentId !in consumed }
            for (cue in cueFiles) {
                val companions = folderEntries.filter {
                    it.documentId != cue.documentId && it.documentId !in consumed &&
                        it.extensionLower == "bin" && it.baseNameLower == cue.baseNameLower
                }
                result += GameCandidateGroup(
                    representative = cue,
                    companions = companions,
                    discIndex = extractDiscIndex(cue.fileName),
                    discCount = null,
                    groupKey = cue.baseNameLower
                )
                consumed += cue.documentId
                consumed += companions.map { it.documentId }
            }

            // 4) 나머지는 단독 게임 후보입니다. (남은 m3u가 더 있다면 단독 후보로 처리합니다.)
            for (entry in folderEntries) {
                if (entry.documentId in consumed) continue
                result += GameCandidateGroup(
                    representative = entry,
                    companions = emptyList(),
                    discIndex = extractDiscIndex(entry.fileName),
                    discCount = null,
                    groupKey = null
                )
                consumed += entry.documentId
            }
        }

        return result
    }

    private val discIndexPattern = Regex("\\b(?:Disc|Disk|CD)\\s?(\\d+)\\b", RegexOption.IGNORE_CASE)

    private fun extractDiscIndex(fileName: String): Int? =
        discIndexPattern.find(fileName)?.groupValues?.get(1)?.toIntOrNull()
}
