# BUILD REPORT — 별빛 탐험대 UI 안정화 (Stabilization v1.0)

## 빌드 결과: 로컬 Gradle 컴파일 미검증 (이 작업 환경 한정 / 네트워크 제한), 코드 수정은 완료

이 문서는 `CLAUDE_작업지시서_v1.0.md`에 따라 실제로 수정한 내용과, 이 샌드박스
환경에서 실제로 실행해 본 명령·결과를 그대로 남깁니다. 빌드를 성공했다고
거짓으로 보고하지 않습니다.

---

## 수정한 파일

- `app/src/main/java/com/starlight/expedition/StarlightApp.kt`
- `core/designsystem/src/main/java/com/starlight/expedition/core/designsystem/theme/Spacing.kt`
- `core/designsystem/src/main/java/com/starlight/expedition/core/designsystem/theme/StarlightColors.kt`
- `core/designsystem/src/main/java/com/starlight/expedition/core/designsystem/component/CoverFrame.kt`
- `core/designsystem/src/main/java/com/starlight/expedition/core/designsystem/component/HomeComponents.kt`
- `core/designsystem/src/main/java/com/starlight/expedition/core/designsystem/component/GameListRow.kt`
- `core/designsystem/src/main/java/com/starlight/expedition/core/designsystem/component/ListComponents.kt`
- `feature/quickstart/src/main/java/com/starlight/expedition/feature/quickstart/QuickStartScreen.kt`
- `feature/home/src/main/java/com/starlight/expedition/feature/home/HomeScreen.kt`
- `feature/favorites/src/main/java/com/starlight/expedition/feature/favorites/FavoritesScreen.kt`
- `feature/gamelist/src/main/java/com/starlight/expedition/feature/gamelist/GameListScreen.kt`

지시서 2절의 수정·삭제·이동 금지 파일(워크플로, gradlew, wrapper jar/properties,
libs.versions.toml, 루트/모듈 `build.gradle.kts`, `AndroidManifest.xml`,
`cover_starlight.png`, `cover_hero_legend.png`)은 위 목록에 없으며, 실제로
건드리지 않았습니다. 모듈 구조(11개 모듈)와 패키지명도 그대로 유지했습니다.

## 페이지 잔상 제거 방법 (3절)

- `StarlightApp.kt`의 `NavHost`에 `enterTransition`, `exitTransition`,
  `popEnterTransition`, `popExitTransition`을 모두 `{ EnterTransition.None }` /
  `{ ExitTransition.None }`으로 명시해 화면 전환 애니메이션을 전부 껐습니다.
  이전 화면과 새 화면이 겹쳐 그려지거나 동시에 페이드되는 구간이 없습니다.
- `NavHost`를 감싸는 `Box(weight(1f))`에 `colors.appBackground`로 불투명 배경을
  추가하고, 빠른 시작·홈·즐겨찾기·게임리스트 화면 각각의 최상위 `Column`에도
  동일한 불투명 배경을 추가했습니다. 화면 전환 순간에도 이전 화면이 비쳐
  보이지 않습니다.
- 하단 메뉴 선택(`saveState`/`restoreState`/`launchSingleTop`) 로직은
  손대지 않고 그대로 유지했습니다.
- 카드 내부 대표 이미지 크로스페이드(빠른 시작 첫 카드)와 페이지 전환
  애니메이션을 분리된 상태로 유지했습니다(카드 내부 `Crossfade`는 그대로,
  페이지 전체 `Crossfade`/`AnimatedContent`는 추가하지 않음).

## 하단 안전 여백 계산 방법 (5절)

기존 코드는 화면마다 `navSafeHeight`(고정 18dp) 또는 여기에 `bottomNavContentExtra`
(24dp)를 더 얹은 `scrollListContentBottomPadding`을 섞어 써서, 화면별로 여백이
서로 다르고 시스템 내비게이션 바 실제 높이를 반영하지 못했습니다.

`Spacing.kt`에 아래 공용 함수 하나를 추가하고, 4개 화면(빠른 시작/홈/즐겨찾기/
게임리스트) 모두 이 함수 하나만 쓰도록 통일했습니다.

```kotlin
@Composable
fun StarlightSpacing.contentBottomSafePadding(): Dp {
    val systemNavigationBarHeight = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
    return bottomNavHeight + bottomNavBottomGap + systemNavigationBarHeight + lg  // lg = 16dp
}
```

