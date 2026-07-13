# 별빛 탐험대 Android 화면 안정화 통합 작업 실행지시서 v1.0

## 1. 작업 기준

이 압축파일의 루트는 현재 GitHub Actions에서 `:app:assembleDebug` 빌드가 성공한 전체 Android 프로젝트입니다.

새 프로젝트를 만들지 말고, 이 프로젝트를 그대로 기준으로 수정합니다.  
기존 멀티모듈 구조, 패키지명, Gradle 버전, Kotlin 버전, Compose 구조를 유지합니다.

현재 기준 주요 구조:

- `app`
- `core:common`
- `core:data`
- `core:designsystem`
- `core:model`
- `core:navigation`
- `feature:quickstart`
- `feature:home`
- `feature:favorites`
- `feature:gamelist`
- `feature:settings`

WebView로 전환하지 않습니다.  
기능 삭제나 임시 주석 처리로 빌드만 통과시키지 않습니다.  
확정된 393dp 화면 비율과 카드 구조를 임의로 재설계하지 않습니다.

참고 실기 화면:

- `reference/현재화면_빠른시작.jpg`
- `reference/현재화면_홈.jpg`

## 2. 필수 선행 확인

수정 전 프로젝트 루트에서 아래를 실행합니다.

```bash
python3 scripts/verify_project_structure.py
./gradlew --no-daemon --stacktrace :app:assembleDebug
```

수정 전 빌드가 실패하면 작업을 시작하지 말고, 최초 오류를 먼저 바로잡습니다.

다음 파일은 삭제·누락·이동 금지입니다.

- `.github/workflows/android-build.yml`
- `gradlew`
- `gradlew.bat`
- `gradle/wrapper/gradle-wrapper.jar`
- `gradle/wrapper/gradle-wrapper.properties`
- `gradle/libs.versions.toml`
- `settings.gradle.kts`
- 루트 `build.gradle.kts`
- 모든 모듈의 `build.gradle.kts`
- `app/src/main/AndroidManifest.xml`
- `core/designsystem/src/main/res/drawable-nodpi/cover_starlight.png`
- `core/designsystem/src/main/res/drawable-nodpi/cover_hero_legend.png`

## 3. 페이지 이동 잔상 제거

하단 메뉴로 페이지를 이동할 때 이전 화면이 겹쳐 보이거나 잔상이 남지 않도록 수정합니다.

대상 중심 파일:

- `app/src/main/java/com/starlight/expedition/StarlightApp.kt`

요구사항:

1. Navigation Compose 화면 전환 애니메이션을 명시적으로 전부 끕니다.
2. `enterTransition`, `exitTransition`, `popEnterTransition`, `popExitTransition`은 모두 `None`으로 처리합니다.
3. 이전 화면과 새 화면을 동시에 페이드하거나 겹쳐 그리지 않습니다.
4. 하단 메뉴 선택 즉시 새 페이지로 교체합니다.
5. `saveState`, `restoreState`, `launchSingleTop` 기반의 현재 탭 상태 보존은 유지합니다.
6. NavHost 영역과 각 화면 최상위에는 완전히 불투명한 배경을 적용합니다.
7. 빠른 시작 → 홈 → 즐겨찾기 → 게임리스트를 반복 이동해도 이전 페이지가 보이지 않아야 합니다.
8. 페이지 전체에 적용되는 fade, slide, scale, crossfade 효과는 추가하지 않습니다.

## 4. 카드 내부 애니메이션 정리

대표 이미지 두 장의 자동 교대는 빠른 시작 첫 카드 내부에서만 유지합니다.

대상 중심 파일:

- `feature/quickstart/src/main/java/com/starlight/expedition/feature/quickstart/QuickStartScreen.kt`
- `core/designsystem/src/main/java/com/starlight/expedition/core/designsystem/component/CoverFrame.kt`

요구사항:

