package io.seatbooker.io.seatbooker.controllers

import io.seatbooker.io.seatbooker.models.UserLookupResult
import io.seatbooker.io.seatbooker.models.dto.LoginUserDto
import io.seatbooker.io.seatbooker.models.dto.RegisterUserDto
import io.seatbooker.io.seatbooker.models.response.CreateUserResponse
import io.seatbooker.io.seatbooker.models.response.LoginResponse
import io.seatbooker.io.seatbooker.service.AuthenticationService
import io.seatbooker.io.seatbooker.service.JwtService
import io.seatbooker.io.seatbooker.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/api/v1/auth")
@RestController
open class AuthenticationController @Autowired constructor(
    private val jwtService: JwtService,
    private val userService: UserService,
    private val authenticationService: AuthenticationService
) {

    @PostMapping("/signup")
    fun register(@RequestBody registerUserDto: RegisterUserDto): ResponseEntity<CreateUserResponse> {
        val userResult = userExists(registerUserDto.username)
        when (userResult) {
            is UserLookupResult.UserExists -> {
                return ResponseEntity.status(
                    HttpStatus.CONFLICT
                ).body(
                    CreateUserResponse(
                        errorMessage = "User signup failed"
                    )
                )
            }

            is UserLookupResult.UserNotFound -> {
                val registeredUser = authenticationService.signup(registerUserDto)
                return ResponseEntity.ok(
                    CreateUserResponse(
                        user = registeredUser
                    )
                )
            }
        }
    }

    @PostMapping("/login")
    fun authenticate(@RequestBody dto: LoginUserDto): ResponseEntity<LoginResponse> {
        val userResult = userExists(dto.username)
        when (userResult) {
            is UserLookupResult.UserExists -> {
                val authUser = authenticationService.authenticate(dto)
                val jwtToken = jwtService.generateToken(authUser)
                val loginResponse = LoginResponse(
                    token = jwtToken,
                    expiresIn = jwtService.getExpirationTime()
                )
                return ResponseEntity.ok(loginResponse)
            }

            is UserLookupResult.UserNotFound -> {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .build()
            }
        }
    }

    private fun userExists(username: String): UserLookupResult {
        val user = userService.findUser(username)
        return if (user == null) {
            UserLookupResult.UserNotFound("User not found")
        } else {
            UserLookupResult.UserExists(user)
        }
    }
}