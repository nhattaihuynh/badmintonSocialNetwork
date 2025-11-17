package com.social.friendship_service.repository

import com.social.friendship_service.nodes.Profile
import org.springframework.data.neo4j.repository.Neo4jRepository
import org.springframework.data.neo4j.repository.query.Query
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


    // Finds all users who have sent a friend request to the given user
    @Query("""
        MATCH (u:User)-[:SENT_FRIEND_REQUEST]->(me:User {userId: ${'$'}myUserId})
        RETURN u
    """)
    fun findIncomingFriendRequests(myUserId: String): List<Profile>

    // Finds all users who are friends with the given user
    @Query("""
        MATCH (me:User {userId: ${'$'}myUserId})-[:IS_FRIENDS_WITH]-(f:User)
        RETURN f
    """)
    fun findFriends(myUserId: String): List<Profile>

    // Finds all mutual friends between two users
    @Query("""
        MATCH (u1:User {userId: ${'$'}userId1})-[:IS_FRIENDS_WITH]-(m:User)-[:IS_FRIENDS_WITH]-(u2:User {userId: ${'$'}userId2})
        RETURN m
    """)
    fun findMutualFriends(userId1: String, userId2: String): List<Profile>
}