1. `cover_starlight.png`와 `cover_hero_legend.png`는 3~4초 간격으로 교대합니다.
2. 전환은 확대·이동 없이 크로스페이드만 사용합니다.
3. 현재 600ms 수준을 유지하거나 실기에서 잔상이 없도록 350~500ms 범위에서 조정할 수 있습니다.
4. 빠른 시작 화면을 벗어나면 교대 코루틴과 애니메이션이 즉시 중단되어야 합니다.
5. 다시 빠른 시작 화면에 들어오면 정상적으로 새로 시작해야 합니다.
6. 카드 밖이나 다른 페이지에 이전 이미지 레이어가 남지 않아야 합니다.
7. 페이지 이동 애니메이션과 카드 내부 이미지 애니메이션을 혼합하지 않습니다.
8. 버튼 눌림 효과는 짧은 터치 피드백만 유지합니다.

## 5. 하단 메뉴와 콘텐츠 간격 수정

현재 홈 화면의 마지막 카드와 하단 메뉴가 지나치게 붙어 보이는 문제를 수정합니다.

대상 중심 파일:

- `core/designsystem/src/main/java/com/starlight/expedition/core/designsystem/theme/Spacing.kt`
- `app/src/main/java/com/starlight/expedition/StarlightApp.kt`
- `feature/quickstart/src/main/java/com/starlight/expedition/feature/quickstart/QuickStartScreen.kt`
- `feature/home/src/main/java/com/starlight/expedition/feature/home/HomeScreen.kt`
- `feature/favorites/src/main/java/com/starlight/expedition/feature/favorites/FavoritesScreen.kt`
- `feature/gamelist/src/main/java/com/starlight/expedition/feature/gamelist/GameListScreen.kt`

요구사항:

1. 마지막 콘텐츠와 하단 메뉴 상단 사이에 실기 기준 최소 16dp의 빈 공간을 확보합니다.
2. 시스템 내비게이션 바 높이를 반드시 포함합니다.
3. 고정 숫자만으로 처리하지 말고 `WindowInsets.navigationBars` 또는 동등한 Compose 방식으로 하단 안전영역을 반영합니다.
4. 빠른 시작, 홈, 즐겨찾기, 게임리스트가 동일한 하단 안전 여백 계산을 사용해야 합니다.
5. 콘텐츠가 하단 메뉴 뒤로 들어가거나 메뉴와 맞닿지 않아야 합니다.
6. 하단 메뉴 자체의 화면 위치, 너비, 4개 메뉴 구조는 유지합니다.
7. 목록 화면의 마지막 항목도 하단 메뉴 위에서 완전히 보여야 합니다.
8. 393dp 기준 디자인을 유지하고, 넓은 화면에서는 중앙 고정 원칙을 유지합니다.

권장 계산 개념:

```text
콘텐츠 하단 여백 =
하단 메뉴 높이
+ 메뉴 하단 간격
+ 시스템 내비게이션 바 높이
+ 콘텐츠와 메뉴 사이 16dp
```

중복 계산으로 공간이 과도하게 커지지 않도록 한 곳에서 공통화합니다.

## 6. 전체 배경색과 카드 색상 정리

현재 화면 전체가 푸른 회보라색으로 보이고, 반투명 하단 메뉴 때문에 배경이 비쳐 보이는 문제를 수정합니다.

대상 중심 파일:

- `core/designsystem/src/main/java/com/starlight/expedition/core/designsystem/theme/StarlightColors.kt`
- `core/designsystem/src/main/java/com/starlight/expedition/core/designsystem/component/BottomNavBar.kt`
- 카드 배경을 사용하는 디자인시스템 컴포넌트

라이트 모드 기준 색상:

```text
앱 전체 배경: #F7F7FC
바깥 배경: #F7F7FC
일반 카드: #FFFFFF
카드 테두리: #ECEAF3
하단 메뉴 배경: #FFFFFF
하단 메뉴 테두리: #ECEAF3
기본 글자: #151724
보조 글자: #757988
기존 보라 포인트: 유지
```

