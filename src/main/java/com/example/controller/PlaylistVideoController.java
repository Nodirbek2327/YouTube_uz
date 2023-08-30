package com.example.controller;

import com.example.dto.ApiResponseDTO;
import com.example.dto.PlaylistVideoDTO;
import com.example.mapper.PlaylistVideoMapper;
import com.example.service.PlaylistVideoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/playlistVideo")
public class PlaylistVideoController {
    @Autowired
    private PlaylistVideoService playlistVideoService;
    // 1. Create (User and Owner)
    @PostMapping("")
    public ResponseEntity<ApiResponseDTO> create(@Valid @RequestBody PlaylistVideoDTO videoDTO){
        return ResponseEntity.ok(playlistVideoService.create(videoDTO));
    }
//      2. Update
    @PutMapping("")
    public ResponseEntity<ApiResponseDTO>update(@Valid @RequestBody PlaylistVideoDTO videoDTO){
        return ResponseEntity.ok(playlistVideoService.update(videoDTO));
    }
    // 3. Delete PlayListVideo
    @DeleteMapping("")
    public ResponseEntity<ApiResponseDTO>delete(@RequestParam("playlistId") String playlistId,
                                                @RequestParam("videoId") String videoId){
        return ResponseEntity.ok(playlistVideoService.delete(playlistId,videoId));
    }
    // 4. Get Video list by playListId (video status published)
    @GetMapping("/{playlistId}")
    public ResponseEntity<List<PlaylistVideoMapper>>getById(@PathVariable String playlistId){
        return ResponseEntity.ok(playlistVideoService.getByPlaylistId(playlistId));
    }
}
