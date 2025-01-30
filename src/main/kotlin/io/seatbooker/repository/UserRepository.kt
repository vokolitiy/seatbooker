package io.seatbooker.io.seatbooker.repository

import io.seatbooker.io.seatbooker.models.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User, Long> {
    fun findByUsername(email: String): User?
}