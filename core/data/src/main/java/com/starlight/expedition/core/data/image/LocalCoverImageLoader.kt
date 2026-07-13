package com.starlight.expedition.core.data.image

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.util.Size
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * 커버 이미지를 Coil·Glide 없이 `ContentResolver`로 직접 불러옵니다(실행지시서 23.6절).
 * 목록 썸네일 크기에 맞게 샘플링하고, 작은 메모리 LRU 캐시를 씁니다.
 */
class LocalCoverImageLoader(private val context: Context) {

    private val cache = LruBitmapCache(maxEntries = 60)

    suspend fun loadThumbnail(uriString: String, targetSizePx: Int): Bitmap? = withContext(Dispatchers.IO) {
        val cacheKey = "$uriString@$targetSizePx"
        cache.get(cacheKey)?.let { return@withContext it }

        val uri = runCatching { Uri.parse(uriString) }.getOrNull() ?: return@withContext null

        val bitmap = runCatching {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                context.contentResolver.loadThumbnail(uri, Size(targetSizePx, targetSizePx), null)
            } else {
                decodeSampledFallback(uri, targetSizePx)
            }
        }.getOrNull()

        if (bitmap != null) {
            cache.put(cacheKey, bitmap)
        }
        bitmap
    }

    private fun decodeSampledFallback(uri: Uri, targetSizePx: Int): Bitmap? {
        val resolver = context.contentResolver

        val boundsOptions = BitmapFactory.Options().apply { inJustDecodeBounds = true }
        resolver.openInputStream(uri)?.use { stream ->
            BitmapFactory.decodeStream(stream, null, boundsOptions)
        } ?: return null

        val sampleSize = calculateSampleSize(boundsOptions.outWidth, boundsOptions.outHeight, targetSizePx)
        val decodeOptions = BitmapFactory.Options().apply { inSampleSize = sampleSize }

        return resolver.openInputStream(uri)?.use { stream ->
            BitmapFactory.decodeStream(stream, null, decodeOptions)
        }
    }

    private fun calculateSampleSize(width: Int, height: Int, targetSizePx: Int): Int {
        if (width <= 0 || height <= 0 || targetSizePx <= 0) return 1
        var sampleSize = 1
        var currentWidth = width
        var currentHeight = height
        while (currentWidth / 2 >= targetSizePx && currentHeight / 2 >= targetSizePx) {
            currentWidth /= 2
            currentHeight /= 2
            sampleSize *= 2
        }
        return sampleSize
    }
}

/**
 * 외부 캐시 라이브러리를 추가하지 않기 위해 직접 만든, 아주 작은 스레드 안전 LRU 캐시입니다.
 */
private class LruBitmapCache(maxEntries: Int) {
    private val lock = Any()
    private val map = object : LinkedHashMap<String, Bitmap>(maxEntries, 0.75f, true) {
        override fun removeEldestEntry(eldest: MutableMap.MutableEntry<String, Bitmap>): Boolean = size > maxEntries
    }

    fun get(key: String): Bitmap? = synchronized(lock) { map[key] }

    fun put(key: String, bitmap: Bitmap) {
        synchronized(lock) { map[key] = bitmap }
    }
}
