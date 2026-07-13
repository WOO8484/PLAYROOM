# BUILD REPORT

## 기준 프로젝트
- 입력 파일: `StarlightExpedition_GameLibrary_Integrated_v2_0.zip`
- 기준 버전: 패키지 안내 문서 기준 `StarlightExpedition_Fix_2` (GitHub Actions
  `Build Android APK With Full Logs` 성공 커밋 `8802e70`)를 그대로 전달받아 직접 수정함.
  새 프로젝트를 만들지 않았고, 기존 멀티모듈 구조·Gradle Wrapper·AGP/Kotlin/Compose BOM
  버전을 그대로 유지함.

## 구현 완료
- 폴더 선택: `ActivityResultContracts.OpenDocumentTree()`로 시스템 폴더 선택기 연결(`GameFolderDialog.kt`). 일반 파일 경로 문자열 변환 없음.
- 권한 유지: 선택 직후 `contentResolver.takePersistableUriPermission(uri, FLAG_GRANT_READ_URI_PERMISSION)` 호출, 앱 시작·다이얼로그 오픈 시 `persistedUriPermissions`로 재확인(`GameFolderRepositoryImpl.refreshPermissionStates`).
- 재귀 검색: `DocumentsContract` + `ContentResolver.query`로 큐 기반 반복 순회, 방문 documentId 집합으로 무한 루프 방지, `Dispatchers.IO`에서 실행, `ensureActive()`로 취소 대응(`DocumentsContractTreeReader.kt`).
- 플랫폼 분류: 확장자 우선 고신뢰 판별표 + 모호한 확장자(iso/chd/cue/pbp/bin/zip·7z/cia)는 상위 폴더 힌트와 결합해 판별, 근거 없으면 `UNKNOWN`으로 둠(`PlatformClassifier.kt`, `PlatformRule.kt`).
- 파일명 정리: 지역·검증·리비전·디스크 태그만 제거하고 원본 파일명은 `fileName`에 그대로 보존(`GameTitleNormalizer.kt`).
- 묶음 파일 처리: 폴더 단위로 M3U → GDI/트랙 → CUE/BIN 순으로 우선 흡수해 대표 파일 1개 + 동반 파일 목록으로 그룹화(`GameFileGrouping.kt`).
- 로컬 저장: 게임 목록은 `filesDir/starlight/game_library_v1.json`에 임시 파일 → 원자적 교체 방식으로 저장(`GameLibraryFileStore.kt`), 폴더 목록은 기존 DataStore에 JSON 문자열로 저장(`GameFolderPreferences.kt`).
- 커버 매칭: 같은 폴더 → covers/cover/boxart/artwork/images 순으로 위치를 찾고, 정규화된 게임명·ROM 파일명·front 접미사·지역 태그 제거 순으로 이름을 비교(`LocalCoverMatcher.kt`). 커버 이미지 자체는 앱 내부로 복사하지 않고 `content://` URI만 저장.
- 빠른 시작 연결: `lastPlayedAt`이 가장 최근인 실제 게임만 "이어 하기"로 사용, 없으면 빈 상태 문구. 추천은 실제 등록 게임 중에서만 선택하고 게임이 없으면 두 카드 모두 "폴더 추가" 안내를 표시(`QuickStartScreen.kt`, `QuickStartViewModel.kt`).
- 홈 연결: 등록 게임 수·즐겨찾기 수·최근 플레이 3개를 모두 `GameRepository` 실제 데이터로 계산, 오늘 플레이 시간은 세션 측정이 없으므로 항상 0분(`HomeViewModel.kt`).
- 즐겨찾기 연결: `GameRepository.observeGames()`에서 `isFavorite == true`만 필터링해 표시, 게임리스트에서 변경 시 즉시 반영(`FavoritesScreen.kt`).
- 게임리스트 연결: 샘플 목록 제거, 실제 플랫폼 필터(보유 플랫폼만 표시) + 파일명/원제/플랫폼명까지 포함한 검색, 최근 플레이 우선 정렬(`GameListViewModel.kt`, `GameListScreen.kt`).

## 신규 파일

