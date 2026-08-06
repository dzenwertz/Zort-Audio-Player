package com.aurastream.mobile.ui.player

import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.aurastream.mobile.domain.model.Song
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AudioPlayerManager(context: Context) {

    private val player: ExoPlayer = ExoPlayer.Builder(context.applicationContext).build()

    private val _currentSong = MutableStateFlow<Song?>(null)
    val currentSong: StateFlow<Song?> = _currentSong.asStateFlow()

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    private val _currentPositionMs = MutableStateFlow(0L)
    val currentPositionMs: StateFlow<Long> = _currentPositionMs.asStateFlow()

    private val _durationMs = MutableStateFlow(0L)
    val durationMs: StateFlow<Long> = _durationMs.asStateFlow()

    private val _isShuffle = MutableStateFlow(false)
    val isShuffle: StateFlow<Boolean> = _isShuffle.asStateFlow()

    private val _isRepeat = MutableStateFlow(false)
    val isRepeat: StateFlow<Boolean> = _isRepeat.asStateFlow()

    private val _playlistQueue = MutableStateFlow<List<Song>>(emptyList())
    private var currentIndex = -1

    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    private var positionUpdateJob: Job? = null

    init {
        player.addListener(object : Player.Listener {
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                _isPlaying.value = isPlaying
                if (isPlaying) {
                    startPositionUpdates()
                } else {
                    stopPositionUpdates()
                }
            }

            override fun onPlaybackStateChanged(playbackState: Int) {
                if (playbackState == Player.STATE_READY) {
                    _durationMs.value = player.duration.coerceAtLeast(0L)
                } else if (playbackState == Player.STATE_ENDED) {
                    playNext()
                }
            }
        })
    }

    fun playSong(song: Song, queue: List<Song> = listOf(song)) {
        _playlistQueue.value = queue
        currentIndex = queue.indexOfFirst { it.id == song.id }.coerceAtLeast(0)
        _currentSong.value = song

        val mediaItem = MediaItem.fromUri(song.audioUrl)
        player.setMediaItem(mediaItem)
        player.prepare()
        player.play()
    }

    fun togglePlayPause() {
        if (player.isPlaying) {
            player.pause()
        } else {
            if (_currentSong.value != null) {
                player.play()
            }
        }
    }

    fun seekTo(positionMs: Long) {
        player.seekTo(positionMs)
        _currentPositionMs.value = positionMs
    }

    fun playNext() {
        val queue = _playlistQueue.value
        if (queue.isEmpty()) return
        currentIndex = if (_isShuffle.value) {
            (queue.indices).random()
        } else {
            (currentIndex + 1) % queue.size
        }
        playSong(queue[currentIndex], queue)
    }

    fun playPrevious() {
        val queue = _playlistQueue.value
        if (queue.isEmpty()) return
        currentIndex = if (currentIndex - 1 < 0) queue.size - 1 else currentIndex - 1
        playSong(queue[currentIndex], queue)
    }

    fun toggleShuffle() {
        _isShuffle.value = !_isShuffle.value
    }

    fun toggleRepeat() {
        _isRepeat.value = !_isRepeat.value
    }

    private fun startPositionUpdates() {
        positionUpdateJob?.cancel()
        positionUpdateJob = scope.launch {
            while (isActive && player.isPlaying) {
                _currentPositionMs.value = player.currentPosition.coerceAtLeast(0L)
                delay(500)
            }
        }
    }

    private fun stopPositionUpdates() {
        positionUpdateJob?.cancel()
    }

    fun release() {
        stopPositionUpdates()
        player.release()
    }
}
