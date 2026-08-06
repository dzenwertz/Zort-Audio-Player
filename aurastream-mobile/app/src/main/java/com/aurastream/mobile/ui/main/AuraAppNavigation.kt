package com.aurastream.mobile.ui.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.QueueMusic
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.aurastream.mobile.data.remote.RetrofitClient
import com.aurastream.mobile.data.repository.AuraRepositoryImpl
import com.aurastream.mobile.domain.model.Playlist
import com.aurastream.mobile.domain.model.Song
import com.aurastream.mobile.domain.usecase.*
import com.aurastream.mobile.ui.components.AddToPlaylistBottomSheet
import com.aurastream.mobile.ui.components.CreatePlaylistDialog
import com.aurastream.mobile.ui.components.ExpandedPlayerModal
import com.aurastream.mobile.ui.components.MiniPlayer
import com.aurastream.mobile.ui.home.HomeScreen
import com.aurastream.mobile.ui.home.HomeViewModel
import com.aurastream.mobile.ui.player.AudioPlayerManager
import com.aurastream.mobile.ui.playlists.PlaylistDetailScreen
import com.aurastream.mobile.ui.playlists.PlaylistViewModel
import com.aurastream.mobile.ui.playlists.PlaylistsScreen
import com.aurastream.mobile.ui.search.SearchScreen
import com.aurastream.mobile.ui.search.SearchViewModel
import com.aurastream.mobile.ui.theme.SpotifyDark
import com.aurastream.mobile.ui.theme.SpotifyGreen
import com.aurastream.mobile.ui.theme.SpotifySurface
import com.aurastream.mobile.ui.theme.TextSecondary

sealed class NavScreen(val route: String, val title: String, val icon: ImageVector) {
    data object Home : NavScreen("home", "Inicio", Icons.Default.Home)
    data object Search : NavScreen("search", "Buscar", Icons.Default.Search)
    data object Playlists : NavScreen("playlists", "Biblioteca", Icons.Default.QueueMusic)
}

