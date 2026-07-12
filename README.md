# PLAYROOM UI Prototype 0.1.0

PLAYROOM 앱의 **UI 테스트 전용** Android 네이티브 프로토타입입니다.
Kotlin + Jetpack Compose + Material 3, Gradle 멀티모듈 구조로 작성되었습니다.
실제 에뮬레이션·ROM 파일 접근·저장은 전혀 하지 않으며, 모든 게임 데이터는 코드에
내장된 가짜(fake) 데이터입니다.

## ⚠️ 빌드 상태 — 먼저 읽어주세요

이 프로젝트의 **소스 코드는 100% 완성**되어 있지만, 이 코드를 생성한 작업 환경
(샌드박스)은 인터넷 접근이 `pypi.org`, `github.com`, `npmjs.com` 등 일부
개발 도메인으로 제한되어 있어 **Android SDK, Google Maven(dl.google.com /
maven.google.com), Gradle 배포 서버(services.gradle.org)에 접근할 수 없습니다.**
이 때문에 이 환경에서는 `./gradlew assembleDebug`를 실제로 실행해 debug APK를
만들어낼 수 없었습니다.

작업지시서 26번 항목 "빌드하지 않은 결과를 완료라고 보고" 금지 원칙에 따라,
**실행하지 않은 빌드를 성공했다고 보고하지 않습니다.** 자세한 내용과 각 항목별
상태는 `BUILD_REPORT.md`를 확인하세요.

**Android Studio(Hedgehog 이상)가 설치된 일반 PC에서는 별도 설정 없이 아래
명령 한 줄로 정상적으로 빌드되어야 합니다** — 이 프로젝트는 표준 AndroidX /
Compose / AGP 조합만 사용하는 평범한 Gradle 프로젝트입니다.

```bash
./gradlew clean testDebugUnitTest assembleDebug
```

빌드 후 APK 위치:

```text
app/build/outputs/apk/debug/app-debug.apk
```

## 실행 방법

1. 이 ZIP의 압축을 풉니다.
2. Android Studio에서 `PLAYROOM_UI_Prototype_0.1.0` 폴더를 "Open" 합니다.
3. Gradle Sync가 끝날 때까지 기다립니다 (최초 1회는 의존성 다운로드로 수 분
   소요될 수 있습니다).
4. 에뮬레이터 또는 실기기(minSdk 26 / Android 8.0 이상)를 선택하고 Run 합니다.

## 기술 스택

- Kotlin, Jetpack Compose, Material 3
- Single Activity, Gradle Kotlin DSL, Version Catalog
- JDK 17 / minSdk 26 / compileSdk·targetSdk 35
- Hilt·Room·Retrofit·Coil·Firebase·네트워크 라이브러리 **미사용**
- 즐겨찾기 상태는 `GameRepository`(메모리 내), 첫 실행 여부만 `SharedPreferences`에 저장

## 문서

- `MODULE_STRUCTURE.md` — 모듈 목록, 역할, 의존 관계, 공통 컴포넌트/데이터 위치
- `BUILD_REPORT.md` — 실제 실행한 명령과 결과, 알려진 제한사항 (정직하게 작성)
- `DEVICE_CHECKLIST.md` — 화면 크기·글자 배율·내비게이션 방식별 확인 체크리스트
  (실기기 확인은 사용자가 직접 진행해야 함을 명시)

## 이번 버전에서 하지 않는 것

- 실제 ROM 파일 읽기/쓰기, 실제 에뮬레이션
- 실제 폴더 선택기(SAF) — "게임 폴더 추가"는 1초 뒤 결과를 보여주는 가짜 흐름
- 네트워크 통신, 광고, 분석 SDK

## GitHub Actions 빌드

Gradle Wrapper와 GitHub Actions 자동 빌드 설정이 포함되어 있습니다.
자세한 방법은 [`GITHUB_BUILD.md`](GITHUB_BUILD.md)를 확인하세요.
