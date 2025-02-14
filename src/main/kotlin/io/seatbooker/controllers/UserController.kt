package io.seatbooker.io.seatbooker.controllers

import io.seatbooker.io.seatbooker.models.User
import io.seatbooker.io.seatbooker.models.dto.BookingHistoryDto
import io.seatbooker.io.seatbooker.models.response.ApiResponse
import io.seatbooker.io.seatbooker.models.response.SuccessResponse
import io.seatbooker.io.seatbooker.service.CinemaService
import io.seatbooker.io.seatbooker.service.UserService
import java.util.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
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
        val currentUser = authentication.principal as User
        return ResponseEntity.ok(currentUser)
    }

    @GetMapping("/me/bookings")
    fun bookingHistory(): ResponseEntity<ApiResponse> {
        val user = userService.currentUser()
        val history = user.bookingHistory
        if (history.isEmpty()) {
            return ResponseEntity.ok(
                SuccessResponse.BookingHistoryResponse(listOf())
            )
        } else {
            val bookings = history.map { booking ->
                BookingHistoryDto(
                    bookingDate = Date.from(booking.bookedAt?.toInstant()),
                    cinemaName = booking.cinemaName,
                    hallName = cinemaService.findHallName(booking.hallId),
                    seats = booking.bookedSeats.toList()
                )
            }

            return ResponseEntity.ok(
                SuccessResponse.BookingHistoryResponse(bookings)
            )
        }
    }
}