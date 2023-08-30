package com.example.controller;

import com.example.config.CustomUserDetails;
import com.example.dto.AttachDTO;
import com.example.service.AttachService;
import com.example.util.SpringSecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/attach")
public class AttachController {
    @Autowired
    private AttachService attachService;

    @PostMapping("/upload")
    public ResponseEntity<AttachDTO> upload(@RequestParam("file") MultipartFile file) {
        CustomUserDetails userDetails = SpringSecurityUtil.getCurrentUser();
        return ResponseEntity.ok().body(attachService.save(file, userDetails.getProfile().getId()));
    }

    @GetMapping(value = "/open/{id}/img", produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] openImageById(@PathVariable("id") String id) {
        return attachService.loadImageById(id);
    }

    @GetMapping("/open/download/{id}")
    public ResponseEntity<Resource> download(@PathVariable("id") String id) {
        return attachService.download(id);
    }

    @GetMapping(value = "/get/pageImp")
    public ResponseEntity<PageImpl<AttachDTO>> pagination(@RequestParam("page") int page,
                                                          @RequestParam("size") int size) {
        return ResponseEntity.ok(attachService.pagination(page - 1, size));
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable("id") String id) {
        return ResponseEntity.ok(attachService.delete(id));
    }

    @GetMapping(value = "/open/{id}/general", produces = MediaType.ALL_VALUE)
    public byte[] openByIdGeneral(@PathVariable("id") String id) {
        return attachService.loadByIdGeneral(id);
    }

}
