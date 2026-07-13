package com.starlight.expedition

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.starlight.expedition.core.designsystem.component.BottomNavBar
import com.starlight.expedition.core.designsystem.component.GameFolderIconButton
import com.starlight.expedition.core.designsystem.component.SettingsGearButton
import com.starlight.expedition.core.designsystem.theme.DesignReferenceWidth
import com.starlight.expedition.core.designsystem.theme.StarlightTheme
import com.starlight.expedition.core.navigation.StarlightRoute
import com.starlight.expedition.core.navigation.starlightBottomNavEntries
import com.starlight.expedition.feature.favorites.FavoritesScreen
import com.starlight.expedition.feature.favorites.FavoritesViewModel
import com.starlight.expedition.feature.gamelist.GameListScreen
import com.starlight.expedition.feature.gamelist.GameListViewModel
import com.starlight.expedition.feature.home.HomeScreen
import com.starlight.expedition.feature.home.HomeViewModel
import com.starlight.expedition.feature.library.GameFolderDialog
import com.starlight.expedition.feature.library.GameFolderViewModel
import com.starlight.expedition.feature.quickstart.QuickStartScreen
import com.starlight.expedition.feature.quickstart.QuickStartViewModel
import com.starlight.expedition.feature.settings.SettingsDialog
import com.starlight.expedition.feature.settings.SettingsViewModel

/**
 * 앱 전체 조립부입니다. 393dp 기준 화면을 유지하고, 600dp 이상 화면에서는
 * 393dp 너비로 중앙 고정합니다(태블릿 2열 재배치 없음). 393dp보다 좁은 화면에서는
 * 기기 너비에 맞춰 자연스럽게 줄어듭니다.
 */
@Composable
fun StarlightApp(appContainer: AppContainer) {
    val colors = StarlightTheme.colors

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.windowBackground),
        contentAlignment = Alignment.TopCenter
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .widthIn(max = DesignReferenceWidth)
                .background(colors.appBackground)
        ) {
            StarlightAppContent(appContainer = appContainer)
        }
    }
}