요구사항:

1. `navBackground`의 알파를 제거하고 불투명 흰색으로 변경합니다.
2. 카드와 하단 메뉴 뒤로 화면 배경이 비치지 않게 합니다.
3. 화면 전체 배경과 바깥 배경의 색 차이로 띠가 생기지 않게 합니다.
4. 다크 모드는 기존 구조를 유지하되 이번 라이트 모드 수정 때문에 깨지지 않아야 합니다.
5. 임의 색상을 화면별로 직접 쓰지 말고 디자인 토큰을 사용합니다.

## 7. 빠른 시작 카드 배경 정리

현재 민트·보라 그라데이션이 카드 전체에 깔려 텍스트 영역과 카드 하단까지 번져 보이는 문제를 수정합니다.

대상 중심 파일:

- `feature/quickstart/src/main/java/com/starlight/expedition/feature/quickstart/QuickStartScreen.kt`
- `core/designsystem/src/main/java/com/starlight/expedition/core/designsystem/component/CoverFrame.kt`
- `core/designsystem/src/main/java/com/starlight/expedition/core/designsystem/theme/StarlightColors.kt`

요구사항:

1. 카드 기본 배경은 흰색을 사용합니다.
2. 보라·민트 계열 시각 효과는 오른쪽 이미지 프레임 또는 이미지 장식 영역 안에서만 보이게 합니다.
3. 왼쪽 텍스트 영역 뒤는 흰색으로 유지합니다.
4. 카드 아래쪽 전체에 민트색이 번지는 현상을 제거합니다.
5. 이미지 프레임은 기존 기울기, 위치, 크기 비율을 임의로 변경하지 않습니다.
6. 카드 모서리 밖으로 이미지가 새지 않도록 clip 순서를 확인합니다.
7. 첫 카드와 두 번째 카드의 카드 크기와 버튼 위치는 유지합니다.
8. 텍스트 가독성을 떨어뜨리는 이미지 레이어가 왼쪽 영역을 덮지 않게 합니다.

## 8. 홈 화면 정리

요구사항:

1. 마지막 최근 플레이 카드와 하단 메뉴 사이에 최소 16dp를 확보합니다.
2. 통계 카드와 최근 플레이 카드는 순백색 카드로 통일합니다.
3. 카드 간 세로 간격은 현재 구조를 유지하며 균일하게 보정합니다.
4. 홈 제목, 통계 수치, 최근 플레이 목록, 설정 버튼의 위치와 정보는 유지합니다.
5. 카드 높이를 무리하게 줄이거나 내용을 삭제하지 않습니다.

## 9. 하단 메뉴 선택 효과 정리

대상 중심 파일:

- `core/designsystem/src/main/java/com/starlight/expedition/core/designsystem/component/BottomNavBar.kt`
- `core/designsystem/src/main/java/com/starlight/expedition/core/designsystem/component/ClickEffects.kt`

요구사항:

1. 선택된 메뉴만 보라색 아이콘과 글자로 표시합니다.
2. 선택 원형 배경은 지정된 아이콘 영역 안에서만 표시합니다.
3. 페이지 변경 시 아이콘 겹침, 잔상, 크로스페이드가 없어야 합니다.
4. 아이콘과 메뉴 글자의 수직 간격을 4개 항목에서 동일하게 유지합니다.
5. 메뉴 항목을 누르는 짧은 축소 피드백은 유지할 수 있으나 페이지 화면에는 영향을 주지 않아야 합니다.
6. 하단 메뉴 카드의 크기와 4등분 구조를 변경하지 않습니다.

## 10. 금지사항

