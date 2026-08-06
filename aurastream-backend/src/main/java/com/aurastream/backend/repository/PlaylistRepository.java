package com.aurastream.backend.repository;

import com.aurastream.backend.model.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlaylistRepository extends JpaRepository<Playlist, Long> {
    Optional<Playlist> findByNameIgnoreCase(String name);
}
