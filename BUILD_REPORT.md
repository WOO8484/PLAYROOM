# BUILD REPORT — 별빛 탐험대 Android 네이티브 1차 개발

## 빌드 결과: 실패 (이 작업 환경 한정 / 네트워크 제한)

`./gradlew :app:assembleDebug`를 이 작업 환경(샌드박스)에서 실행했고,
**Gradle 배포본 다운로드 단계에서 실패**했습니다. 소스 코드 작성과 프로젝트 구조는
모두 완료되었지만, 이 환경의 외부 네트워크 접근 제한 때문에 마지막 컴파일 단계까지
진행하지 못했습니다. 아래에 실제로 실행한 명령과 결과를 그대로 남깁니다.

## 실제로 확인한 것

1. **`gradle-wrapper.jar` 확보 및 `gradlew` 정상 동작 확인**
   이 환경은 `services.gradle.org`(Gradle 공식 배포처) 접근이 차단되어 있어 표준 방식으로는
   wrapper jar를 받을 수 없었습니다. 대신 이미 네트워크 허용 목록에 있는
   `raw.githubusercontent.com`을 통해 Gradle 공식 저장소(`gradle/gradle`)에 포함된
   `gradle-wrapper.jar`를 내려받아 배치했습니다. 이후 아래 명령이 정상적으로
   `GradleWrapperMain`을 실행하는 것까지 확인했습니다.

   ```bash
   $ ./gradlew --version
   Fetching distribution.
   Downloading https://services.gradle.org/distributions/gradle-8.10.2-bin.zip
   Attempt 1/1 failed. Reason: Server returned HTTP response code: 403 for URL:
   https://services.gradle.org/distributions/gradle-8.10.2-bin.zip
   Exception in thread "main" java.io.IOException: Server returned HTTP response code: 403 ...
   ```

   즉 `gradlew` 스크립트, `gradle-wrapper.properties`, `gradle-wrapper.jar` 자체는
   모두 정상 동작했고, 그다음 필요한 "실제 Gradle 배포본 다운로드"에서 막혔습니다.

2. **네트워크 제한 범위 확인**
   빌드에 필요한 아래 호스트들을 직접 확인했고, 전부 이 환경에서 403으로 차단되어 있습니다.

   ```text
   https://services.gradle.org   -> 403 (Gradle 배포본)
   https://dl.google.com         -> 403 (Android SDK, Google Maven)
   https://maven.google.com      -> 403 (Jetpack/Compose 라이브러리)
   https://repo1.maven.org       -> 403 (Maven Central)
   https://plugins.gradle.org    -> 403 (Gradle Plugin Portal)
   ```

   이 네 가지가 전부 열려야 `com.android.application`, `org.jetbrains.kotlin.android`,
   AndroxidX/Compose 의존성, Gradle 실행 파일 자체를 받을 수 있습니다. 이 작업 환경은
   `github.com`, `raw.githubusercontent.com`, `pypi.org`, `npmjs.com`,
   `archive.ubuntu.com` 등 제한된 목록만 허용하고 있어, 이 네 가지 호스트는 대체 경로도
   찾지 못했습니다.

3. **결론**
   이 환경에서는 물리적으로 Android/Gradle 빌드를 끝까지 실행할 수 없습니다. 소스
   코드·Gradle 설정·리소스는 표준적인 최신 Jetpack Compose 멀티모듈 프로젝트 구조와
   문법을 따랐고, 인터넷이 열려 있는 일반 개발 PC나 Android Studio에서는
   `./gradlew clean :app:assembleDebug`가 정상적으로 진행되어야 합니다. 다만 이 환경
   안에서는 그 결과를 직접 검증하지 못했다는 점을 분명히 남깁니다.

## APK 경로

`app/build/outputs/apk/debug/app-debug.apk` — **이번 작업에서는 생성되지 못했습니다.**
인터넷이 열려 있는 환경에서 위 빌드 명령을 실행하면 이 경로에 생성됩니다.

## 테스트

`./gradlew testDebugUnitTest`, `./gradlew lintDebug`도 같은 이유(의존성 다운로드 불가)로
이 환경에서 실행하지 못했습니다. 다만 순수 Kotlin 로직에 대한 JUnit 테스트는 작성해
두었으며, 인터넷이 열린 환경에서 실행하면 검증할 수 있습니다.

