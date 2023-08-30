package com.example.controller;

import com.example.dto.UpdateVideoDTO;
import com.example.dto.VideoDTO;
import com.example.service.VideoService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/video")
public class VideoController {

    @Autowired
    private VideoService videoService;

    @PostMapping("")
    public ResponseEntity<?> create(@Valid @RequestBody VideoDTO videoDTO) {
        return ResponseEntity.ok(videoService.create(videoDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable("id") String videoId,
                                    @Valid @RequestBody UpdateVideoDTO updateVideoDTO) {
        return ResponseEntity.ok(videoService.update(videoId, updateVideoDTO));
    }

    @PutMapping("/change/{id}/status")
    public ResponseEntity<?> changeStatus(@PathVariable("id") String videoId,
                                          @RequestParam AccessLevel status) {
        return ResponseEntity.ok(videoService.changeStatus(videoId, status));
    }

    @PutMapping("/increase/{id}")
    public ResponseEntity<?> increaseViewCount(@PathVariable("id") String videoId) {
        return ResponseEntity.ok(videoService.increaseViewCount(videoId));
    }

    @GetMapping("/get/by/cat/{id}")
    public ResponseEntity<?> getPaginationByCategory(@PathVariable("id") Integer categoryId,
                                                     @RequestParam(value = "page", defaultValue = "1") int page,
                                                     @RequestParam(value = "size", defaultValue = "10") int size) {
        return ResponseEntity.ok(videoService.getPaginationByCategory(categoryId, page - 1, size));
    }

    @GetMapping("get/search")
    public ResponseEntity<?> search(@RequestParam("title") String title) {
        return ResponseEntity.ok(videoService.search(title));
    }

    @GetMapping("/get/by/tag/{id}")
    public ResponseEntity<?> getByTag(@PathVariable("id") Integer tagId,
                                      @RequestParam(value = "page", defaultValue = "1") int page,
                                      @RequestParam(value = "size", defaultValue = "10") int size) {
        return ResponseEntity.ok(videoService.getByTag(tagId, page - 1, size));
    }

    @GetMapping("/full/get/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") String videoId) {
        return ResponseEntity.ok(videoService.getById(videoId));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("page/get")
    public ResponseEntity<?> getPagination(@RequestParam(value = "page", defaultValue = "1") int page,
                                           @RequestParam(value = "size", defaultValue = "10") int size) {
        return ResponseEntity.ok(videoService.getPagination(page - 1, size));
    }

    @GetMapping("/get/channel/{id}")
    public ResponseEntity<?> getPaginationByChannel(@PathVariable("id") String channelId,
                                                    @RequestParam(value = "page", defaultValue = "1") int page,
                                                    @RequestParam(value = "size", defaultValue = "10") int size) {
        return ResponseEntity.ok(videoService.getChannelVideoPagination(channelId,page,size));
    }
}

