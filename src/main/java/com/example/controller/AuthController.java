package com.example.controller;

import com.example.dto.ApiResponseDTO;
import com.example.dto.AuthDTO;
import com.example.dto.ProfileDTO;
import com.example.enums.Language;
import com.example.service.AuthService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    @Autowired
    private AuthService authService;
    //1. Registration (with email verification)
    //	  id,name,surname,email,main_photo (url)
    @PostMapping("/registration")
    public ResponseEntity<ApiResponseDTO> registration(@Valid @RequestBody ProfileDTO profileDTO){
    return ResponseEntity.ok(authService.registration(profileDTO));
    }
    //email verification
    @GetMapping("/verification/email/{jwt}")
    public ResponseEntity<String>emailVerification(@PathVariable String jwt){
    return ResponseEntity.ok(authService.emailVerification(jwt));
    }

//    2. Authorization
    @PostMapping("/login")
    public ResponseEntity<ApiResponseDTO>login(@Valid @RequestBody AuthDTO authDTO,
                                               @RequestParam("lang")Language language){
        return ResponseEntity.ok(authService.login(authDTO,language));
    }

}
