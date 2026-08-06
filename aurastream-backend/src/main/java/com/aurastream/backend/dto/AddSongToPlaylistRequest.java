package com.aurastream.backend.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddSongToPlaylistRequest {

    @NotNull(message = "Song ID is required")
    private Long songId;
}
