package com.starlight.expedition.core.data.local

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore

/**
 * 앱 전체에서 하나의 DataStore 인스턴스를 공유하기 위한 확장 프로퍼티입니다.
 * 다크모드·효과음·자동 저장·즐겨찾기 값을 모두 이 하나의 파일에 저장합니다.
 */
val Context.starlightDataStore by preferencesDataStore(name = "starlight_preferences")
