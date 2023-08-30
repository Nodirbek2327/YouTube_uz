package com.example.controller;

import com.example.dto.EmailHistoryDTO;
import com.example.dto.FilterEmailHistoryDTO;
import com.example.service.EmailHistoryService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/emailHistory")
public class EmailHistoryController {
    @Autowired
    private EmailHistoryService emailHistoryService;

    //   1. Get email pagination (ADMIN)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/getAll")
    public ResponseEntity<PageImpl<EmailHistoryDTO>> getAllByPagination(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                                                        @RequestParam(value = "size", defaultValue = "10") Integer size) {
        return ResponseEntity.ok(emailHistoryService.getAllByPagination(page - 1, size));
    }

    //2. Get Email pagination by email
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/getByEmail")
    public ResponseEntity<PageImpl<EmailHistoryDTO>> getByEmail(@RequestParam("email") String email,
                                                                @RequestParam(value = "page", defaultValue = "1") Integer page,
                                                                @RequestParam(value = "size", defaultValue = "10") Integer size) {
        return ResponseEntity.ok(emailHistoryService.getByEmail(email, page - 1, size));
    }

    //3. filter (email,created_date) + with pagination
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/filterPagination")
    public ResponseEntity<PageImpl<EmailHistoryDTO>>
    filterPagination(@RequestBody FilterEmailHistoryDTO dto,
                     @RequestParam(value = "page", defaultValue = "1") Integer page,
                     @RequestParam(value = "size", defaultValue = "10") Integer size) {
        return ResponseEntity.ok(emailHistoryService.filterByPagination(dto, page - 1, size));

    }
}
