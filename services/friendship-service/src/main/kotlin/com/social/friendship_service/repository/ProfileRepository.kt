package com.social.friendship_service.repository

import com.social.friendship_service.nodes.Profile
import org.springframework.data.neo4j.repository.Neo4jRepository
import org.springframework.stereotype.Repository
import java.util.Optional
import java.util.UUID

@Repository
interface ProfileRepository : Neo4jRepository<Profile, UUID> {
    
    /**
     * Find a profile by its profileId
     * 
     * @param profileId The unique identifier of the profile
     * @return Optional containing the profile if found
     */
    fun findByProfileId(profileId: String): Optional<Profile>
    
    /**
     * Find a profile by its username
     * 
     * @param username The username to search for
     * @return Optional containing the profile if found
     */
    fun findByUsername(username: String): Optional<Profile>
}
