package com.aurastream.backend.repository;

import com.aurastream.backend.model.PlaylistItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlaylistItemRepository extends JpaRepository<PlaylistItem, Long> {
    Optional<PlaylistItem> findByPlaylistIdAndSongId(Long playlistId, Long songId);
    void deleteByPlaylistIdAndSongId(Long playlistId, Long songId);
}
