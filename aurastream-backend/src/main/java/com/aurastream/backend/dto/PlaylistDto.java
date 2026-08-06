package com.aurastream.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlaylistDto {
    private Long id;
    private String name;
    private String description;
    private String coverUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<PlaylistItemDto> items;
    private int totalSongs;
}
