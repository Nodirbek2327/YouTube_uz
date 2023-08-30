package com.example.controller;

import com.example.config.CustomUserDetails;
import com.example.dto.ProfileDTO;
import com.example.service.ProfileService;
import com.example.util.SpringSecurityUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/profile")
public class ProfileController {
    @Autowired
    private ProfileService profileService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = {"/create"})
    public ResponseEntity<ProfileDTO> create(@Valid @RequestBody ProfileDTO dto) {
        return ResponseEntity.ok(profileService.create(dto, SpringSecurityUtil.getProfileId()));
    }

    @GetMapping(value = "/getDetail")
    public ResponseEntity<ProfileDTO> getDetail(){
        return ResponseEntity.ok(profileService.getProfileDetail());
    }

    @PutMapping( value = "/updateMainPhoto")
    public ResponseEntity<ProfileDTO> updateMainPhoto(@RequestParam("photoId") String photoId){
        return ResponseEntity.ok(profileService.updateMainPhoto(photoId, SpringSecurityUtil.getProfileId()));
    }

    @PutMapping(value = "/updateProfileDetail")
    public ResponseEntity<ProfileDTO> updateProfileDetail(@RequestParam("name") String name,
                                                          @RequestParam("surname") String surname){
        return ResponseEntity.ok(profileService.updateProfileDetail(name,surname, SpringSecurityUtil.getProfileId()));
    }

    @PutMapping(value = "/changePassword")
    public ResponseEntity<Boolean> changePassword(@RequestParam("password") String password){
        return ResponseEntity.ok(profileService.changePassword(password, SpringSecurityUtil.getProfileId()));
    }

    @PostMapping(value = "/updateEmail")
    public ResponseEntity<ProfileDTO> sendMSGtoEmail(@RequestParam("email") String email){
        return ResponseEntity.ok(profileService.sendMSGtoEmail(email, SpringSecurityUtil.getCurrentUser().getProfile()));
    }

    @GetMapping(value = "/updateEmail/{jwt}")
    public ResponseEntity<String> updateEmail(@PathVariable("jwt") String jwt){
        return ResponseEntity.ok(profileService.updateEmail(jwt));
    }

}
