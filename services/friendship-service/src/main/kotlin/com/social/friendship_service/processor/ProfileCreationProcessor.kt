package com.social.friendship_service.processor

import com.social.friendship_service.nodes.Profile
import com.social.friendship_service.service.FriendshipService
import com.social.kafka.command.CreateProfileCommand
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class ProfileCreationProcessor(
    private val friendshipService: FriendshipService
) {
    private val logger = LoggerFactory.getLogger(ProfileCreationProcessor::class.java)

    @KafkaListener(topics = ["profile-create"], groupId = "profile-create-group")
    fun consumeProfileCreation(event: CreateProfileCommand) {
        logger.info(
            "Received profile creation event: userId={}, userName={}, fullName={}",
            event.profileId(), event.username(), event.fullName()
        )
        try {
            val profile = friendshipService.createProfileFromEvent(event)
            logger.info("Profile created successfully with id: {}", profile.id)
        } catch (e: Exception) {
            logger.error("Error processing profile creation event: ${e.message}", e)
        }
    }
}