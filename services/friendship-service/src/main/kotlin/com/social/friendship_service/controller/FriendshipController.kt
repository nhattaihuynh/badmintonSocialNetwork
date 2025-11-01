package com.social.friendship_service.controller

import com.social.friendship_service.service.FriendshipService
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/friendship")
class FriendshipController(
    val friendshipService: FriendshipService
) {
    // create a friend request


    // accept a friend request


    // list incoming pending friend requests



    // get all friends for a profile


    // get mutual friend between two profiles


    // unfriend a user
    
}