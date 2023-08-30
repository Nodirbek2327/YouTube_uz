package com.example.controller;

import com.example.dto.TagDTO;
import com.example.entity.TagEntity;
import com.example.service.TagService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/tag")
public class TagController {

    @Autowired
    private TagService tagService;

    @PostMapping("")
    public ResponseEntity<TagDTO> create(@Valid @RequestBody TagDTO tagDTO) {
        return ResponseEntity.ok().body(tagService.create(tagDTO));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(value = "/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Integer id,
                                    @RequestBody TagDTO tagDTO) {
        return ResponseEntity.ok(tagService.update(id, tagDTO));
    }


    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(tagService.delete(id));
    }

    @GetMapping(value = "/get")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(tagService.getList());
    }


}
