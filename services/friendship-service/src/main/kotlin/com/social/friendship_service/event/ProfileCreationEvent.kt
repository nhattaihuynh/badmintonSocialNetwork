package com.social.friendship_service.event

import com.fasterxml.jackson.annotation.JsonProperty

data class ProfileCreationEvent(
    @JsonProperty("profileId")
    val profileId: String,

    @JsonProperty("username")
    val username: String,

    @JsonProperty("fullName")
    val fullName: String
)