지시서 5절의 권장 계산 개념(하단 메뉴 높이 + 메뉴 하단 간격 + 시스템 내비게이션
바 높이 + 콘텐츠-메뉴 사이 16dp)을 그대로 구현했으며, 고정 숫자 대신
`WindowInsets.navigationBars`로 실제 시스템 바 높이를 반영합니다. 기존
`navSafeHeight`/`scrollListContentBottomPadding` 프로퍼티는 다른 곳에서 더 이상
참조하지 않도록 모든 사용처를 교체했습니다(하위 호환을 위해 프로퍼티 자체는
삭제하지 않고 남겨두었습니다).

## 변경한 색상 토큰 (6절, 라이트 모드만)

| 토큰 | 이전 | 변경 |
|---|---|---|
| `appBackground` | `#F4F6FF` | `#F7F7FC` |
| `windowBackground` | `#EEF1FB` | `#F7F7FC` (앱 배경과 동일 → 띠 제거) |
| `textPrimary` | `#171A2A` | `#151724` |
| `textMuted` | `#747B8F` | `#757988` |
| `line`(카드 테두리 공용 토큰) | `#1A292E4C`(반투명) | `#ECEAF3`(불투명) |
| `cardBorder` | `#EBFFFFFF`(반투명) | `#ECEAF3`(불투명) |
| `navBackground` | `#D6FFFFFF`(반투명) | `#FFFFFF`(불투명) |
| `navBorder` | `#EBFFFFFF`(반투명) | `#ECEAF3`(불투명) |
| `surface`(일반 카드) | `#FFFFFF` | 변경 없음(이미 흰색) |
| `primary` / `primaryVariant`(보라 포인트) | 유지 | 유지 |

다크 모드(`DarkStarlightColors`)는 손대지 않았습니다.

추가로, `SummaryCard`(`HomeComponents.kt`), `GameListRow`, `GameSearchField`
(`ListComponents.kt`)가 `colors.surface.copy(alpha = 0.86f~0.88f)`로 카드를
반투명하게 그리던 부분을 `colors.surface`(불투명)로 바꿔, 카드와 하단 메뉴
뒤로 화면 배경이 비치는 문제를 제거했습니다. 임의 색상값을 직접 쓰지 않고
기존 디자인 토큰만 사용했습니다.

빠른 시작 카드(7절)는 카드 배경 자체를 `colors.surface`(흰색)로 바꾸고,
기존 민트·보라 그라데이션(`cardGradientStart/Mid/End`)은 `CoverFrame`에 추가한
`decorationBrush` 파라미터로 옮겨 이미지 프레임의 클립 영역 안에서만 그려지도록
했습니다. 프레임의 기울기·위치·크기 비율, 카드 크기, 버튼 위치는 변경하지
않았습니다.

## 유지한 애니메이션

- 빠른 시작 첫 카드 내부 대표 이미지 크로스페이드(3.5초 간격, 600ms
  `tween`, `Crossfade`) — Lifecycle(`repeatOnLifecycle(STARTED)`)에 연동되어
  화면을 벗어나면 자동 중단되고 다시 들어오면 새로 시작하는 기존 구조를 그대로
  유지했습니다. 600ms는 지시서가 허용하는 "현재 수준 유지" 범위입니다.
- 버튼 눌림 시 0.97배 축소되는 `scaleClickable` 터치 피드백(`ClickEffects.kt`,
  하단 메뉴/버튼 공통) — 변경 없음.
- 하단 메뉴 선택 원형 배경(34dp, 아이콘 영역 내부로 제한)은 즉시 색상만
  바뀌는 방식으로 이미 구현되어 있어 크로스페이드나 잔상이 없습니다. 변경
  없음.

## 제거한 애니메이션

- `NavHost`의 기본 화면 전환 애니메이션(enter/exit/popEnter/popExit)을 전부
  `None`으로 껐습니다. 이전에는 명시적으로 꺼져 있지 않아 Navigation Compose
  기본 전환 효과가 남아 있을 수 있었습니다.
- 페이지 전체에 적용되는 fade/slide/scale/crossfade 효과는 추가하지
  않았으며, 기존에도 없었습니다.

## 로컬 빌드 명령과 결과

이 작업 환경(샌드박스)에서 실행한 명령과 실제 결과입니다.

