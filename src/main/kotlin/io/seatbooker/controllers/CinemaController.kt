package io.seatbooker.io.seatbooker.controllers

import io.seatbooker.io.seatbooker.models.BookingResult
import io.seatbooker.io.seatbooker.models.dto.SeatDto
import io.seatbooker.io.seatbooker.models.response.ApiResponse
import io.seatbooker.io.seatbooker.models.response.ErrorResponse
import io.seatbooker.io.seatbooker.models.response.SuccessResponse
import io.seatbooker.io.seatbooker.service.CinemaService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/cinema")
open class CinemaController @Autowired constructor(
    private val cinemaService: CinemaService
){

    @PostMapping("/seats/book")
    fun bookSeat(@RequestBody seatDto: SeatDto): ResponseEntity<ApiResponse> {
        val bookingResult = cinemaService.bookSeat(seatDto)
        when (bookingResult) {
            is BookingResult.SeatAlreadyBooked -> {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ErrorResponse.SeatErrorResponse(bookingResult.message))
            }
            is BookingResult.BookingAdded -> {
                return ResponseEntity.ok(
                    SuccessResponse.SeatBookResponse(
                        seatNumber = bookingResult.booking.bookedSeats,
                        isBooked = true,
                        seatPrice = bookingResult.booking.ticketPrice ?: 0.0,
                        userId = bookingResult.booking.users?.id ?: 0,
                        cinemaName = bookingResult.booking.cinemaName
                    )
                )
            }
        }
    }
}