package com.social.friendship_service.service

import com.social.friendship_service.event.ProfileCreationEvent
import com.social.friendship_service.nodes.Profile
import com.social.friendship_service.repository.ProfileRepository
import org.springframework.stereotype.Service
import org.slf4j.LoggerFactory

@Service
class FriendshipService(private val profileRepository: ProfileRepository) {

    private val logger = LoggerFactory.getLogger(FriendshipService::class.java)

    /**
     * Creates a new Profile node in Neo4j from a ProfileCreationEvent
     * 
     * @param event The ProfileCreationEvent containing profile information
     * @return The created Profile entity
     */
    fun createProfileFromEvent(event: ProfileCreationEvent): Profile {
        logger.info("Creating profile node for profileId: {}", event.profileId)
        
        val profile = Profile(
            profileId = event.profileId,
            username = event.username,
            fullName = event.fullName
        )
        
        return profileRepository.save(profile).also {
            logger.info("Profile node created successfully with id: {}", it.id)
        }
    }
}