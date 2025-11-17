package com.social.friendship_service.controller

import com.social.friendship_service.service.FriendshipService
import org.springframework.http.ResponseEntity
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/friendship")
class FriendshipController(
    val friendshipService: FriendshipService
) {

    /**
     * Create a friend request from the current user to another user
     *
     * @param sourceProfileId The profile ID of the user sending the friend request
     * @param targetProfileId The profile ID of the user receiving the friend request
     * @return ResponseEntity with success message or error
     */
    @PostMapping("/request/send")
    fun sendFriendRequest(
        @RequestParam sourceProfileId: String,
        @RequestParam targetProfileId: String
    ): ResponseEntity<Map<String, Any>> {
        return try {
            val success = friendshipService.sendFriendRequest(sourceProfileId, targetProfileId)
            if (success) {
                ResponseEntity.ok(mapOf(
                    "success" to true,
                    "message" to "Friend request sent successfully"
                ))
            } else {
                ResponseEntity.badRequest().body(mapOf(
                    "success" to false,
                    "message" to "Failed to send friend request"
                ))
            }
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(mapOf(
                "success" to false,
                "message" to "Error: ${e.message}"
            ))
        }
    }

    /**
     * Accept a friend request from another user
     *
     * @param profileId The profile ID of the user accepting the request
     * @param friendProfileId The profile ID of the user whose request is being accepted
     * @return ResponseEntity with success message or error
     */
    @PostMapping("/request/accept")
    fun acceptFriendRequest(
        @RequestParam profileId: String,
        @RequestParam friendProfileId: String
    ): ResponseEntity<Map<String, Any>> {
        return try {
            val success = friendshipService.acceptFriendRequest(profileId, friendProfileId)
            if (success) {
                ResponseEntity.ok(mapOf(
                    "success" to true,
                    "message" to "Friend request accepted successfully"
                ))
            } else {
                ResponseEntity.badRequest().body(mapOf(
                    "success" to false,
                    "message" to "Failed to accept friend request"
                ))
            }
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(mapOf(
                "success" to false,
                "message" to "Error: ${e.message}"
            ))
        }
    }

    /**
     * List all incoming pending friend requests for a user
     *
     * @param profileId The profile ID of the user
     * @return ResponseEntity with list of pending friend requests
     */
    @GetMapping("/request/incoming")
    fun getIncomingFriendRequests(
        @RequestParam profileId: String
    ): ResponseEntity<Map<String, Any>> {
        return try {
            val pendingRequests = friendshipService.getIncomingFriendRequests(profileId)
            ResponseEntity.ok(mapOf(
                "success" to true,
                "count" to pendingRequests.size,
                "requests" to pendingRequests.map { profile ->
                    mapOf(
                        "id" to profile.id,
                        "profileId" to profile.profileId,
                        "username" to profile.username,
                        "fullName" to profile.fullName
                    )
                }
            ))
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(mapOf(
                "success" to false,
                "message" to "Error: ${e.message}"
            ))
        }
    }

    /**
     * Get all friends for a profile
     *
     * @param profileId The profile ID of the user
     * @return ResponseEntity with list of friends
     */
    @GetMapping("/friends")
    fun getAllFriends(
        @RequestParam profileId: String
    ): ResponseEntity<Map<String, Any>> {
        return try {
            val friends = friendshipService.getFriends(profileId)
            ResponseEntity.ok(mapOf(
                "success" to true,
                "count" to friends.size,
                "friends" to friends.map { profile ->
                    mapOf(
                        "id" to profile.id,
                        "profileId" to profile.profileId,
                        "username" to profile.username,
                        "fullName" to profile.fullName
                    )
                }
            ))
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(mapOf(
                "success" to false,
                "message" to "Error: ${e.message}"
            ))
        }
    }

    /**
     * Get mutual friends between two profiles
     *
     * @param profileId1 The profile ID of the first user
     * @param profileId2 The profile ID of the second user
     * @return ResponseEntity with list of mutual friends
     */
    @GetMapping("/mutual")
    fun getMutualFriends(
        @RequestParam profileId1: String,
        @RequestParam profileId2: String
    ): ResponseEntity<Map<String, Any>> {
        return try {
            val mutualFriends = friendshipService.getMutualFriends(profileId1, profileId2)
            ResponseEntity.ok(mapOf(
                "success" to true,
                "count" to mutualFriends.size,
                "mutualFriends" to mutualFriends.map { profile ->
                    mapOf(
                        "id" to profile.id,
                        "profileId" to profile.profileId,
                        "username" to profile.username,
                        "fullName" to profile.fullName
                    )
                }
            ))
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(mapOf(
                "success" to false,
                "message" to "Error: ${e.message}"
            ))
        }
    }

    /**
     * Unfriend a user (remove friendship)
     *
     * @param profileId The profile ID of the user removing the friend
     * @param friendProfileId The profile ID of the user being removed as friend
     * @return ResponseEntity with success message or error
     */
    @DeleteMapping("/unfriend")
    fun unfriendUser(
        @RequestParam profileId: String,
        @RequestParam friendProfileId: String
    ): ResponseEntity<Map<String, Any>> {
        return try {
            val success = friendshipService.removeFriendship(profileId, friendProfileId)
            if (success) {
                ResponseEntity.ok(mapOf(
                    "success" to true,
                    "message" to "Friend removed successfully"
                ))
            } else {
                ResponseEntity.badRequest().body(mapOf(
                    "success" to false,
                    "message" to "Failed to remove friend"
                ))
            }
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(mapOf(
                "success" to false,
                "message" to "Error: ${e.message}"
            ))
        }
    }
}