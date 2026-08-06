package com.aurastream.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SongDto {
    private Long id;
    private String title;
    private Long artistId;
    private String artistName;
    private Long albumId;
    private String albumTitle;
    private String coverUrl;
    private Integer durationSeconds;
    private String genre;
    private String audioUrl;
    private Long playCount;
    private Integer bpm;
}