- 새 Android 프로젝트 생성
- WebView 적용
- 멀티모듈 통합 또는 모듈 삭제
- 기능 삭제로 빌드 통과
- 대표 이미지 삭제
- 페이지 전체 Crossfade 또는 AnimatedContent 추가
- 확정 GUI의 카드 크기·버튼 위치·하단 메뉴 구조 임의 변경
- Gradle·Kotlin·AGP 버전 임의 업그레이드
- 의존성 무단 추가
- `gradle-wrapper.jar` 누락
- `.github` 폴더 누락
- 빌드하지 않은 상태에서 완료 보고
- 오류를 숨기기 위한 `continue-on-error` 추가
- Kotlin 오류 코드를 주석 처리하여 우회

## 11. 컴파일 오류 예방 기준

Compose 상태 위임을 사용할 때 import를 반드시 확인합니다.

```kotlin
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
```

`MutableInteractionSource`는 아래 경로를 사용합니다.

```kotlin
import androidx.compose.foundation.interaction.MutableInteractionSource
```

다른 모듈의 nullable 공개 프로퍼티는 직접 스마트 캐스트하지 말고 지역 변수에 저장한 뒤 검사합니다.

```kotlin
val lastPlayedAt = game.lastPlayedAt
if (lastPlayedAt != null) {
    // 사용
}
```

사용하지 않는 import와 중복 import를 제거합니다.  
수정 파일마다 전체 import와 컴파일 가능성을 다시 확인합니다.

## 12. 필수 빌드 검증

작업 완료 전 프로젝트 루트에서 반드시 아래를 순서대로 실행합니다.

```bash
python3 scripts/verify_project_structure.py
./gradlew --no-daemon clean :app:assembleDebug --stacktrace
```

GitHub에 반영한 뒤에는 반드시 아래 워크플로가 성공해야 합니다.

```text
.github/workflows/android-build.yml
```

성공 조건:

- `Build Android APK With Full Logs` 초록색 성공
- `StarlightExpedition-debug-apk` 아티팩트 생성
- `app-debug.apk` 포함
- 전체 빌드 로그 아티팩트 생성
- Kotlin compile error 0건
- 누락 파일 0건

빌드가 실패하면 첫 실제 오류부터 수정하고 다시 빌드합니다.  
빌드 성공 전에는 작업 완료로 보고하지 않습니다.

## 13. 실기 확인

아래 순서로 최소 10회 반복합니다.

```text
빠른 시작 → 홈 → 즐겨찾기 → 게임리스트 → 빠른 시작
```

확인 항목:

- 이전 페이지 잔상 없음
- 검은 프레임이나 투명 배경 없음
- 대표 이미지 전환 중 페이지 이동해도 이미지 잔상 없음
- 마지막 카드와 하단 메뉴 사이 최소 16dp
- 시스템 내비게이션 바와 하단 메뉴 겹침 없음
- 모든 페이지 하단 간격 동일
- 하단 메뉴 선택 표시 즉시 변경
- 빠른 시작 카드 왼쪽 텍스트 영역 흰색 유지
- 카드 하단 민트색 번짐 제거
- 393dp 화면 구조 유지

## 14. 최종 산출물

완료 후 하나의 ZIP 파일만 전달합니다.

ZIP 루트에는 바로 아래 파일과 폴더가 보여야 하며, 바깥에 프로젝트명 폴더를 한 겹 더 만들지 않습니다.

```text
.github/
app/
core/
feature/
gradle/
scripts/
build.gradle.kts
settings.gradle.kts
gradle.properties
gradlew
gradlew.bat
README.md
BUILD_REPORT.md
```

`BUILD_REPORT.md`에는 아래만 정확히 작성합니다.

```text
- 수정한 파일
- 페이지 잔상 제거 방법
- 하단 안전 여백 계산 방법
- 변경한 색상 토큰
- 유지한 애니메이션
- 제거한 애니메이션
- 로컬 빌드 명령과 결과
- GitHub Actions 결과
- APK 생성 경로
- 남은 오류
```

완료 파일명:

```text
StarlightExpedition_UI_Stabilization_v1.0.zip
```
