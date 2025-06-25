package com.social.kafka.command;

public record CreateProfileCommand(String profileId, String username, String fullName) {
}
