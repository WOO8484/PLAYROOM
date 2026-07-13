# 수정 보고서 2

## 최초 오류

1. `HomeScreen.kt`
   - 다른 모듈에 선언된 공개 프로퍼티 `lastPlayedAt`에 대해 스마트 캐스트 불가

2. `QuickStartScreen.kt`
   - `mutableIntStateOf`를 `by` 위임으로 사용할 때 `getValue` / `setValue` import 누락
   - `lastPlayedAt` 스마트 캐스트 불가

## 수정

- `lastPlayedAt`을 지역 변수에 먼저 저장한 뒤 null 검사
- `androidx.compose.runtime.getValue` 추가
- `androidx.compose.runtime.setValue` 추가

## 수정 파일

- `feature/home/.../HomeScreen.kt`
- `feature/quickstart/.../QuickStartScreen.kt`
