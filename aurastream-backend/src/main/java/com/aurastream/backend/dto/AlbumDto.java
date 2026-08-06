package com.aurastream.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlbumDto {
    private Long id;
    private String title;
    private Long artistId;
    private String artistName;
    private String coverUrl;
    private Integer releaseYear;
}
