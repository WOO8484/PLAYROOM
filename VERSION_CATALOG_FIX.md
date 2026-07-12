# Version Catalog 빌드 오류 수정

## 원인
루트 프로젝트의 `gradle/libs.versions.toml`은 Gradle이 자동으로 `libs` 카탈로그로 등록합니다.

기존 `settings.gradle.kts`에서 같은 파일을 아래 방식으로 다시 등록해
`from()`이 두 번 호출되는 오류가 발생했습니다.

```kotlin
versionCatalogs {
    create("libs") {
        from(files("gradle/libs.versions.toml"))
    }
}
```

## 수정
루트 `settings.gradle.kts`의 수동 `versionCatalogs` 블록을 제거했습니다.

`build-logic/settings.gradle.kts`의 카탈로그 연결은 포함 빌드에서 필요한 설정이므로 유지했습니다.

## 다시 실행
```bash
./gradlew --stacktrace clean testDebugUnitTest assembleDebug
```