```bash
$ python3 scripts/verify_project_structure.py
PROJECT STRUCTURE CHECK PASSED
- root: /home/claude/project
- Kotlin files: 64
- required files: 25
- required modules: 11

$ ./gradlew --no-daemon --stacktrace :app:assembleDebug
Fetching distribution.
Downloading https://services.gradle.org/distributions/gradle-8.10.2-bin.zip
Attempt 1/1 failed. Reason: Server returned HTTP response code: 403 for URL:
https://services.gradle.org/distributions/gradle-8.10.2-bin.zip
Exception in thread "main" java.io.IOException: Server returned HTTP response
code: 403 for URL: https://services.gradle.org/distributions/gradle-8.10.2-bin.zip
```

이 환경의 네트워크는 `api.github.com`, `github.com`, `raw.githubusercontent.com`,
`pypi.org`, `npmjs.com`, `archive.ubuntu.com` 등 제한된 목록만 허용하고
`services.gradle.org`(Gradle 배포본), `dl.google.com`/`maven.google.com`
(Android SDK·Jetpack/Compose 의존성), `repo1.maven.org`(Maven Central),
`plugins.gradle.org`(Gradle Plugin Portal)에는 접근할 수 없습니다. 이 네
호스트가 모두 열려야 `:app:assembleDebug`가 끝까지 진행됩니다. 이 환경에는
Android SDK와 `kotlinc`도 설치되어 있지 않아, 컴파일러 수준의 정적 검증조차
로컬에서 수행할 수 없었습니다(이전 `BUILD_REPORT.md`가 남긴 기록과 동일한
제약입니다).

**따라서 이번 수정에 대해 Kotlin 컴파일 성공을 로컬에서 직접 확인하지
못했습니다.** 대신 아래를 수행했습니다.

- 수정한 11개 파일 전체를 다시 `view`로 열어 문법(중괄호/괄호 짝, import
  존재 여부, 함수 시그니처 일치)을 한 줄씩 재검토했습니다.
- 11절의 컴파일 오류 예방 기준(`getValue`/`setValue` import,
  `MutableInteractionSource` 경로, nullable 스마트캐스트 지역 변수화)에 걸리는
  새 코드가 없는지 확인했습니다. 이번 수정에서는 새로운 `by` 위임이나
  다른 모듈의 nullable 프로퍼티 직접 스마트캐스트를 추가하지 않았습니다.
  기존 `getValue`/`setValue` import는 그대로 남아 있습니다.
- 새로 추가한 최상위 함수 `contentBottomSafePadding()`은 `@Composable`
  확장 함수로 선언했고, 호출하는 4개 화면 모두 `@Composable` 컨텍스트 안에서
  호출하도록 배치했습니다.
- `CoverFrame`에 추가한 `decorationBrush: Brush? = null` 파라미터는 기본값이
  있어 기존 호출부와 하위 호환됩니다.

## GitHub Actions 결과

**실행하지 못했습니다.** 이 작업 환경에는 GitHub Actions를 실행할 수 있는
권한이나 실제 저장소 접근이 없습니다(코드 조회를 위한 `api.github.com`/
`github.com` 접근만 허용됨). `.github/workflows/android-build.yml`은 수정하지
않고 그대로 남겨 두었으므로, 이 ZIP을 실제 GitHub 저장소 루트에 반영한 뒤
`Build Android APK With Full Logs` 워크플로를 실행해 최종 성공 여부를
확인해야 합니다.

## APK 생성 경로

`app/build/outputs/apk/debug/app-debug.apk` — 이번 작업에서는 생성되지
**못했습니다.** 위 네트워크 제약 때문입니다. GitHub Actions 또는 인터넷이
열린 개발 환경에서 `./gradlew clean :app:assembleDebug`를 실행하면 이 경로에
생성되어야 합니다.

## 남은 오류

- 실제 Kotlin/AGP 컴파일을 이 환경에서 끝까지 수행하지 못했으므로, 컴파일
  오류가 전혀 없다고 100% 보장할 수는 없습니다. GitHub Actions에서
  `:app:assembleDebug`를 1차로 실행해 확인이 필요합니다.
- 393dp 기준 화면에서의 실제 여백(16dp)·잔상 제거·카드 색상 효과는 코드
  수치·로직으로는 지시서 요구사항대로 구현했지만, 실기기/에뮬레이터
  화면 캡처로 13절의 10회 반복 확인은 이 환경에서 수행하지 못했습니다.
  APK가 만들어진 뒤 실기 확인이 필요합니다.