### `feature:library` (신규 모듈)
- `feature/library/build.gradle.kts`
- `feature/library/src/main/java/com/starlight/expedition/feature/library/GameFolderDialog.kt`
- `feature/library/src/main/java/com/starlight/expedition/feature/library/GameFolderManagementRow.kt`
- `feature/library/src/main/java/com/starlight/expedition/feature/library/GameFolderUiState.kt`
- `feature/library/src/main/java/com/starlight/expedition/feature/library/GameFolderViewModel.kt`

### `core:data` — scanner
- `scanner/DocumentsContractTreeReader.kt`
- `scanner/GameDocumentScanner.kt`
- `scanner/GameFileGrouping.kt`
- `scanner/GameCandidateGroup.kt`
- `scanner/ScannedFileEntry.kt`
- `scanner/GameTitleNormalizer.kt`
- `scanner/PlatformClassifier.kt`
- `scanner/PlatformRule.kt`
- `scanner/ScanExclusionRules.kt`
- `scanner/LocalCoverMatcher.kt`
- `scanner/Sha256.kt`

### `core:data` — local / repository / image
- `local/GameLibraryDto.kt`, `local/GameFolderDto.kt`, `local/GameDtoMapper.kt`
- `local/GameLibraryFileStore.kt`, `local/GameFolderPreferences.kt`
- `repository/GameFolderRepository.kt`, `repository/GameFolderRepositoryImpl.kt`
- `repository/GameLibraryMerge.kt`
- `image/LocalCoverImageLoader.kt`

### `core:model`
- `ClassificationConfidence.kt`, `FolderPermissionState.kt`, `GameSourceFolder.kt`
- `PlatformClassification.kt`, `GameScanState.kt`, `GameScanSummary.kt`, `GameCoverMatch.kt`

### `core:designsystem`
- `component/GameCoverImage.kt` (실제 커버 또는 플랫폼 기본 표시)
- `GameFolderIconButton`을 `component/Buttons.kt`에 추가
- `component/PlatformBadge`를 `component/ListComponents.kt`에 추가

### 테스트
- `core/data/src/test/.../scanner/PlatformClassifierTest.kt`
- `core/data/src/test/.../scanner/GameTitleNormalizerTest.kt`
- `core/data/src/test/.../scanner/GameFileGroupingTest.kt`
- `core/data/src/test/.../local/GameLibraryFileStoreTest.kt`
- `core/data/src/test/.../repository/RepositoryTest.kt` (GameLibraryMerge 병합 로직)
- `core/data/src/test/.../repository/GameRepositoryImplTest.kt` (새 생성자 시그니처로 재작성)

## 수정 파일
- `core/model/Game.kt` — 실제 파일 기반 전체 필드로 확장
- `core/model/Platform.kt` — 콘솔별 실제 플랫폼 값으로 확장(`NATIVE/RETRO/PORTABLE`은 Preview·테스트 전용으로 유지)
- `core/model/GameGenre.kt` — `UNKNOWN` 추가
- `core/data/repository/GameRepository.kt` / `GameRepositoryImpl.kt` — `SampleGameData` 의존 제거, `GameLibraryFileStore` 기반으로 재작성
- `core/data/repository/FavoritesRepositoryImpl.kt` — 기본값이 `SampleGameData.defaultFavoriteIds`를 참조하던 부분을 `emptySet()`으로 수정(생산 코드에 남아 있던 샘플 데이터 참조 제거)
- `core/data/repository/RecommendationRepository.kt` / `RecommendationRepositoryImpl.kt` — 실제 게임 목록을 인자로 받고 후보가 없으면 `null` 반환하도록 재작성
- `core/designsystem/component/CoverFrame.kt` — 정적 `Painter` 전용이던 것을 실제 커버(`GameCoverImage`)를 넣을 수 있는 범용 `content` 오버로드로 확장(기하 규칙은 그대로 유지)
- `core/designsystem/component/GameListRow.kt` — 고정 이모지 썸네일 대신 `leading` 슬롯을 선택적으로 받도록 확장(기존 호출부는 영향 없음)
- `feature/quickstart/*` — 3.5초 자동 커버 교대 완전 제거, 실제 게임 데이터·실제 커버·빈 상태 연결
- `feature/home/*` — 실제 등록 게임 수 집계 추가, 샘플 통계 제거
- `feature/favorites/*` — 하드코딩된 상태 문구 맵 제거, 실제 커버·플랫폼 정보로 교체
- `feature/gamelist/*` — 장르 필터를 플랫폼 필터로 교체, 검색 범위 확장, 하드코딩된 태그 맵 제거
- `app/AppContainer.kt` — 폴더 검색 관련 컴포넌트 전부 생성, Application 범위 `CoroutineScope` 추가
- `app/StarlightApp.kt` — 헤더에 폴더 아이콘 추가, `GameFolderDialog` 연결
- `settings.gradle.kts`, `app/build.gradle.kts` — `:feature:library` 추가
- `core/data/build.gradle.kts`, `core/designsystem/build.gradle.kts` — `kotlinx-serialization` 플러그인·의존성, `core:data` 의존성 추가(기존 버전 카탈로그 항목만 사용, 새 라이브러리 추가 없음)
- `scripts/verify_project_structure.py` — `feature:library` 관련 파일·모듈·의존성 검사 추가

