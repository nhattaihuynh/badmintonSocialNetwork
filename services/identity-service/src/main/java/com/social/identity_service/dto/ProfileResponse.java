package com.social.identity_service.dto;

import java.util.UUID;

public record ProfileResponse(
        UUID id,
        String username,
        String fullName,
        String phoneNumber,
        String bio,
        String profilePictureUrl,
        String coverPictureUrl,
        String preferredLocation,

        // TODO
        Integer totalFriend,
        String formattedCreatedAt,
        String formattedUpdatedAt
) {
}
