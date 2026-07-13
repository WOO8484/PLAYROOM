package com.starlight.expedition.core.data.local

import java.io.File
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json

/**
 * 게임 라이브러리를 `game_library_v1.json` 파일로 안전하게 저장·읽기 합니다(실행지시서 22.2~22.3절).
 *
 * 저장은 임시 파일에 먼저 쓴 뒤 원본을 교체하는 방식이며, 동시 쓰기는 [Mutex]로 막습니다.
 * [storageDir]를 생성자 인자로 받아, 실제 앱에서는 `filesDir/starlight`를,
 * 단위 테스트에서는 임시 디렉터리를 그대로 넘길 수 있습니다(Android Context 불필요).
 */
class GameLibraryFileStore(private val storageDir: File) {

    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
        prettyPrint = false
    }
    private val mutex = Mutex()

    private val targetFile: File get() = File(storageDir, FILE_NAME)
    private val tempFile: File get() = File(storageDir, "$FILE_NAME.tmp")

    sealed interface ReadResult {
        data class Success(val dto: GameLibraryFileDto) : ReadResult
        data object NotFound : ReadResult
        data class Corrupted(val message: String) : ReadResult
    }

    suspend fun read(): ReadResult = withContext(Dispatchers.IO) {
        mutex.withLock { readLocked() }
    }

    private fun readLocked(): ReadResult {
        val file = targetFile
        if (!file.exists()) return ReadResult.NotFound

        return try {
            val text = file.readText(Charsets.UTF_8)
            val dto = json.decodeFromString(GameLibraryFileDto.serializer(), text)
            ReadResult.Success(dto)
        } catch (e: SerializationException) {
            backupCorruptedFile(file)
            ReadResult.Corrupted(e.message ?: "JSON 파싱 실패")
        } catch (e: IllegalArgumentException) {
            backupCorruptedFile(file)
            ReadResult.Corrupted(e.message ?: "JSON 파싱 실패")
        }
    }

    /**
     * 임시 파일에 먼저 쓰고, 쓰기가 끝난 뒤에만 원본을 교체합니다.
     * 쓰기 도중 실패하면 기존 [targetFile]은 그대로 유지됩니다.
     */
    suspend fun write(dto: GameLibraryFileDto): Result<Unit> = withContext(Dispatchers.IO) {
        mutex.withLock {
            try {
                storageDir.mkdirs()
                val temp = tempFile
                temp.writeText(json.encodeToString(GameLibraryFileDto.serializer(), dto), Charsets.UTF_8)

                val target = targetFile
                val replaced = temp.renameTo(target)
                if (!replaced) {
                    // 같은 파일시스템이 아니거나 rename이 지원되지 않는 드문 경우를 위한 대체 경로입니다.
                    target.writeText(temp.readText(Charsets.UTF_8), Charsets.UTF_8)
                    temp.delete()
                }
                Result.success(Unit)
            } catch (e: Exception) {
                tempFile.delete()
                Result.failure(e)
            }
        }
    }

    private fun backupCorruptedFile(file: File) {
        try {
            val backup = File(storageDir, "game_library_v1.corrupted-${System.currentTimeMillis()}.json")
            file.copyTo(backup, overwrite = true)
        } catch (_: Exception) {
            // 백업조차 실패하면 원본 손상 파일은 건드리지 않고 그대로 둡니다.
        }
    }

    companion object {
        const val FILE_NAME = "game_library_v1.json"
    }
}
