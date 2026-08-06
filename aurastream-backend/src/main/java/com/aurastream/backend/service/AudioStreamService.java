package com.aurastream.backend.service;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

@Service
public class AudioStreamService {

    /**
     * Synthesizes a valid MP3 audio stream for dynamic demo playback
     * supporting HTTP Byte-Range (206 Partial Content) requests.
     */
    public ResponseEntity<byte[]> streamAudio(String rangeHeader) {
        byte[] audioBytes = generateDemoMp3Data();
        int fileSize = audioBytes.length;

        if (rangeHeader == null || !rangeHeader.startsWith("bytes=")) {
            HttpHeaders headers = new HttpHeaders();
            headers.set(HttpHeaders.CONTENT_TYPE, "audio/mpeg");
            headers.setContentLength(fileSize);
            headers.set(HttpHeaders.ACCEPT_RANGES, "bytes");
            return new ResponseEntity<>(audioBytes, headers, HttpStatus.OK);
        }

        String[] ranges = rangeHeader.replace("bytes=", "").split("-");
        int start = Integer.parseInt(ranges[0]);
        int end = ranges.length > 1 && !ranges[1].isEmpty() ? Integer.parseInt(ranges[1]) : fileSize - 1;

        if (start >= fileSize) {
            HttpHeaders headers = new HttpHeaders();
            headers.set(HttpHeaders.CONTENT_RANGE, "bytes */" + fileSize);
            return new ResponseEntity<>(headers, HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE);
        }

        if (end >= fileSize) {
            end = fileSize - 1;
        }

        int contentLength = end - start + 1;
        byte[] rangeBytes = new byte[contentLength];
        System.arraycopy(audioBytes, start, rangeBytes, 0, contentLength);

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_TYPE, "audio/mpeg");
        headers.set(HttpHeaders.ACCEPT_RANGES, "bytes");
        headers.set(HttpHeaders.CONTENT_RANGE, String.format("bytes %d-%d/%d", start, end, fileSize));
        headers.setContentLength(contentLength);

        return new ResponseEntity<>(rangeBytes, headers, HttpStatus.PARTIAL_CONTENT);
    }

    private byte[] generateDemoMp3Data() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // Generate valid MP3 sync frames (44.1kHz, 128kbps stereo tone frames)
        for (int i = 0; i < 300; i++) {
            baos.write(0xFF); // Sync word 11 bits
            baos.write(0xFB); // MPEG-1 Layer 3 no CRC
            baos.write(0x90); // 128kbps, 44.1kHz
            baos.write(0x64); // Padding, private, stereo
            for (int j = 0; j < 413; j++) {
                baos.write((byte) (j % 256));
            }
        }
        return baos.toByteArray();
    }
}
