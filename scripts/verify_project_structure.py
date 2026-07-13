#!/usr/bin/env python3
from pathlib import Path
import sys

ROOT = Path(__file__).resolve().parent.parent

required_files = [
    ".github/workflows/android-build.yml",
    ".gitignore",
    "build.gradle.kts",
    "settings.gradle.kts",
    "gradle.properties",
    "gradlew",
    "gradlew.bat",
    "gradle/libs.versions.toml",
    "gradle/wrapper/gradle-wrapper.jar",
    "gradle/wrapper/gradle-wrapper.properties",
    "app/build.gradle.kts",
    "app/proguard-rules.pro",
    "app/src/main/AndroidManifest.xml",
    "app/src/main/java/com/starlight/expedition/AppContainer.kt",
    "core/common/build.gradle.kts",
    "core/data/build.gradle.kts",
    "core/designsystem/build.gradle.kts",
    "core/model/build.gradle.kts",
    "core/navigation/build.gradle.kts",
    "feature/quickstart/build.gradle.kts",
    "feature/home/build.gradle.kts",
    "feature/favorites/build.gradle.kts",
    "feature/gamelist/build.gradle.kts",
    "feature/settings/build.gradle.kts",
    "feature/library/build.gradle.kts",
    "feature/library/src/main/java/com/starlight/expedition/feature/library/GameFolderDialog.kt",
    "feature/library/src/main/java/com/starlight/expedition/feature/library/GameFolderViewModel.kt",
    "feature/library/src/main/java/com/starlight/expedition/feature/library/GameFolderUiState.kt",
    "feature/library/src/main/java/com/starlight/expedition/feature/library/GameFolderManagementRow.kt",
    "core/data/src/main/java/com/starlight/expedition/core/data/scanner/PlatformClassifier.kt",
    "core/data/src/main/java/com/starlight/expedition/core/data/scanner/GameTitleNormalizer.kt",
    "core/data/src/main/java/com/starlight/expedition/core/data/scanner/GameFileGrouping.kt",
    "core/data/src/main/java/com/starlight/expedition/core/data/scanner/DocumentsContractTreeReader.kt",
    "core/data/src/main/java/com/starlight/expedition/core/data/scanner/GameDocumentScanner.kt",
    "core/data/src/main/java/com/starlight/expedition/core/data/local/GameLibraryFileStore.kt",
    "core/data/src/main/java/com/starlight/expedition/core/data/local/GameFolderPreferences.kt",
    "core/data/src/main/java/com/starlight/expedition/core/data/repository/GameFolderRepository.kt",
    "core/data/src/main/java/com/starlight/expedition/core/data/repository/GameFolderRepositoryImpl.kt",
    "core/data/src/main/java/com/starlight/expedition/core/data/image/LocalCoverImageLoader.kt",
    "core/data/src/test/java/com/starlight/expedition/core/data/scanner/PlatformClassifierTest.kt",
    "core/data/src/test/java/com/starlight/expedition/core/data/scanner/GameTitleNormalizerTest.kt",
    "core/data/src/test/java/com/starlight/expedition/core/data/scanner/GameFileGroupingTest.kt",
    "core/data/src/test/java/com/starlight/expedition/core/data/local/GameLibraryFileStoreTest.kt",
    "core/data/src/test/java/com/starlight/expedition/core/data/repository/RepositoryTest.kt",
    "core/designsystem/src/main/res/drawable-nodpi/cover_starlight.png",
    "core/designsystem/src/main/res/drawable-nodpi/cover_hero_legend.png",
]

errors = []
for rel in required_files:
    path = ROOT / rel
    if not path.is_file():
        errors.append(f"MISSING FILE: {rel}")
    elif path.stat().st_size == 0:
        errors.append(f"EMPTY FILE: {rel}")

settings = (ROOT / "settings.gradle.kts").read_text(encoding="utf-8")
required_modules = [
    ":app",
    ":core:model",
    ":core:data",
    ":core:designsystem",
    ":core:navigation",
    ":core:common",
    ":feature:quickstart",
    ":feature:home",
    ":feature:favorites",
    ":feature:gamelist",
    ":feature:settings",
    ":feature:library",
]
for module in required_modules:
    if f'include("{module}")' not in settings:
        errors.append(f"MISSING MODULE INCLUDE: {module}")

app_build = (ROOT / "app/build.gradle.kts").read_text(encoding="utf-8")
if 'project(":feature:library")' not in app_build:
    errors.append("app/build.gradle.kts MISSING DEPENDENCY: :feature:library")

wrapper = ROOT / "gradle/wrapper/gradle-wrapper.jar"
if wrapper.exists() and wrapper.stat().st_size < 10_000:
    errors.append("INVALID WRAPPER JAR: file is unexpectedly small")

kotlin_files = list(ROOT.glob("**/*.kt"))
if len(kotlin_files) < 90:
    errors.append(f"TOO FEW KOTLIN FILES: {len(kotlin_files)}")

if (ROOT / "StarlightExpedition").is_dir():
    errors.append("NESTED PROJECT ROOT DETECTED: upload the contents, not an outer project folder")
if (ROOT / "StarlightExpedition_GameLibrary_v2").is_dir():
    errors.append("NESTED PROJECT ROOT DETECTED: upload the contents, not an outer project folder")

if errors:
    print("PROJECT STRUCTURE CHECK FAILED")
    for error in errors:
        print(f"- {error}")
    sys.exit(1)

print("PROJECT STRUCTURE CHECK PASSED")
print(f"- root: {ROOT}")
print(f"- Kotlin files: {len(kotlin_files)}")
print(f"- required files: {len(required_files)}")
print(f"- required modules: {len(required_modules)}")
