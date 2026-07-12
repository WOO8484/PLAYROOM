# 별빛 탐험대 (Starlight Expedition) — Android 네이티브 1차 개발

Jetpack Compose 네이티브 멀티모듈로 구현한 별빛 탐험대 1차 개발 결과물입니다.
`starlight_expedition_gui_v7_4_clean.html`을 디자인·배치·비율 기준으로만 사용했고,
WebView는 사용하지 않았습니다.

## 이 빌드에 대해 꼭 읽어주세요

이 프로젝트는 이 작업 환경(샌드박스)에서 소스 코드까지는 전부 작성했지만,
**실제 `./gradlew assembleDebug` 실행과 APK 생성은 이 환경에서 실패했습니다.**
이유와 재현 방법은 `BUILD_REPORT.md`에 정확히 적어두었습니다. 요약하면:

- 이 작업 환경은 `services.gradle.org`(Gradle 배포본), `dl.google.com` / `maven.google.com`(Android SDK·Jetpack 라이브러리), `repo1.maven.org`(Maven Central)에 대한 외부 네트워크 접근이 차단되어 있습니다.
- 그 결과 Gradle 배포본 자체와 AGP·Compose·AndroxidX 등 의존성을 내려받을 수 없어, 이 환경 안에서는 빌드를 끝까지 진행할 수 없었습니다.
- `gradlew` 스크립트와 `gradle-wrapper.jar`는 정상 동작을 확인했고(버전 확인 명령까지는 성공), 실제로 막히는 지점은 그 다음 단계인 "Gradle 배포본 다운로드"였습니다.
- **인터넷이 정상적으로 열려 있는 개발 PC나 Android Studio에서는 아래 "빌드 방법"대로 정상적으로 빌드되어야 합니다.** 소스 코드 자체는 문법·구조·의존성 선언까지 모두 작성 완료된 상태입니다.

성공한 것처럼 보고하지 않기 위해, 이 사실을 README와 BUILD_REPORT 양쪽에 명확히 남깁니다.

## 실행 방법 (Android Studio)

1. Android Studio(최신 안정 버전 권장)에서 이 폴더(`StarlightExpedition/`)를 **Open**으로 엽니다.
2. Gradle Sync가 끝날 때까지 기다립니다. (인터넷 연결 필요 — Google Maven, Maven Central, Gradle 배포본 다운로드)
3. `app` 실행 구성을 선택하고 에뮬레이터 또는 실기기에서 Run 합니다. (`minSdk 26` 이상)

## 빌드 방법 (커맨드라인)

```bash
cd StarlightExpedition
./gradlew clean
./gradlew :app:assembleDebug
```

디버그 APK는 `app/build/outputs/apk/debug/app-debug.apk`에 생성됩니다.

검증용 명령:

```bash
./gradlew lintDebug
./gradlew testDebugUnitTest
```

## 모듈 구조

```text
StarlightExpedition/
├─ app/                      # Application 진입점, AppContainer, NavHost, 화면 조립
├─ core/
│  ├─ model/                 # Game, GameGenre, Platform, PlayRecord, AppSettings, Recommendation, UiState 등
│  ├─ data/                  # GameRepository·SettingsRepository·FavoritesRepository·RecommendationRepository, DataStore, 샘플 데이터
│  ├─ designsystem/          # 색상·타이포그래피·간격·모서리 토큰, 공통 Composable(버튼, 카드, 하단 메뉴, 커버 프레임 등)
│  ├─ navigation/            # Route, 하단 메뉴 항목, 화면별 설정 버튼 표시 여부
│  └─ common/                # Dispatcher 공급, 시간 포맷, 검색 유틸리티
└─ feature/
   ├─ quickstart/            # 빠른 시작 (이어 하기 / 오늘 뭐 하지?)
   ├─ home/                  # 홈 (요약 카드 3개 + 최근 플레이)
   ├─ favorites/             # 즐겨찾기
   ├─ gamelist/              # 게임리스트 (검색 + 장르 필터)
   └─ settings/              # 설정 Dialog
```

의존성 방향: `feature:*` → `core:*` 만 허용, `feature` 간 직접 참조 없음. Hilt는 사용하지 않고
`app/AppContainer.kt`가 Repository 구현체를 생성해 각 화면 ViewModel에 생성자로 전달합니다.

## 구현 범위 (1차 개발)

- 빠른 시작 / 홈 / 즐겨찾기 / 게임리스트 / 설정, 플로팅 하단 메뉴
- 다크모드 저장(DataStore, 앱 재실행 후 유지, 시스템 다크모드 강제하지 않음)
- 즐겨찾기 등록·해제 및 영구 저장
- 게임 이름 검색 + 장르 필터(전체/RPG/퍼즐/액션/캐주얼) 동시 적용
- 랜덤 추천("오늘 뭐 하지?" 카드의 "랜덤 선택")
- 최근 플레이 표시(홈), 로컬 샘플 게임 6종
- 빠른 시작 대표 이미지 크로스페이드(3.5초 간격, 600ms 전환, 화면 이탈·백그라운드 시 정지)
- 두 커버 프레임(이어 하기 / 오늘 뭐 하지?)의 동일 비율 구현(상단 10.24%, 우측 -13.71%, 높이 74.70%, 모서리 29.84%, -5도 기울기)
- 393dp 기준 레이아웃, 600dp 이상에서 393dp 폭 중앙 고정
- 로딩 / 성공 / 빈 결과 / 오류 상태 구조

## 이번 1차 개발 제외 범위

Libretro 실행, PPSSPP·외부 에뮬레이터 실행, BIOS 검사, 실제 게임 폴더 스캔, 세이브 파일 관리,
치트 적용, PSP HD 텍스처 설치, AI 공략 API, 클라우드 동기화, 외부 서버 연결 — 위 기능은
실행지시서 3절에 따라 이번 범위에서 제외했으며, 관련 버튼("이어서 하기", "바로 시작", "게임 소개")은
화면에는 확정 GUI대로 존재하지만 가짜 성공 메시지 없이 동작을 연결하지 않았습니다.

## 이미지 자산

`starlight_expedition_gui_v7_4_clean.html`에 Data URI로 포함되어 있던 SVG 커버 2장을 PNG로
변환해 `core/designsystem/src/main/res/drawable-nodpi/`에 넣었습니다(`cover_starlight.png`,
`cover_hero_legend.png`). 네트워크 이미지는 사용하지 않습니다.

## GitHub Actions 확인

워크플로 파일은 반드시 저장소 루트 기준 아래 위치에 있어야 합니다.

`.github/workflows/android-build.yml`

GitHub 저장소의 Code 화면에서 `.github` 폴더가 실제로 보이는지 확인하십시오.
보이지 않으면 Actions가 나타나지 않습니다.

Actions 탭에서 `Build Android APK`를 선택한 뒤 `Run workflow`를 누릅니다.
