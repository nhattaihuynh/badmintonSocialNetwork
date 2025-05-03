package com.social.profile_service.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record ProfileResponse(
    UUID id,
    String username,
    String fullName,
    String phoneNumber,
    String bio,
    String profilePictureUrl,
    String coverPictureUrl,
    String skillLevel,
    String preferredPlayStyle,
    Integer yearsOfExperience,
    String preferredLocation,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,

    // Derived fields that will be populated by MapStruct
    String formattedCreatedAt,
    String formattedUpdatedAt
) {}
