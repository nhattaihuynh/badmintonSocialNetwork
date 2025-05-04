package com.social.profile_service.controller;

import com.social.profile_service.dto.ProfileRequest;
import com.social.profile_service.dto.ProfileResponse;
import com.social.profile_service.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {
    private final ProfileService profileService;

    @GetMapping("/{id}")
    public ResponseEntity<ProfileResponse> getProfileById(@PathVariable UUID id) {
        return ResponseEntity.ok(profileService.getProfileResponseById(id));
    }

    @PostMapping
    public ResponseEntity<ProfileResponse> createProfile(@RequestBody ProfileRequest profileRequest) {
        ProfileResponse createdProfile = profileService.createProfileFromRequest(profileRequest);
        return new ResponseEntity<>(createdProfile, HttpStatus.CREATED);
    }
}
