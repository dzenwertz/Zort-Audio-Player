package com.aurastream.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArtistDto {
    private Long id;
    private String name;
    private String genre;
    private String bio;
    private String imageUrl;
}
