# GitHub Actions 로그 확인 방법

이 패키지에는 다음 워크플로가 포함되어 있습니다.

`.github/workflows/android-build.yml`

## 실행

GitHub 저장소의 `Actions` 탭에서:

1. `Build Android APK With Full Logs`
2. `Run workflow`
3. 실행 완료 후 맨 아래 `Artifacts`

## 빌드 실패 시 다운로드

`StarlightExpedition-full-build-logs`

압축을 풀고 아래 순서로 확인합니다.

1. `error-summary.log`
2. `assembleDebug-full.log`
3. `environment.log`
4. `verify-project.log`
5. `gradle-version.log`

`error-summary.log`에는 최초 오류 후보와 전체 로그 마지막 300줄이 저장됩니다.

## 빌드 성공 시

`StarlightExpedition-debug-apk` 아티팩트가 함께 생성됩니다.
