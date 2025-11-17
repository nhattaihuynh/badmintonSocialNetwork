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
    val profileRepository: ProfileRepository,
    val neo4jTemplate: Neo4jTemplate
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
     * Send a friend request from one profile to another
     *
     * @param sourceProfileId The profile ID sending the request
     * @param targetProfileId The profile ID receiving the request
     * @return True if the friend request was sent successfully
     */
    @Transactional
    fun sendFriendRequest(sourceProfileId: String, targetProfileId: String): Boolean {
        logger.info("Sending friend request from {} to {}", sourceProfileId, targetProfileId)

        if (sourceProfileId == targetProfileId) {
            logger.warn("Cannot send friend request to self: {}", sourceProfileId)
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

        // Check if request already exists
        if (sourceProfile.sentFriendRequest.any { it.profileId == targetProfileId }) {
            logger.info("Friend request already sent from {} to {}", sourceProfileId, targetProfileId)
            return true
        }

        // Add request relationship
        sourceProfile.sentFriendRequest.add(targetProfile)
        targetProfile.receivedFriendRequest.add(sourceProfile)

        profileRepository.save(sourceProfile)
        profileRepository.save(targetProfile)

        logger.info("Friend request sent successfully from {} to {}", sourceProfileId, targetProfileId)
        return true
    }

    /**
     * Accept a friend request from another profile
     *
     * @param profileId The profile ID accepting the request
     * @param friendProfileId The profile ID whose request is being accepted
     * @return True if the friend request was accepted successfully
     */
    @Transactional
    fun acceptFriendRequest(profileId: String, friendProfileId: String): Boolean {
        logger.info("Accepting friend request from {} to {}", friendProfileId, profileId)

        val profileOpt = profileRepository.findByProfileId(profileId)
        val friendProfileOpt = profileRepository.findByProfileId(friendProfileId)

        if (profileOpt.isEmpty || friendProfileOpt.isEmpty) {
            logger.warn("One or both profiles not found. Profile: {}, Friend: {}",
                profileOpt.isPresent, friendProfileOpt.isPresent)
            return false
        }

        val profile = profileOpt.get()
        val friendProfile = friendProfileOpt.get()

        // Check if request exists
        if (!profile.receivedFriendRequest.any { it.profileId == friendProfileId }) {
            logger.warn("No friend request from {} to {}", friendProfileId, profileId)
            return false
        }

        // Remove request relationships
        profile.receivedFriendRequest.removeIf { it.profileId == friendProfileId }
        friendProfile.sentFriendRequest.removeIf { it.profileId == profileId }

        // Create friendship (bidirectional)
        profile.friends.add(friendProfile)
        friendProfile.friends.add(profile)

        profileRepository.save(profile)
        profileRepository.save(friendProfile)

        logger.info("Friend request accepted from {} to {}", friendProfileId, profileId)
        return true
    }

    /**
     * Get all incoming friend requests for a profile
     *
     * @param profileId The profile ID to get incoming requests for
     * @return List of profiles that sent friend requests to this profile
     */
    fun getIncomingFriendRequests(profileId: String): List<Profile> {
        logger.info("Getting incoming friend requests for profile: {}", profileId)

        val profileOpt = profileRepository.findByProfileId(profileId)

        if (profileOpt.isEmpty) {
            logger.warn("Profile not found: {}", profileId)
            return emptyList()
        }

        return profileOpt.get().receivedFriendRequest.toList().also {
            logger.info("Found {} incoming friend requests for profile {}", it.size, profileId)
        }
    }

    /**
     * Get mutual friends between two profiles
     *
     * @param profileId1 The first profile ID
     * @param profileId2 The second profile ID
     * @return List of profiles that are friends with both users
     */
    fun getMutualFriends(profileId1: String, profileId2: String): List<Profile> {
        logger.info("Getting mutual friends between profiles: {} and {}", profileId1, profileId2)

        val profile1Opt = profileRepository.findByProfileId(profileId1)
        val profile2Opt = profileRepository.findByProfileId(profileId2)

        if (profile1Opt.isEmpty || profile2Opt.isEmpty) {
            logger.warn("One or both profiles not found. Profile1: {}, Profile2: {}",
                profile1Opt.isPresent, profile2Opt.isPresent)
            return emptyList()
        }

        val profile1 = profile1Opt.get()
        val profile2 = profile2Opt.get()

        val profile1Friends = profile1.friends.map { it.profileId }.toSet()
        val profile2Friends = profile2.friends.map { it.profileId }.toSet()

        val mutualFriendsProfileIds = profile1Friends.intersect(profile2Friends)
        val mutualFriends = profile1.friends.filter { it.profileId in mutualFriendsProfileIds }

        logger.info("Found {} mutual friends between {} and {}", mutualFriends.size, profileId1, profileId2)
        return mutualFriends
    }

     /*
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