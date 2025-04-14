package io.seatbooker.io.seatbooker.service

import io.seatbooker.io.seatbooker.models.BookingResult
import io.seatbooker.io.seatbooker.models.User
import io.seatbooker.io.seatbooker.models.UserBookingHistory
import io.seatbooker.io.seatbooker.repository.UserBookingHistoryRepository
import io.seatbooker.io.seatbooker.repository.UserRepository
import java.time.ZonedDateTime
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
class UserService @Autowired constructor(
    private val repository: UserRepository,
    private val historyRepository: UserBookingHistoryRepository
) {

    fun findUser(email: String): User? = repository.findByUsername(email)

    fun currentUser(): User {
        val authentication = SecurityContextHolder.getContext().authentication
        val currentUser = findUser(authentication.principal.toString()) as User
        return currentUser
    }

    fun addBooking(
        bookTime: ZonedDateTime?,
        cinema: String,
        price: Double,
        movieHallId: Long,
        seatNumber: Int
    ): BookingResult {
        val userOpt = repository.findById(currentUser().id ?: 0)
        val user = userOpt.get()
        val history = UserBookingHistory().apply {
            bookedAt = bookTime
            ticketPrice = price
            users = user
            cinemaName = cinema
            hallId = movieHallId
            bookedSeats = mutableSetOf(seatNumber)
        }
        val savedHistory = historyRepository.save(history)
        val updatedUser = user.apply {
            bookingHistory.add(savedHistory)
        }
        val saveResult = repository.save(updatedUser)
        val lastResult = saveResult.bookingHistory.last()
        return BookingResult.BookingAdded(lastResult)
    }
}