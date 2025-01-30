package io.seatbooker.io.seatbooker.controllers

import io.seatbooker.io.seatbooker.models.User
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/api/v1/users")
@RestController
open class UserController {

    @GetMapping("/me", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun authenticatedUser(): ResponseEntity<User> {
        val authentication = SecurityContextHolder.getContext().authentication
        val currentUser = authentication.principal as User
        return ResponseEntity.ok(currentUser)
    }
}