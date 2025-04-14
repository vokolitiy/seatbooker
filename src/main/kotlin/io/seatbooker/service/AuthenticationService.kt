package io.seatbooker.io.seatbooker.service

import io.seatbooker.io.seatbooker.models.Role
import io.seatbooker.io.seatbooker.models.User
import io.seatbooker.io.seatbooker.models.dto.LoginUserDto
import io.seatbooker.io.seatbooker.models.dto.RegisterUserDto
import io.seatbooker.io.seatbooker.repository.RoleRepository
import io.seatbooker.io.seatbooker.repository.UserRepository
import java.util.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
open class AuthenticationService @Autowired constructor(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val authenticationManager: AuthenticationManager
) {

    fun signup(dto: RegisterUserDto): User {
        val userToRegister = User().apply {
            email = dto.email
            password = passwordEncoder.encode(dto.password)
            username = dto.username
            firstName = dto.firstName
            lastName = dto.lastName
            roles = listOf(Role().apply { name = dto.role }).toMutableList()
        }

        val registeredUser = userRepository.save(userToRegister)
        return registeredUser
    }

    fun authenticate(dto: LoginUserDto): User {
        authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(
                dto.username,
                dto.password
            )
        )

        return userRepository.findByUsername(dto.username)
            ?: throw IllegalStateException("Something went wrong")
    }
}