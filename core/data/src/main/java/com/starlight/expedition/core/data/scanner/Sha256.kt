package com.starlight.expedition.core.data.scanner

import java.security.MessageDigest

/**
 * 폴더 ID와 게임 ID에 사용하는 안정적인 해시입니다.
 * 파일 내용을 읽는 해시가 아니라 문자열(경로/URI) 해시입니다.
 */
object Sha256 {
    fun of(value: String): String {
        val digest = MessageDigest.getInstance("SHA-256").digest(value.toByteArray(Charsets.UTF_8))
        return digest.joinToString(separator = "") { byte -> "%02x".format(byte) }
    }
}
