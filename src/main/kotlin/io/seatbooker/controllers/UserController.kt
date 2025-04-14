package io.seatbooker.io.seatbooker.controllers

import io.seatbooker.io.seatbooker.models.User
import io.seatbooker.io.seatbooker.service.CinemaService
import io.seatbooker.io.seatbooker.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/api/v1/users")
@RestController
open class UserController @Autowired constructor(
    private val userService: UserService,
    private val cinemaService: CinemaService
){

    @GetMapping("/me", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun authenticatedUser(): ResponseEntity<User> {
        val authentication = SecurityContextHolder.getContext().authentication
        val currentUser = userService.findUser(authentication.principal.toString())
        return ResponseEntity.ok(currentUser)
    }
}