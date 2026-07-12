# BUILD REPORT

- 프로젝트명: PLAYROOM UI Prototype
- 버전: 0.1.0
- 빌드 날짜: 2026-07-12
- Android Gradle Plugin: 8.7.2
- Kotlin: 2.0.21
- compileSdk: 35
- targetSdk: 35
- minSdk: 26
- 모듈 수: 12 (`:app` 1, `:core:*` 4, `:feature:*` 7) + `:build-logic:convention` 1개 포함 빌드
- 실행 명령: `./gradlew clean testDebugUnitTest assembleDebug`
- testDebugUnitTest: **미실행** (아래 "실제로 확인한 것" 참고 — 동등한 검증은 수행함)
- assembleDebug: **미실행**
- APK 경로: 없음 — 이 환경에서 생성되지 않았습니다
- APK 크기: 없음
- 남은 경고: 확인 불가 (Gradle/AGP를 실행하지 못해 lint·컴파일러 경고 목록을 얻을 수 없음)
- 알려진 제한: 아래 "왜 빌드를 실행하지 못했는가" 참고

---

## 왜 빌드를 실행하지 못했는가

이 프로젝트를 작성한 도구 환경(샌드박스)의 네트워크는 다음 도메인으로만
제한되어 있습니다: `pypi.org`, `npmjs.com`, `github.com`(+ release-assets),
`crates.io`, `archive.ubuntu.com` 등 개발용 패키지 저장소 일부.

**`dl.google.com` / `maven.google.com`(AndroidX·Compose·AGP 배포처),
`services.gradle.org`(Gradle 배포 서버), `repo.maven.apache.org`(Maven Central)에는
접근할 수 없습니다.** 직접 확인한 결과는 아래와 같습니다.

```text
$ curl -sI https://dl.google.com          → HTTP 403 (host_not_allowed)
$ curl -sI https://maven.google.com       → HTTP 403 (host_not_allowed)
$ curl -sI https://services.gradle.org    → HTTP 403 (host_not_allowed)
$ curl -sI https://repo.maven.apache.org  → HTTP 403 (host_not_allowed)
```

Android SDK도 설치되어 있지 않습니다(`ANDROID_HOME` 미설정, `/usr/lib/android-sdk`
없음). 이 세 가지(Gradle 배포본, Android SDK, AndroidX/Compose 의존성)가 모두
없으면 `assembleDebug`는 물리적으로 실행할 수 없습니다 — 코드 품질과 무관한
환경 제약입니다.

작업지시서 1번·26번·28번·32번 항목이 명시적으로 "빌드하지 않은 결과를 완료로
보고하지 않는다"를 요구하므로, **여기서 assembleDebug/testDebugUnitTest 성공을
주장하지 않습니다.**

## 실제로 확인한 것

Gradle/AGP는 실행할 수 없었지만, GitHub(허용된 도메인)에서 Kotlin 컴파일러
배포본(`kotlinc 2.0.21`, 이 프로젝트의 Kotlin 버전과 동일)을 직접 내려받아
아래 두 가지를 **실제로 실행**했습니다.

1. **`:core:model` 전체 컴파일** — 외부 의존성이 전혀 없는 순수 Kotlin
   모듈이라 실제 라이브러리 없이도 그대로 컴파일됩니다. 결과: **오류 0, 경고
   0**, 클래스 파일 18개 생성 확인.

2. **`:core:data`(메인 + 테스트) 컴파일 및 테스트 실행** — 이 모듈이 쓰는
   `kotlinx.coroutines.flow` API 표면(`StateFlow`/`MutableStateFlow`/
   `asStateFlow`/`update`)과 JUnit4(`@Test`, `Assert`)가 실제 라이브러리와
   동일한 시그니처의 최소 스텁으로 대체되었을 뿐, `GameRepository`,
   `InMemoryGameRepository`, `GameQueries.kt`, `GameQueriesTest.kt`는
   **한 글자도 수정하지 않은 실제 소스 코드**입니다. 결과:

   ```text
   PASS: library filter favorites returns only favorited games
   PASS: library filter by system returns only that system
   PASS: search matches by title and by system, ignores case
   PASS: recommendation options filter keeps only matching games
   PASS: pickRecommendation never returns a game that is not launchable
   PASS: pickRecommendation avoids the excluded game when an alternative exists
   Total: 6, Passed: 6, Failed: 0
   ```

   작업지시서 §27이 요구하는 4개 테스트(즐겨찾기 필터·기종 필터·검색·추천
   옵션 필터)를 포함해 6개 모두 통과했습니다. 실제 Gradle의
   `testDebugUnitTest`가 정확히 같은 결과를 낼 것이라고 100% 보장하지는
   않지만(예: JUnit 러너 자체의 동작 차이), 테스트가 검증하는 로직 자체는
   실제 컴파일된 바이트코드로 실행되어 통과했습니다.

3. **나머지 10개 모듈(Compose UI 전부: `:core:designsystem`, `:feature:*`
   7개, `:app`)은 컴파일할 수 없었습니다** — AndroidX Compose/Material3/
   Navigation과 Android SDK 자체가 필요하기 때문입니다. 대신:
   - 89개 Kotlin 파일 전체에 대해 중괄호·괄호·대괄호 균형, `package`
     선언과 디렉터리 경로 일치, `TODO`/`FIXME`/데드코드 흔적을 정적으로
     검사했습니다 (전부 통과).
   - 커스텀 Composable·클래스·함수 146개의 선언부와 모든 호출부를
     교차 검증해 이름 오타가 없는지 확인했습니다 (일치하지 않는 항목
     0개 — 남은 2건은 `@Preview`, `CompositionLocalProvider` 같은
     AndroidX 자체 심볼이라 정상).
   - 작업 중 실제로 3건의 버그를 이 과정에서 발견해 수정했습니다:
     (a) 팔레트 색상 리터럴 오타, (b) 태블릿 폭 제한 Modifier가 아무 효과가
     없던 버그, (c) `Modifier` 확장 함수 안에서 `@Composable`인
     `remember()`를 호출해 컴파일이 깨질 뻔한 부분.
   - 이 검사들은 **실제 컴파일을 대체하지 못합니다.** 타입 불일치, Compose
     컴파일러 전용 규칙 위반, 리소스 참조 오류(`R.drawable.xxx` 철자 등)는
     실제 Android Studio Gradle Sync에서만 확실히 드러납니다.

## 사용자가 해야 할 일

Android Studio(Hedgehog 이상, 내장 JDK 17+ 포함)가 설치된 일반 PC에서:

```bash
./gradlew clean testDebugUnitTest assembleDebug
```

정상적으로 성공하면 APK는 `app/build/outputs/apk/debug/app-debug.apk`에
생성됩니다. 만약 실패한다면 대부분 리소스 참조 철자나 Compose 컴파일러
규칙 관련 사소한 오류일 가능성이 높습니다 — 오류 로그를 알려주시면 이어서
수정하겠습니다.
