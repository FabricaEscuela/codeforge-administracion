package com.codefactory.dev_social_network.usuarios.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codefactory.dev_social_network.shared.config.CurrentUserPort;
import com.codefactory.dev_social_network.usuarios.dto.UserProfileEditionRequestDTO;
import com.codefactory.dev_social_network.usuarios.dto.UserProfileResponseDTO;
import com.codefactory.dev_social_network.usuarios.interfaces.UserProfileService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/profile")
public class UserProfileController {

    private final UserProfileService userProfileService;
    private final CurrentUserPort currentUserPort;

    public UserProfileController(UserProfileService userProfileService, CurrentUserPort currentUserPort) {
        this.userProfileService = userProfileService;
        this.currentUserPort = currentUserPort;
    }

    @GetMapping
    public ResponseEntity<UserProfileResponseDTO> getMyProfile() {
        return ResponseEntity.ok(userProfileService.getUserProfile(currentUserPort.currentUserId()));
    }

    @PutMapping
    public ResponseEntity<UserProfileResponseDTO> updateMyProfile(
            @Valid @RequestBody UserProfileEditionRequestDTO request) {
        return ResponseEntity.ok(userProfileService.updateUserProfile(currentUserPort.currentUserId(), request));
    }
}