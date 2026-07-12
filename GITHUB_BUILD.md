# GitHub Actions 빌드 방법

## 포함된 Gradle Wrapper

프로젝트 루트에 아래 파일이 모두 포함되어 있습니다.

- `gradlew`
- `gradlew.bat`
- `gradle/wrapper/gradle-wrapper.jar`
- `gradle/wrapper/gradle-wrapper.properties`

Wrapper 기준 Gradle 버전은 `8.9`입니다.

## GitHub에 업로드

1. 새 GitHub 저장소를 만듭니다.
2. 이 프로젝트 폴더의 **내용 전체**를 저장소 루트에 업로드합니다.
3. `Actions` 탭에서 `Android Build`를 실행합니다.
4. 빌드 완료 후 실행 결과 하단의 `Artifacts`에서 내려받습니다.

산출물:

- `PLAYROOM-debug-apk` — 설치용 `app-debug.apk`
- `PLAYROOM-build-reports` — 테스트 결과와 빌드 진단 파일

## 실행 명령

GitHub Actions는 다음 명령을 실행합니다.

```bash
./gradlew --stacktrace clean testDebugUnitTest assembleDebug
```

## Android Studio 로컬 빌드

1. Android Studio에서 프로젝트 루트 폴더를 엽니다.
2. JDK 17을 선택합니다.
3. Gradle Sync를 완료합니다.
4. 터미널에서 실행합니다.

macOS/Linux:

```bash
chmod +x gradlew
./gradlew clean testDebugUnitTest assembleDebug
```

Windows:

```bat
gradlew.bat clean testDebugUnitTest assembleDebug
```

APK 경로:

```text
app/build/outputs/apk/debug/app-debug.apk
```

## 참고

현재 패키지 제작 환경에서는 외부 Gradle 서버 접속이 차단되어 실제 빌드를 완료하지 못했습니다. GitHub Actions 또는 인터넷 연결이 가능한 Android Studio 환경에서 워크플로가 실제 의존성을 받아 빌드합니다.