- `core/common/.../SearchUtilsTest.kt` — 검색어 정규화·부분 일치 검증
- `core/common/.../TimeFormatTest.kt` — 상대 시간·플레이 시간 포맷 검증
- `core/data/.../GameRepositoryImplTest.kt` — 즐겨찾기 상태 반영, 즐겨찾기 토글,
  최근 플레이 3개 이하·정렬 검증 (Fake Repository 사용)

## 구현 완료 기능

- 빠른 시작 / 홈 / 즐겨찾기 / 게임리스트 / 설정 5개 화면, 플로팅 하단 메뉴
- 빠른 시작에서만 설정 톱니바퀴 숨김, 나머지 3개 화면에서는 표시
- 빠른 시작 카드 2개 고정(스크롤 없음), 카드 간 공통 배경판 없음
- 두 커버 프레임 동일 비율 구현(상단 10.24% / 우측 -13.71% / 높이 74.70% / 모서리 29.84% / -5도)
- 대표 이미지 크로스페이드(3.5초 간격, 600ms 전환), 화면 이탈·백그라운드 시 자동 정지(Lifecycle 연동)
- 홈: 전체 스크롤 없음, 요약 카드 3개 + 최근 플레이 3개 동일 높이
- 즐겨찾기 / 게임리스트: 목록 영역만 스크롤, 마지막 카드가 하단 메뉴에 가려지지 않도록
  실제 콘텐츠 패딩 적용
- 게임명 검색(대소문자 무시, 공백 제거, 한글 지원) + 장르 필터 동시 적용, 검색 결과 없음 상태
- 즐겨찾기 등록·해제 및 DataStore 영구 저장(앱 재실행 후 유지)
- 다크모드 DataStore 영구 저장, 시스템 다크모드 자동 강제하지 않음, 라이트/다크 대비 확보
- 랜덤 선택(오늘 뭐 하지? 카드) 동작, 이어 하기·오늘 뭐 하지 카드에 사용 중인 게임은 추천 후보에서 제외
- 393dp 기준 레이아웃, 600dp 이상 화면에서 393dp 폭 중앙 고정, 태블릿 2열 재배치 없음
- 상태바·제스처 내비게이션 안전 영역 반영(`statusBarsPadding`, `navigationBarsPadding`)
- 로딩 / 성공 / 빈 결과 / 오류 상태를 가진 UiState 구조
- 접근성: 아이콘 버튼 `contentDescription`, 즐겨찾기 상태 설명, 장식 이미지 `contentDescription = null`
- Toast/Snackbar 형태의 성공 메시지 없음, 기본 직사각형 Ripple 대신 형태에 맞춘 클릭 반응(0.97배 축소)

## 미구현 기능 (1차 개발 범위 제외 — 실행지시서 3절 기준)

Libretro 실행, PPSSPP·기타 외부 에뮬레이터 연동, BIOS 검사, 실제 게임 폴더 스캔, 세이브 파일
관리, 치트 적용, PSP HD 텍스처 설치, AI 공략 API, 클라우드 동기화, 외부 서버 연결. 이 기능들과
연결될 예정인 버튼("이어서 하기", "바로 시작", "게임 소개")은 확정 GUI대로 화면에 존재하지만,
가짜 성공 메시지 없이 동작은 연결하지 않았습니다.

## 확인이 필요한 실제 오류

- 이 환경에서 실제 컴파일을 끝까지 수행하지 못했기 때문에, 문법 오류가 전혀 없다고
  100% 보장할 수는 없습니다. 작성 후 정적 검토(미사용 import 검사, 죽은 코드 검사,
  Gradle 스크립트 검토)는 수행했지만, 실제 `kotlinc`/AGP 컴파일 오류 여부는 인터넷이
  열린 환경에서 첫 `./gradlew :app:assembleDebug` 실행 시 최종 확인이 필요합니다.
- 커버 프레임 비율·크로스페이드·하단 메뉴 겹침 방지 등은 코드상 수치 그대로 구현했지만,
  실기기/에뮬레이터에서의 시각적 확인은 이 환경에서 수행하지 못했습니다. Android Studio
  Preview 또는 에뮬레이터에서 393dp 기준 화면으로 1차 확인을 권장합니다.
