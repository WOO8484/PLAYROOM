package com.starlight.expedition

import android.content.Context
import com.starlight.expedition.core.data.image.LocalCoverImageLoader
import com.starlight.expedition.core.data.local.GameFolderPreferences
import com.starlight.expedition.core.data.local.GameLibraryFileStore
import com.starlight.expedition.core.data.repository.FavoritesRepository
import com.starlight.expedition.core.data.repository.FavoritesRepositoryImpl
import com.starlight.expedition.core.data.repository.GameFolderRepository
import com.starlight.expedition.core.data.repository.GameFolderRepositoryImpl
import com.starlight.expedition.core.data.repository.GameRepository
import com.starlight.expedition.core.data.repository.GameRepositoryImpl
import com.starlight.expedition.core.data.repository.RecommendationRepository
import com.starlight.expedition.core.data.repository.RecommendationRepositoryImpl
import com.starlight.expedition.core.data.repository.SettingsRepository
import com.starlight.expedition.core.data.repository.SettingsRepositoryImpl
import com.starlight.expedition.core.data.scanner.DocumentsContractTreeReader
import com.starlight.expedition.core.data.scanner.GameDocumentScanner
import java.io.File
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

/**
 * Hilt를 쓰지 않으므로, 이 단순한 컨테이너가 Repository 구현체를 한 곳에서 생성해
 * 각 화면의 ViewModel에 생성자로 전달합니다(실행지시서 39절).
 *
 * 폴더 검색은 이 [applicationScope] 하나에서만 실행됩니다. 다이얼로그를 닫거나
 * 탭을 이동해도 검색이 계속되는 이유가 이 Scope가 Application 수명이기 때문입니다.
 */
class AppContainer(context: Context) {

    private val appContext = context.applicationContext

    /** 앱 프로세스 수명 동안 유지되는 단일 Scope입니다. 검색 작업은 항상 이 Scope에서만 실행됩니다. */
    val applicationScope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    val favoritesRepository: FavoritesRepository = FavoritesRepositoryImpl(appContext)
    val settingsRepository: SettingsRepository = SettingsRepositoryImpl(appContext)

    private val gameLibraryFileStore = GameLibraryFileStore(File(appContext.filesDir, "starlight"))
    private val gameFolderPreferences = GameFolderPreferences(appContext)
    private val documentsContractTreeReader = DocumentsContractTreeReader(appContext)
    private val gameDocumentScanner = GameDocumentScanner(appContext, documentsContractTreeReader)

    private val gameRepositoryImpl: GameRepositoryImpl = GameRepositoryImpl(
        fileStore = gameLibraryFileStore,
        favoritesRepository = favoritesRepository,
        repositoryScope = applicationScope
    )
    val gameRepository: GameRepository = gameRepositoryImpl

    val gameFolderRepository: GameFolderRepository = GameFolderRepositoryImpl(
        context = appContext,
        folderPreferences = gameFolderPreferences,
        scanner = gameDocumentScanner,
        gameRepository = gameRepositoryImpl,
        applicationScope = applicationScope
    )

    val recommendationRepository: RecommendationRepository = RecommendationRepositoryImpl()

    val localCoverImageLoader: LocalCoverImageLoader = LocalCoverImageLoader(appContext)
}
