# 별빛 탐험대 (Starlight Expedition) — 게임 폴더 자동 분류 · 게임 라이브러리 v2.0

Jetpack Compose 네이티브 멀티모듈 Android 앱입니다. 사용자가 상단 우측 폴더 아이콘으로
게임 폴더를 지정하면, 앱이 SAF(Storage Access Framework)로 하위 파일을 검색하고 플랫폼을
자동 판별해 실제 게임 라이브러리로 저장합니다. 빠른 시작·홈·즐겨찾기·게임리스트는 모두 이
실제 라이브러리 데이터를 함께 사용합니다.

## 이 빌드에 대해 꼭 읽어주세요

이 프로젝트는 Claude 작업 환경(샌드박스)에서 소스 코드까지는 전부 작성·검토했지만,
**실제 `./gradlew` 빌드는 이 환경에서 끝까지 실행하지 못했습니다.** 이유와 재현 과정은
`BUILD_REPORT.md`에 정확히 기록되어 있습니다. 요약하면:

- 이 샌드박스는 `services.gradle.org`(Gradle 배포본), `dl.google.com` / `maven.google.com`
  (Android SDK·Jetpack·Compose 라이브러리), `repo1.maven.org`(Maven Central)에 대한 외부
  네트워크 접근이 차단되어 있습니다.
- `gradlew` 스크립트와 `gradle-wrapper.jar`는 정상 동작을 확인했습니다(`./gradlew --version`
  실행까지는 성공). 실제로 막히는 지점은 그다음 단계인 "Gradle 배포본 다운로드"입니다.
- 이 리포지토리에는 실제 GitHub Actions에서 성공했던 이전 빌드(`8802e70` 커밋, 워크플로
  `Build Android APK With Full Logs`)의 인프라(`.github/workflows`, Gradle Wrapper 등)를
  그대로 유지했습니다. 이번 변경 사항은 그 위에 직접 적용한 것이며, **이번 변경 이후의
  빌드 성공 여부는 GitHub Actions에서 다시 확인해야 합니다.**

성공한 것처럼 보고하지 않기 위해 이 사실을 README와 BUILD_REPORT 양쪽에 남깁니다.

## GitHub Actions로 빌드 확인하기 (권장)

1. 이 ZIP의 내용을 저장소 루트에 그대로 덮어씁니다(폴더를 한 겹 더 감싸지 않습니다).
2. GitHub Actions에서 `Build Android APK With Full Logs` 워크플로를 실행합니다.
3. 성공하면 Artifacts에서 `StarlightExpedition-debug-apk`를 내려받습니다.
4. 실패하면 `StarlightExpedition-full-build-logs` 아티팩트의 `error-summary.log`를 먼저 확인합니다.

## 로컬 빌드 방법 (인터넷이 열려 있는 환경)

```bash
python3 scripts/verify_project_structure.py
./gradlew clean
./gradlew :core:common:test :core:data:test
./gradlew :app:assembleDebug
```

디버그 APK는 `app/build/outputs/apk/debug/app-debug.apk`에 생성됩니다.

## Android Studio로 열기

1. Android Studio(최신 안정 버전 권장)에서 프로젝트 루트를 **Open**으로 엽니다.
2. Gradle Sync가 끝날 때까지 기다립니다(인터넷 연결 필요).
3. `app` 실행 구성을 에뮬레이터 또는 실기기(`minSdk 26` 이상)에서 Run 합니다.

## 모듈 구조

```text
app/                          # Application 진입점, AppContainer, NavHost, 화면 조립
core/
  model/                      # Game, Platform, GameGenre, GameSourceFolder, GameScanState 등 순수 모델
  data/
    scanner/                  # DocumentsContract 재귀 검색, 플랫폼 분류, 파일명 정리, 묶음 파일 처리, 커버 매칭
    local/                    # game_library_v1.json 저장소, 폴더 목록 DataStore, DTO
    repository/               # GameRepository, GameFolderRepository, FavoritesRepository, SettingsRepository, RecommendationRepository
    image/                    # ContentResolver 기반 로컬 커버 이미지 로더(Coil/Glide 미사용)
  designsystem/                # 색상·타이포그래피·간격 토큰, 공통 Composable(카드, 하단 메뉴, 커버 프레임, 게임 커버 이미지 등)
  navigation/                  # Route, 하단 메뉴 항목, 화면별 설정 버튼 표시 여부
  common/                      # Dispatcher 공급, 시간 포맷, 검색 유틸리티
feature/
  quickstart/                  # 빠른 시작 (이어 하기 / 오늘 뭐 하지?) — 실제 게임 데이터 연결
  home/                        # 홈 (요약 카드 3개 + 최근 플레이) — 실제 게임 데이터 연결
  favorites/                   # 즐겨찾기 — 실제 게임 데이터 연결
  gamelist/                    # 게임리스트 (검색 + 플랫폼 필터) — 실제 게임 데이터 연결
  settings/                    # 설정 Dialog
  library/                     # 신규: 게임 폴더 관리 다이얼로그, 상태, ViewModel
```