@Composable
fun AuraAppNavigation() {
    val context = LocalContext.current
    val navController = rememberNavController()

    val repository = remember { AuraRepositoryImpl(RetrofitClient.apiService) }
    val audioPlayerManager = remember { AudioPlayerManager(context) }

    val homeViewModel = remember {
        HomeViewModel(
            GetRecommendedSongsUseCase(repository),
            GetPlaylistsUseCase(repository),
            SearchSongsUseCase(repository)
        )
    }

    val playlistViewModel = remember {
        PlaylistViewModel(
            GetPlaylistsUseCase(repository),
            CreatePlaylistUseCase(repository),
            AddSongToPlaylistUseCase(repository),
            RemoveSongFromPlaylistUseCase(repository),
            SearchSongsUseCase(repository)
        )
    }

    val searchViewModel = remember {
        SearchViewModel(SearchSongsUseCase(repository))
    }

    val currentSong by audioPlayerManager.currentSong.collectAsState()
    val isPlaying by audioPlayerManager.isPlaying.collectAsState()
    val currentPositionMs by audioPlayerManager.currentPositionMs.collectAsState()
    val durationMs by audioPlayerManager.durationMs.collectAsState()
    val isShuffle by audioPlayerManager.isShuffle.collectAsState()
    val isRepeat by audioPlayerManager.isRepeat.collectAsState()

    var showExpandedPlayer by remember { mutableStateOf(false) }
    var selectedSongForPlaylistMenu by remember { mutableStateOf<Song?>(null) }
    var showCreatePlaylistFromMenu by remember { mutableStateOf(false) }

    val playlistsState by playlistViewModel.uiState.collectAsState()
    val userPlaylists = (playlistsState as? com.aurastream.mobile.ui.playlists.PlaylistsUiState.Success)?.playlists ?: emptyList()

    val navItems = listOf(NavScreen.Home, NavScreen.Search, NavScreen.Playlists)
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            Column {
                // Persistent MiniPlayer
                if (currentSong != null) {
                    val progress = if (durationMs > 0) currentPositionMs.toFloat() / durationMs.toFloat() else 0f
                    MiniPlayer(
                        song = currentSong,
                        isPlaying = isPlaying,
                        progress = progress,
                        onPlayPauseClick = { audioPlayerManager.togglePlayPause() },
                        onClick = { showExpandedPlayer = true }
                    )
                }

                NavigationBar(containerColor = SpotifySurface) {
                    navItems.forEach { screen ->
                        NavigationBarItem(
                            icon = { Icon(screen.icon, contentDescription = screen.title) },
                            label = { Text(screen.title) },
                            selected = currentRoute == screen.route,
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = SpotifyGreen,
                                selectedTextColor = SpotifyGreen,
                                indicatorColor = SpotifyGreen.copy(alpha = 0.15f),
                                unselectedIconColor = TextSecondary,
                                unselectedTextColor = TextSecondary
                            ),
                            onClick = {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        },
        containerColor = SpotifyDark
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            NavHost(navController = navController, startDestination = NavScreen.Home.route) {
                composable(NavScreen.Home.route) {
                    HomeScreen(
                        viewModel = homeViewModel,
                        currentPlayingSong = currentSong,
                        onSongClick = { song, queue -> audioPlayerManager.playSong(song, queue) },
                        onSongOptionClick = { song -> selectedSongForPlaylistMenu = song },
                        onPlaylistClick = { playlist ->
                            navController.navigate("playlist_detail/${playlist.id}")
                        }
                    )
                }

                composable(NavScreen.Search.route) {
                    SearchScreen(
                        viewModel = searchViewModel,
                        currentPlayingSong = currentSong,
                        onSongClick = { song, queue -> audioPlayerManager.playSong(song, queue) },
                        onSongOptionClick = { song -> selectedSongForPlaylistMenu = song }
                    )
                }

                composable(NavScreen.Playlists.route) {
                    PlaylistsScreen(
                        viewModel = playlistViewModel,
                        onPlaylistClick = { playlist ->
                            navController.navigate("playlist_detail/${playlist.id}")
                        }
                    )
                }

                composable(
                    route = "playlist_detail/{playlistId}",
                    arguments = listOf(navArgument("playlistId") { type = NavType.LongType })
                ) { backStackEntry ->
                    val playlistId = backStackEntry.arguments?.getLong("playlistId") ?: 0L
                    PlaylistDetailScreen(
                        playlistId = playlistId,
                        viewModel = playlistViewModel,
                        currentPlayingSong = currentSong,
                        onBackClick = { navController.popBackStack() },
                        onSongClick = { song, queue -> audioPlayerManager.playSong(song, queue) },
                        onSongOptionClick = { song -> selectedSongForPlaylistMenu = song }
                    )
                }
            }
        }

        // Expanded Player Modal
        if (showExpandedPlayer && currentSong != null) {
            ExpandedPlayerModal(
                song = currentSong!!,
                isPlaying = isPlaying,
                currentPositionMs = currentPositionMs,
                durationMs = durationMs,
                isShuffle = isShuffle,
                isRepeat = isRepeat,
                onPlayPauseClick = { audioPlayerManager.togglePlayPause() },
                onNextClick = { audioPlayerManager.playNext() },
                onPrevClick = { audioPlayerManager.playPrevious() },
                onSeekTo = { pos -> audioPlayerManager.seekTo(pos) },
                onToggleShuffle = { audioPlayerManager.toggleShuffle() },
                onToggleRepeat = { audioPlayerManager.toggleRepeat() },
                onDismissRequest = { showExpandedPlayer = false },
                onOptionClick = { song ->
                    showExpandedPlayer = false
                    selectedSongForPlaylistMenu = song
                }
            )
        }

        // 3-Dot Menu -> Add to Playlist BottomSheet
        selectedSongForPlaylistMenu?.let { song ->
            AddToPlaylistBottomSheet(
                song = song,
                playlists = userPlaylists,
                onDismissRequest = { selectedSongForPlaylistMenu = null },
                onPlaylistSelected = { playlist, targetSong ->
                    playlistViewModel.addSongToPlaylist(playlist.id, targetSong.id) { _, _ ->
                        selectedSongForPlaylistMenu = null
                    }
                },
                onCreatePlaylistClick = {
                    selectedSongForPlaylistMenu = null
                    showCreatePlaylistFromMenu = true
                }
            )
        }

        // Quick Create Playlist from menu
        if (showCreatePlaylistFromMenu) {
            CreatePlaylistDialog(
                onDismiss = { showCreatePlaylistFromMenu = false },
                onCreate = { name, desc ->
                    playlistViewModel.createNewPlaylist(name, desc) { _, _ ->
                        showCreatePlaylistFromMenu = false
                    }
                }
            )
        }
    }
}
