package com.aurastream.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlaylistItemDto {
    private Long id;
    private SongDto song;
    private LocalDateTime addedAt;
    private Integer position;
}
