package io.seatbooker.io.seatbooker.service

import io.seatbooker.io.seatbooker.models.User
import io.seatbooker.io.seatbooker.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
class UserService @Autowired constructor(
    private val repository: UserRepository
) {

    fun findUser(email: String): User? = repository.findByUsername(email)

    fun currentUser(): User {
        val authentication = SecurityContextHolder.getContext().authentication
        val currentUser = authentication.principal as User
        return currentUser
    }
}