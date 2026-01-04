package com.social.identity_service.dto;

public record ProfileRequest(
        String username,
        String fullName,
        String phoneNumber,
        String bio,
        String profilePictureUrl,
        String coverPictureUrl,
        String location
) {
}
