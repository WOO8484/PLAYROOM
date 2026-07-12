# MODULE STRUCTURE

## 모듈 목록

```text
:app
:build-logic:convention   (포함된 빌드 / included build)
:core:model
:core:designsystem
:core:data
:core:navigation
:feature:onboarding
:feature:home
:feature:library
:feature:favorites
:feature:search
:feature:settings
:feature:player
```

총 12개 모듈 (app 1 + core 4 + feature 7) + build-logic 포함 빌드.

## 각 모듈 역할

| 모듈 | 역할 |
|---|---|
| `:app` | `MainActivity`, 앱 전역 상태(`PlayroomAppViewModel`: 온보딩 여부·앱 스킨), `MainScaffold`(topBar/bottomBar/SnackbarHost 조립), `PlayroomNavHost`(전체 라우팅). 화면 UI 구현 코드는 없음 |
| `:core:model` | `Game`, `GameSystem`, `Difficulty`, `RunStatus`, `RecommendationOptions`, `LibraryFilter`, `AppSkin` 등 순수 Kotlin 데이터 모델·enum. Android UI 의존 없음 |
| `:core:designsystem` | 색상 팔레트(4종 스킨) · 타이포 · spacing/corner 토큰, 공통 Composable(`PlayroomTopBar`, `PlayroomBottomBar`, `PlayroomPrimaryButton`, `PlayroomGameCard`, `PlayroomGameDetailDialog`, `PlayroomDialog`, `PlayroomImageFrame` 등), 카드 아트 이미지 리소스 2장 |
| `:core:data` | `GameRepository`(즐겨찾기 상태 포함 게임 목록 관리), `FakeGameData`(8개 가짜 게임), 검색·필터·추천 순수 함수(`GameQueries.kt`). JVM 유닛 테스트 위치 |
| `:core:navigation` | 라우트 문자열 상수(`PlayroomDestinations`), 하단 탭 정의(`TopLevelDestination`). 화면 UI 없음 |
| `:feature:onboarding` | 첫 실행 환영 화면 |
| `:feature:home` | 메인 화면(이어서 하기 카드, 추천 카드, 4버튼, 상세 팝업, 추천 옵션 팝업) |
| `:feature:library` | 게임룸(전체 목록, 필터, 가짜 폴더 추가 팝업) |
| `:feature:favorites` | 즐겨찾기 목록 |
| `:feature:search` | 검색 오버레이 화면 |
| `:feature:settings` | 설정(게임 관리·플레이 환경·앱 스킨·게임패드 섹션) |
| `:feature:player` | 가짜 게임 실행 화면 + 일시정지 메뉴 |

## 모듈 의존 관계

```text
:app
 ├─ :feature:onboarding, :feature:home, :feature:library,
 │   :feature:favorites, :feature:search, :feature:settings, :feature:player
 ├─ :core:model
 ├─ :core:designsystem
 ├─ :core:data
 └─ :core:navigation

각 :feature:*
 ├─ :core:model
 ├─ :core:designsystem
 ├─ :core:data
 └─ :core:navigation

:core:data
 └─ :core:model

:core:designsystem
 └─ :core:model

:core:navigation
 └─ (없음 — :core:model만 허용되며 현재 버전은 필요하지 않아 의존하지 않음)

:core:model
 └─ (없음 — 순수 Kotlin)
```

의존 방향은 항상 `:app → feature → core`이며 역방향 의존이나 순환 의존이 없습니다.
실제 강제 방법: 각 모듈의 `build.gradle.kts`에 선언된 `dependencies { }` 블록이
이 표와 정확히 일치하며, Gradle 자체가 선언되지 않은 프로젝트 참조를 컴파일
타임에 막아줍니다.

## feature 간 직접 의존이 없는지 확인

- 7개 `:feature:*` 모듈 중 어떤 `build.gradle.kts`에도 `project(":feature:...")`
  형태의 의존성이 없습니다 (`grep -r "project(\":feature" feature/*/build.gradle.kts`
  결과 없음).
- 화면 간 이동은 오직 `:app`의 `PlayroomNavHost`가 각 feature의 Route
  Composable에 콜백 람다(`onNavigateToPlayer`, `onClose` 등)를 주입하는
  방식으로만 이루어집니다. feature가 다른 feature의 ViewModel이나 Composable을
  직접 import하지 않습니다.