## 플랫폼 지원표

| 신뢰도 | 근거 | 플랫폼 |
|---|---|---|
| HIGH | 확장자 단독 | NES, SNES(.sfc/.smc), Game Boy, GBC, GBA, NDS, 3DS(.3ds/.cci), N64(.n64/.z64/.v64), PSP(.cso), GameCube(.gcm), Wii(.wbfs/.wia), Master System, Game Gear, Genesis(.md/.gen), Dreamcast(.gdi/.cdi), PC Engine, Neo Geo Pocket |
| HIGH | 설치 패키지 | 3DS(.cia, launchable=false) |
| MEDIUM | 확장자 + 폴더 힌트 | PSP/PS2/GameCube/Wii(.iso), PS1/PS2/Dreamcast/Saturn/SegaCD/PCEngine/Arcade(.chd), PS1/Saturn/SegaCD/PCEngineCD(.cue), PSP/PS1(.pbp), Genesis(.bin, 폴더 힌트 있을 때만), Arcade(.zip/.7z, MAME/FBNeo/FinalBurn 폴더 힌트) |
| LOW/UNKNOWN | 힌트 없음 | 위 모호한 확장자들에서 폴더 힌트가 없으면 UNKNOWN. .zip/.7z는 아케이드 힌트가 없으면 자동 등록에서 제외. |

## 검색 제외 규칙
- 확장자 제외: sav/srm/state/st/auto/bak/tmp/log/txt/nfo/ini/cfg/xml/json/db/sqlite, cht/ips/bps/ups/xdelta, 이미지(png/jpg/jpeg/webp/gif/bmp), 오디오(mp3/wav/flac/ogg), 동영상(mp4/mkv/avi)
- 폴더 제외(하위로 내려가지 않음): saves/save/states/savestates/cheats/bios/system/textures/texture/screenshots/captures/logs/cache/shader/shaders, 숨김 폴더(.으로 시작)
- 커버 전용 폴더(게임 후보에서는 제외, 커버 검색에서는 사용): cover/covers/boxart/boxarts/artwork/images/thumbnails
- BIOS 폴더 밖 BIOS 파일명 패턴(파일명에 "bios" 포함) 추가 차단

## 저장 구조

```json
{
  "schemaVersion": 1,
  "savedAtEpochMillis": 0,
  "games": [ { "id": "...", "sourceFolderId": "...", "documentUri": "content://...", "titleKo": "...", "platform": "PSP", "platformConfidence": "HIGH", "coverUri": null, "isFavorite": false } ]
}
```
- 위치: 앱 내부 저장소 `filesDir/starlight/game_library_v1.json`
- 폴더 목록: 기존 DataStore(`starlight_preferences`)에 `game_folders_json` 키로 JSON 문자열 저장
- 즐겨찾기: 기존 DataStore `favorite_game_ids` 키(문자열 Set) 그대로 재사용. `GameDto.isFavorite`는 스키마 완전성을 위해 함께 저장하지만, 화면에는 항상 `FavoritesRepository` 값이 우선 적용됨

