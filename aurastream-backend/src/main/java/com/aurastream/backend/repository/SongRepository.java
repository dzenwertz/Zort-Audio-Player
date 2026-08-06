package com.aurastream.backend.repository;

import com.aurastream.backend.model.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SongRepository extends JpaRepository<Song, Long> {

    @Query("SELECT s FROM Song s WHERE LOWER(s.title) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(s.artist.name) LIKE LOWER(CONCAT('%', :query, '%')) OR (s.album IS NOT NULL AND LOWER(s.album.title) LIKE LOWER(CONCAT('%', :query, '%')))")
    List<Song> searchSongs(@Param("query") String query);

    List<Song> findTop10ByOrderByPlayCountDesc();

    List<Song> findByGenreIgnoreCase(String genre);
}