## 공통 컴포넌트 위치

`:core:designsystem/component/` 전체:

- `Buttons.kt` — Primary/Secondary/Action 버튼
- `TopBar.kt`, `BottomBar.kt` — 상단바, 하단 탭바
- `GameCard.kt` — 게임룸/즐겨찾기/검색이 공유하는 2열 카드
- `GameDetailDialog.kt` — 게임 상세정보 팝업 (home/library/favorites/search 공유)
- `Dialog.kt` — 모든 팝업의 공통 셸(`PlayroomDialog`)
- `Banner.kt`, `Chip.kt`, `Badge.kt`, `EmptyState.kt`, `ImageFrame.kt` — 배너, 필터 칩, 상태 배지, 빈 상태, 기울어진 아트 프레임
- `Snackbar.kt` — 전역 `SnackbarHostState`를 feature에 전달하는 `CompositionLocal`
- `ResponsiveWidth.kt` — 태블릿 최대 폭 520dp 중앙 정렬 Modifier

## 공통 데이터 흐름

```text
:core:data:GameRepository (StateFlow<List<Game>>, 즐겨찾기 토글)
        ↑ 읽기/쓰기
:feature:*:XxxViewModel (StateFlow<XxxUiState>, 화면별 로컬 상태 결합)
        ↑ collectAsState
:feature:*:XxxScreen (Compose UI)
```

- 게임 목록·즐겨찾기 여부: `GameRepository` 단일 소스. 어떤 화면에서 즐겨찾기를
  토글해도 모든 화면이 같은 `StateFlow`를 구독하므로 즉시 반영됩니다.
- 검색·필터·추천 로직: `:core:data`의 순수 함수(`filterByLibraryFilter`,
  `search`, `matchesRecommendationOptions`, `pickRecommendation`)이며 각
  ViewModel이 이 함수들을 호출합니다.
- 화면별 로컬 상태(팝업 열림 여부, 검색창 텍스트, 설정 토글 등)는 각
  `XxxViewModel`이 `MutableStateFlow`로 직접 관리합니다.
- 앱 전역 상태(온보딩 여부, 앱 스킨)는 `:app`의 `PlayroomAppViewModel`이
  관리하며 필요한 feature(`SettingsRoute`)에는 파라미터로 전달됩니다 — feature가
  `:app`을 참조하지 않는 방향을 유지하기 위함입니다.

## 미래 에뮬레이터 기능 연결 위치

이번 버전에서는 아래 모듈을 만들지 않았으며, 현재 구조는 다음 위치에 삽입될 수
있도록 역할을 분리해 두었습니다.

| 향후 모듈 | 연결 지점 |
|---|---|
| `:rom:scanner` | `:feature:library`의 `ImportGameFolderDialog`가 현재 `LibraryViewModel.startFakeScan()`으로 흉내내는 부분을 실제 스캔 결과로 교체 |
| `:emulation:api` | `:feature:player`의 `PlayerViewModel`이 현재 `GameRepository`에서 `Game`만 읽는 자리에 실제 실행 세션 인터페이스를 주입 |
| `:emulation:libretro` | 위 `:emulation:api`의 구현체 — `:feature:player`는 인터페이스에만 의존하도록 유지 |
| `:save:manager` | `:feature:player`의 "빠른 저장/불러오기" 버튼(`PauseMenuDialog`)이 현재 Snackbar만 띄우는 부분을 실제 저장 호출로 교체 |
| `:controller:gamepad` | `:feature:settings`의 "게임패드 자동 인식/버튼 테스트" 항목과 `:feature:player`의 가상 입력 연결 지점 |
| `:metadata:provider` | `:core:data:FakeGameData`를 대체 — `Game` 모델과 `GameRepository` 인터페이스는 이미 그대로 재사용 가능 |

핵심 원칙: 위 목록의 모듈들은 모두 `:core:data`의 `GameRepository` 인터페이스
또는 각 feature의 콜백 파라미터 뒤에 숨어 있으므로, 실제 구현을 추가할 때
`:feature:*`나 `:core:designsystem`의 UI 코드를 바꿀 필요가 없습니다.