## 테스트 결과
- 구조 검사: 통과 — `python3 scripts/verify_project_structure.py` 실행 결과 `PROJECT STRUCTURE CHECK PASSED` (Kotlin 파일 101개, 필수 파일 46개, 필수 모듈 12개 확인)
- 단위 테스트: 이 환경에서 실행하지 못함(아래 "빌드하지 못한 항목" 참고). 대신 정적 검토(괄호/중괄호 균형, 미사용 import, 같은 패키지 심볼 해석)를 반복 수행해 명백한 오류는 찾지 못했습니다. 테스트 코드는 작성 완료: PlatformClassifierTest, GameTitleNormalizerTest, GameFileGroupingTest, GameLibraryFileStoreTest, RepositoryTest(GameLibraryMerge), GameRepositoryImplTest.
- Gradle 빌드: 실행 시도했으나 실패 — 아래 "빌드하지 못한 항목" 참고
- GitHub Actions: 이 환경에서는 트리거하지 못했습니다. 저장소에 업로드 후 `Build Android APK With Full Logs` 워크플로 실행이 필요합니다.
- APK: 이번 작업에서는 생성되지 않았습니다.

## 빌드하지 못한 항목
`./gradlew --version`으로 wrapper 동작을 먼저 확인한 뒤, `./gradlew --no-daemon :core:common:test :core:data:test --stacktrace`와 `./gradlew --no-daemon clean :app:assembleDebug --stacktrace` 실행을 시도했습니다.

- 실패 위치: Gradle 배포본 다운로드 단계(아래는 `--version` 실행 시 실제 출력)
```text
Downloading https://services.gradle.org/distributions/gradle-8.10.2-bin.zip
Attempt 1/1 failed. Reason: Server returned HTTP response code: 403 for URL:
https://services.gradle.org/distributions/gradle-8.10.2-bin.zip
```
- 원인: 네트워크 제한. 이 샌드박스는 services.gradle.org, dl.google.com, maven.google.com,
  repo1.maven.org에 대한 외부 접근이 차단되어 있어 Gradle 배포본 자체와 AGP/Compose/AndroidX
  의존성을 받을 수 없습니다. gradlew, gradle-wrapper.jar, gradle-wrapper.properties는
  정상 동작을 확인했고(--version 실행이 배포본 다운로드 단계까지는 정상 진행), 막히는
  지점은 오직 배포본 다운로드입니다.
- 실제 컴파일 미검증 사실: 위 이유로 이번 변경 사항에 대한 kotlinc/AGP 컴파일 성공 여부를
  이 환경에서 직접 확인하지 못했습니다. 정적 검토(전체 파일 미사용 import 검사, 괄호 균형
  검사, 패키지 단위 심볼 해석 검사)는 반복 수행해 문제를 찾지 못했지만, 이는 실제 컴파일을
  대체하지 못합니다. GitHub Actions에서 최종 확인이 반드시 필요합니다.

## 알려진 제한
- 실제 에뮬레이터 실행 미구현(Libretro, PPSSPP, RetroArch, Dolphin 등 연동 없음)
- 인터넷 커버 다운로드 미구현(로컬 파일 매칭만 지원)
- 전체 ROM 해시 미구현(SHA-256은 폴더/문서 ID 문자열에만 사용, 파일 내용 해시 아님)
- 주기적 백그라운드 자동 검색 미구현(수동 재검색만 지원)
- 실제 플레이 시간 측정 미구현(홈의 "오늘 플레이"는 항상 0분)
- 다중 디스크 전환 UI 미구현(대표 파일 1개만 표시, discIndex/discCount/groupKey는 저장)
- 잠재 중복(다른 파일이지만 제목·플랫폼·크기가 같은 경우) 관리 화면 없음

## 남은 오류
- 이 환경에서 실제 Gradle 컴파일을 수행하지 못해, 문법 오류가 전혀 없다고 100% 보장할 수는
  없습니다. GitHub Actions에서 첫 빌드 실행 시 최종 확인이 필요합니다.
- 그 외에는 알려진 오류 없음.
