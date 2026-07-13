package com.starlight.expedition.core.data.scanner

/**
 * DocumentsContract 재귀 검색이 만들어내는 파일 후보 1건입니다.
 * Android 타입을 담지 않는 순수 데이터라서 단위 테스트에서 그대로 만들어 쓸 수 있습니다.
 *
 * @param parentFolderNames 루트에서 가까운 순서의 상위 폴더명 목록 (예: ["ROMs", "PSP"]).
 */
data class ScannedFileEntry(
    val documentId: String,
    val fileName: String,
    val parentFolderNames: List<String>,
    val documentUri: String,
    val mimeType: String?,
    val sizeBytes: Long?,
    val modifiedAtEpochMillis: Long?
) {
    val extensionLower: String get() = fileName.substringAfterLast('.', "").lowercase()
    val baseNameLower: String get() = fileName.substringBeforeLast('.', fileName).lowercase()
    val folderKey: String get() = parentFolderNames.joinToString("/")
}
