package com.social.friendship_service.service

import com.social.friendship_service.nodes.Profile
import com.social.friendship_service.repository.ProfileRepository
import com.social.kafka.command.CreateProfileCommand
import org.springframework.stereotype.Service
import org.slf4j.LoggerFactory
import org.springframework.data.neo4j.core.Neo4jTemplate
import org.springframework.transaction.annotation.Transactional
import java.util.Optional

@Service
class FriendshipService(
    private val profileRepository: ProfileRepository,
    private val neo4jTemplate: Neo4jTemplate
) {

    private val logger = LoggerFactory.getLogger(FriendshipService::class.java)

    /**
     * Creates a new Profile node in Neo4j from a ProfileCreationEvent
     * 
     * @param event The ProfileCreationEvent containing profile information
     * @return The created Profile entity
     */
    fun createProfileFromEvent(event: CreateProfileCommand): Profile {
        logger.info("Creating profile node for profileId: {}", event.profileId())
        
        val profile = Profile(
            profileId = event.profileId,
            username = event.username,
            fullName = event.fullName
        )
        
        return profileRepository.save(profile).also {
            logger.info("Profile node created successfully with id: {}", it.id)
        }
    }
    
    /**
     * Creates a friendship relationship between two profiles
     * 
     * @param sourceProfileId The profile ID of the user initiating the friendship
     * @param targetProfileId The profile ID of the user accepting the friendship
     * @return True if the friendship was created successfully, false otherwise
     */
    @Transactional
    fun createFriendship(sourceProfileId: String, targetProfileId: String): Boolean {
        logger.info("Creating friendship between profiles: {} and {}", sourceProfileId, targetProfileId)
        
        if (sourceProfileId == targetProfileId) {
            logger.warn("Cannot create friendship with self: {}", sourceProfileId)
            return false
        }
        
        val sourceProfileOpt = profileRepository.findByProfileId(sourceProfileId)
        val targetProfileOpt = profileRepository.findByProfileId(targetProfileId)
        
        if (sourceProfileOpt.isEmpty || targetProfileOpt.isEmpty) {
            logger.warn("One or both profiles not found. Source: {}, Target: {}", 
                sourceProfileOpt.isPresent, targetProfileOpt.isPresent)
            return false
        }
        
        val sourceProfile = sourceProfileOpt.get()
        val targetProfile = targetProfileOpt.get()
        
        // Check if friendship already exists
        if (sourceProfile.friends.any { it.profileId == targetProfileId }) {
            logger.info("Friendship already exists between {} and {}", sourceProfileId, targetProfileId)
            return true
        }
        
        // Add friendship in both directions for bidirectional relationship
        sourceProfile.friends.add(targetProfile)
        targetProfile.friends.add(sourceProfile)
        
        profileRepository.save(sourceProfile)
        profileRepository.save(targetProfile)
        
        logger.info("Friendship created successfully between {} and {}", sourceProfileId, targetProfileId)
        return true
    }
    
    /**
     * Gets all friends of a profile
     * 
     * @param profileId The profile ID to get friends for
     * @return List of profiles that are friends with the given profile
     */
    fun getFriends(profileId: String): List<Profile> {
        logger.info("Getting friends for profile: {}", profileId)
        
        val profileOpt = profileRepository.findByProfileId(profileId)
        
        if (profileOpt.isEmpty) {
            logger.warn("Profile not found: {}", profileId)
            return emptyList()
        }
        
        return profileOpt.get().friends.toList().also {
            logger.info("Found {} friends for profile {}", it.size, profileId)
        }
    }
    
    /**
     * Removes a friendship relationship between two profiles
     * 
     * @param sourceProfileId The profile ID of the user removing the friendship
     * @param targetProfileId The profile ID of the user being removed as friend
     * @return True if the friendship was removed successfully, false otherwise
     */
    @Transactional
    fun removeFriendship(sourceProfileId: String, targetProfileId: String): Boolean {
        logger.info("Removing friendship between profiles: {} and {}", sourceProfileId, targetProfileId)
        
        val sourceProfileOpt = profileRepository.findByProfileId(sourceProfileId)
        val targetProfileOpt = profileRepository.findByProfileId(targetProfileId)
        
        if (sourceProfileOpt.isEmpty || targetProfileOpt.isEmpty) {
            logger.warn("One or both profiles not found. Source: {}, Target: {}", 
                sourceProfileOpt.isPresent, targetProfileOpt.isPresent)
            return false
        }
        
        val sourceProfile = sourceProfileOpt.get()
        val targetProfile = targetProfileOpt.get()
        
        // Remove friendship in both directions
        val sourceRemoved = sourceProfile.friends.removeIf { it.profileId == targetProfileId }
        val targetRemoved = targetProfile.friends.removeIf { it.profileId == sourceProfileId }
        
        if (!sourceRemoved && !targetRemoved) {
            logger.info("No friendship exists between {} and {}", sourceProfileId, targetProfileId)
            return false
        }
        
        profileRepository.save(sourceProfile)
        profileRepository.save(targetProfile)
        
        logger.info("Friendship removed successfully between {} and {}", sourceProfileId, targetProfileId)
        return true
    }
}