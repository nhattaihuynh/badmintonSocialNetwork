package com.social.kafka.event;

public record NewProfileEvent(String profileId, String username, String fullName) {
}