@Composable
private fun StarlightAppContent(appContainer: AppContainer) {
    val navController: NavHostController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = StarlightRoute.fromRoute(backStackEntry?.destination?.route)

    var settingsDialogVisible by remember { mutableStateOf(false) }
    var folderDialogVisible by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize()) {
        StarlightHeader(
            showsSettingsButton = currentRoute.showsSettingsButton,
            onSettingsClick = { settingsDialogVisible = true },
            onFolderClick = { folderDialogVisible = true }
        )

        Box(
            modifier = Modifier
                .weight(1f)
                .background(StarlightTheme.colors.appBackground)
        ) {
            NavHost(
                navController = navController,
                startDestination = StarlightRoute.QUICK_START.route,
                enterTransition = { EnterTransition.None },
                exitTransition = { ExitTransition.None },
                popEnterTransition = { EnterTransition.None },
                popExitTransition = { ExitTransition.None }
            ) {
                composable(StarlightRoute.QUICK_START.route) {
                    val viewModel: QuickStartViewModel = viewModel(
                        factory = viewModelFactory {
                            initializer {
                                QuickStartViewModel(
                                    gameRepository = appContainer.gameRepository,
                                    recommendationRepository = appContainer.recommendationRepository
                                )
                            }
                        }
                    )
                    val uiState by viewModel.uiState.collectAsState()
                    QuickStartScreen(
                        uiState = uiState,
                        imageLoader = appContainer.localCoverImageLoader,
                        onRandomRecommendation = viewModel::requestRecommendation,
                        onAddFolderClick = { folderDialogVisible = true }
                    )
                }

                composable(StarlightRoute.HOME.route) {
                    val viewModel: HomeViewModel = viewModel(
                        factory = viewModelFactory {
                            initializer {
                                HomeViewModel(
                                    gameRepository = appContainer.gameRepository,
                                    favoritesRepository = appContainer.favoritesRepository
                                )
                            }
                        }
                    )
                    val uiState by viewModel.uiState.collectAsState()
                    HomeScreen(
                        uiState = uiState,
                        imageLoader = appContainer.localCoverImageLoader,
                        onViewAllClick = {
                            navController.navigate(StarlightRoute.GAME_LIST.route) {
                                launchSingleTop = true
                            }
                        }
                    )
                }

                composable(StarlightRoute.FAVORITES.route) {
                    val viewModel: FavoritesViewModel = viewModel(
                        factory = viewModelFactory {
                            initializer { FavoritesViewModel(gameRepository = appContainer.gameRepository) }
                        }
                    )
                    val uiState by viewModel.uiState.collectAsState()
                    FavoritesScreen(
                        uiState = uiState,
                        imageLoader = appContainer.localCoverImageLoader,
                        onToggleFavorite = viewModel::toggleFavorite
                    )
                }

                composable(StarlightRoute.GAME_LIST.route) {
                    val viewModel: GameListViewModel = viewModel(
                        factory = viewModelFactory {
                            initializer { GameListViewModel(gameRepository = appContainer.gameRepository) }
                        }
                    )
                    val uiState by viewModel.uiState.collectAsState()
                    GameListScreen(
                        uiState = uiState,
                        imageLoader = appContainer.localCoverImageLoader,
                        onQueryChange = viewModel::onQueryChange,
                        onPlatformSelected = viewModel::onPlatformSelected,
                        onToggleFavorite = viewModel::toggleFavorite
                    )
                }
            }

            BottomNavBar(
                entries = starlightBottomNavEntries,
                selectedRoute = currentRoute.route,
                onSelect = { route ->
                    if (route != currentRoute.route) {
                        navController.navigate(route) {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .navigationBarsPadding()
                    .padding(horizontal = StarlightTheme.spacing.bottomNavHorizontalMargin)
                    .padding(bottom = StarlightTheme.spacing.bottomNavBottomGap)
            )
        }
    }

    if (settingsDialogVisible && currentRoute.showsSettingsButton) {
        val settingsViewModel: SettingsViewModel = viewModel(
            factory = viewModelFactory {
                initializer { SettingsViewModel(settingsRepository = appContainer.settingsRepository) }
            }
        )
        val settingsUiState by settingsViewModel.uiState.collectAsState()

        SettingsDialog(
            uiState = settingsUiState,
            onDismiss = { settingsDialogVisible = false },
            onSoundChange = settingsViewModel::setSoundEnabled,
            onAutoSaveChange = settingsViewModel::setAutoSaveEnabled,
            onDarkModeChange = settingsViewModel::setDarkMode
        )
    }

    if (folderDialogVisible) {
        val folderViewModel: GameFolderViewModel = viewModel(
            factory = viewModelFactory {
                initializer { GameFolderViewModel(folderRepository = appContainer.gameFolderRepository) }
            }
        )
        val folderUiState by folderViewModel.uiState.collectAsState()

        GameFolderDialog(
            uiState = folderUiState,
            onDismiss = { folderDialogVisible = false },
            onDialogOpened = folderViewModel::onDialogOpened,
            onFolderPicked = folderViewModel::onFolderPicked,
            onRemoveFolder = folderViewModel::onRemoveFolder,
            onRescanFolder = folderViewModel::onRescanFolder,
            onRescanAll = folderViewModel::onRescanAll,
            onCancelScan = folderViewModel::onCancelScan
        )
    }

    // 빠른 시작으로 돌아오면 설정 버튼이 사라지므로, 열려 있던 Dialog도 함께 닫습니다.
    LaunchedEffect(currentRoute) {
        if (!currentRoute.showsSettingsButton) {
            settingsDialogVisible = false
        }
    }
}

@Composable
private fun StarlightHeader(
    showsSettingsButton: Boolean,
    onSettingsClick: () -> Unit,
    onFolderClick: () -> Unit
) {
    val colors = StarlightTheme.colors

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .height(StarlightTheme.spacing.headerHeight)
            .padding(horizontal = 22.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(17.dp))
                    .background(Brush.linearGradient(listOf(colors.primaryVariant, colors.primary))),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.AutoAwesome,
                    contentDescription = null,
                    tint = colors.onPrimary,
                    modifier = Modifier.size(24.dp)
                )
            }

            Column(modifier = Modifier.padding(start = 13.dp)) {
                Text(
                    text = "별빛 탐험대",
                    style = StarlightTheme.typography.brandTitle,
                    color = colors.textPrimary
                )
                Text(
                    text = "STARLIGHT EXPEDITION",
                    style = StarlightTheme.typography.brandSubtitle,
                    color = colors.textMuted,
                    modifier = Modifier.padding(top = 6.dp)
                )
            }
        }

        // 폴더 아이콘은 모든 주요 탭에서 접근 가능해야 하므로 설정 버튼 표시 여부와 무관하게 항상 보입니다.
        Row(verticalAlignment = Alignment.CenterVertically) {
            GameFolderIconButton(onClick = onFolderClick)
            if (showsSettingsButton) {
                SettingsGearButton(onClick = onSettingsClick, modifier = Modifier.padding(start = 4.dp))
            }
        }
    }
}