의존성 방향: `feature:*` → `core:*` 만 허용, `feature` 간 직접 참조 없음. `feature:library`는
`core:model`·`core:data`·`core:designsystem`·`core:common`에만 의존하며, 파일 검색·저장
로직은 담지 않고 다이얼로그·상태·ViewModel만 담당합니다. Hilt는 사용하지 않고
`app/AppContainer.kt`가 Repository 구현체를 한 번만 생성해 각 화면 ViewModel에 생성자로
전달합니다.

## v2.0에서 새로 구현한 것

- 상단 우측 게임 폴더 관리 아이콘(모든 주요 탭에서 접근 가능, 빠른 시작에서도 표시)
- `ActivityResultContracts.OpenDocumentTree()` + `takePersistableUriPermission` 기반 폴더 선택과 읽기 권한 영구 유지
- 여러 폴더 등록·삭제·전체/개별 재검색, `DocumentsContract` 기반 재귀 검색(일반 파일 경로 미사용)
- 확장자 + 상위 폴더 힌트 기반 플랫폼 자동 판별과 신뢰도·판별 근거 저장(`PlatformClassifier`)
- CUE/BIN, GDI/트랙, M3U/다중 디스크 묶음 파일 중복 방지(`GameFileGrouping`)
- 파일명 정리 및 표시용 게임명 생성, 원본 파일명 보존(`GameTitleNormalizer`)
- 로컬 커버 자동 매칭(같은 폴더 → covers/cover/boxart/artwork/images), 실패 시 플랫폼 기본 표시
- `filesDir/starlight/game_library_v1.json` 원자적 저장(임시 파일 → 교체, 손상 시 백업, 동시 쓰기 Mutex)
- 재검색 시 추가·수정·삭제 동기화, 실패한 검색에서는 기존 목록 보존, 즐겨찾기·플레이 기록 유지
- 빠른 시작·홈·즐겨찾기·게임리스트가 모두 같은 `GameRepository` 데이터를 사용
- 빠른 시작의 3.5초 타이머 자동 커버 교대 완전 제거(선택된 게임이 바뀔 때만 커버가 바뀜)
- 게임리스트 플랫폼 필터(보유 플랫폼만 표시), 파일명·원제·플랫폼명까지 포함하는 검색
- `feature:library` 신규 모듈, 폴더 검색 상태(`GameScanState`)와 진행 요약 UI
- Application 범위 CoroutineScope로 검색을 실행해 다이얼로그를 닫거나 탭을 이동해도 검색 계속

## 이번 업데이트에서 제외한 것 (구조만 고려, 미구현)

인터넷 커버 다운로드, 인터넷 메타데이터 조회, AI 게임명 판별, 전체 ROM 해시, ZIP/7Z 압축 해제,
BIOS 자동 설치, 에뮬레이터 코어 설치, PPSSPP·RetroArch·Dolphin 등 실행 연동, 세이브·치트·텍스처
관리, 클라우드 동기화, 주기적 백그라운드 자동 검색, 실제 플레이 시간 측정. 실제 실행 연동이
없는 상태이므로 플레이 횟수·최근 실행 시간을 가짜로 증가시키지 않습니다.

## 이미지 자산

`cover_starlight.png`, `cover_hero_legend.png`는 Compose Preview·단위 테스트·데모 데이터에서만
사용합니다. 실제 게임 목록에서는 검색으로 찾은 실제 커버(`coverUri`) 또는 플랫폼 기본 표시를
사용하며, 다른 게임의 정적 커버를 대신 보여주지 않습니다.
