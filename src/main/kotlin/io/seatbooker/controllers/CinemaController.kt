package io.seatbooker.io.seatbooker.controllers

import io.seatbooker.io.seatbooker.models.dto.SeatDto
import io.seatbooker.io.seatbooker.models.response.ApiResponse
import io.seatbooker.io.seatbooker.models.response.ErrorResponse
import io.seatbooker.io.seatbooker.models.response.SuccessResponse
import io.seatbooker.io.seatbooker.service.CinemaService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/cinema")
open class CinemaController @Autowired constructor(
    private val cinemaService: CinemaService
){

    @GetMapping("/{id}/hall/{hallId}/seats")
    fun findSeats(@PathVariable id: String, @PathVariable hallId: String): ResponseEntity<ApiResponse> {
        try {
            val cinema = cinemaService.findCinema(id.toLong())
            if (!cinema.isPresent) {
                return ResponseEntity.status(
                    HttpStatus.NOT_FOUND
                ).body(
                    ErrorResponse.SeatErrorResponse("Cinema not found")
                )
            } else {
                val movieHall = cinemaService.findMovieHall(cinema.get(), hallId.toLong())
                return if (movieHall == null) {
                    ResponseEntity.status(
                        HttpStatus.NOT_FOUND
                    ).body(
                        ErrorResponse.SeatErrorResponse("Movie hall not found")
                    )
                } else {
                    ResponseEntity.ok(
                        SuccessResponse.SeatResponse(movieHall.seats.toList())
                    )
                }
            }
        } catch (e: NumberFormatException) {
            return ResponseEntity.status(
                HttpStatus.BAD_REQUEST
            ).body(
                ErrorResponse.SeatErrorResponse("Unknown error")
            )
        }
    }

    @PostMapping("/seats/book")
    fun bookSeat(@RequestBody seatDto: SeatDto): ResponseEntity<ApiResponse> {
        val cinemaId = seatDto.cinemaId
        val movieHallId = seatDto.movieHallId
        val seats = findSeats(cinemaId, movieHallId).body
        if (seats == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ErrorResponse.SeatErrorResponse("Seats not found"))
        } else {
            if (seats is SuccessResponse.SeatResponse) {
                val isBooked = cinemaService.isSeatBooked(seats.seats, movieHallId.toLong())
                if (isBooked) {
                    return ResponseEntity.status(HttpStatus.CONFLICT).build()
                } else {
                    val bookSeatResponse = cinemaService.bookSeat(
                        seats.seats,
                        movieHallId.toLong(),
                        cinemaId.toLong()
                    )
                    if (bookSeatResponse == null) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .build()
                    } else {
                        return ResponseEntity.ok(bookSeatResponse)
                    }
                }
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ErrorResponse.SeatErrorResponse(
                        "Unknown error"
                    ))
            }
        }
    }
}