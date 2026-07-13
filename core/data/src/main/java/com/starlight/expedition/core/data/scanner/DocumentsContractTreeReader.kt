package com.starlight.expedition.core.data.scanner

import android.content.Context
import android.net.Uri
import android.provider.DocumentsContract
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

/**
 * `DocumentsContract`/`ContentResolver`로 SAF 트리를 반복 순회합니다.
 * 일반 파일 경로나 `File` API를 사용하지 않습니다(실행지시서 16절).
 */
class DocumentsContractTreeReader(private val context: Context) {

    /**
     * [treeUri] 하위 파일들을 큐 기반으로 순회하며 방출합니다.
     * BIOS/저장/캐시 등 제외 폴더 이름은 하위로 내려가지 않습니다.
     * 동일 documentId를 두 번 방문하지 않도록 방문 집합을 둡니다.
     */
    fun walk(treeUri: Uri): Flow<ScannedFileEntry> = flow {
        val resolver = context.contentResolver
        val rootDocumentId = DocumentsContract.getTreeDocumentId(treeUri)
        val visited = mutableSetOf<String>()
        val queue = ArrayDeque<Pair<String, List<String>>>()
        queue.addLast(rootDocumentId to emptyList())

        while (queue.isNotEmpty()) {
            coroutineContext.ensureActive()
            val (documentId, parentNames) = queue.removeFirst()
            if (!visited.add(documentId)) continue

            val childrenUri = DocumentsContract.buildChildDocumentsUriUsingTree(treeUri, documentId)
            val cursor = resolver.query(
                childrenUri,
                arrayOf(
                    DocumentsContract.Document.COLUMN_DOCUMENT_ID,
                    DocumentsContract.Document.COLUMN_DISPLAY_NAME,
                    DocumentsContract.Document.COLUMN_MIME_TYPE,
                    DocumentsContract.Document.COLUMN_SIZE,
                    DocumentsContract.Document.COLUMN_LAST_MODIFIED,
                    DocumentsContract.Document.COLUMN_FLAGS
                ),
                null,
                null,
                null
            ) ?: continue

            cursor.use { rows ->
                val idIndex = rows.getColumnIndex(DocumentsContract.Document.COLUMN_DOCUMENT_ID)
                val nameIndex = rows.getColumnIndex(DocumentsContract.Document.COLUMN_DISPLAY_NAME)
                val mimeIndex = rows.getColumnIndex(DocumentsContract.Document.COLUMN_MIME_TYPE)
                val sizeIndex = rows.getColumnIndex(DocumentsContract.Document.COLUMN_SIZE)
                val modifiedIndex = rows.getColumnIndex(DocumentsContract.Document.COLUMN_LAST_MODIFIED)

                if (idIndex < 0 || nameIndex < 0) return@use

                while (rows.moveToNext()) {
                    coroutineContext.ensureActive()

                    val childId = rows.getString(idIndex) ?: continue
                    val childName = rows.getString(nameIndex) ?: continue
                    val childMime = if (mimeIndex >= 0) rows.getString(mimeIndex) else null
                    val childSize = if (sizeIndex >= 0 && !rows.isNull(sizeIndex)) rows.getLong(sizeIndex) else null
                    val childModified =
                        if (modifiedIndex >= 0 && !rows.isNull(modifiedIndex)) rows.getLong(modifiedIndex) else null

                    val isDirectory = childMime == DocumentsContract.Document.MIME_TYPE_DIR
                    if (isDirectory) {
                        if (!ScanExclusionRules.isExcludedFolderName(childName)) {
                            queue.addLast(childId to (parentNames + childName))
                        }
                    } else {
                        val childUri = DocumentsContract.buildDocumentUriUsingTree(treeUri, childId)
                        emit(
                            ScannedFileEntry(
                                documentId = childId,
                                fileName = childName,
                                parentFolderNames = parentNames,
                                documentUri = childUri.toString(),
                                mimeType = childMime,
                                sizeBytes = childSize,
                                modifiedAtEpochMillis = childModified
                            )
                        )
                    }
                }
            }
        }
    }.flowOn(Dispatchers.IO)

    fun displayNameOf(treeUri: Uri): String {
        val documentId = DocumentsContract.getTreeDocumentId(treeUri)
        val documentUri = DocumentsContract.buildDocumentUriUsingTree(treeUri, documentId)
        val cursor = context.contentResolver.query(
            documentUri,
            arrayOf(DocumentsContract.Document.COLUMN_DISPLAY_NAME),
            null,
            null,
            null
        ) ?: return treeUri.lastPathSegment ?: treeUri.toString()

        cursor.use { rows ->
            val nameIndex = rows.getColumnIndex(DocumentsContract.Document.COLUMN_DISPLAY_NAME)
            if (nameIndex >= 0 && rows.moveToFirst()) {
                rows.getString(nameIndex)?.let { return it }
            }
        }
        return treeUri.lastPathSegment ?: treeUri.toString()
    }
}
