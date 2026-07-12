이 폴더에는 debug APK가 없습니다.

작업지시서가 요구하는 PLAYROOM_UI_Prototype_0.1.0_debug.apk는 이 프로젝트를
만든 샌드박스 환경에 Android SDK와 Google Maven(dl.google.com /
maven.google.com) 접근이 없어 실제로 빌드하지 못했습니다. 이유와 실제로
확인한 내용은 프로젝트 루트의 BUILD_REPORT.md에 자세히 적어두었습니다.

APK를 만들려면 이 프로젝트 폴더 전체를 Android Studio에서 열고 아래 명령을
실행하세요:

    ./gradlew clean testDebugUnitTest assembleDebug

빌드가 끝나면 다음 위치에 APK가 생성됩니다:

    app/build/outputs/apk/debug/app-debug.apk
