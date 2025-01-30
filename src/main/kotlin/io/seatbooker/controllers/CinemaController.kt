package io.seatbooker.io.seatbooker.controllers

import io.seatbooker.io.seatbooker.models.dto.SeatDto
import io.seatbooker.io.seatbooker.models.response.SeatBookResponse
import io.seatbooker.io.seatbooker.models.response.SeatResponse
import io.seatbooker.io.seatbooker.repository.CinemaRepository
import io.seatbooker.io.seatbooker.repository.MovieHallRepository
import io.seatbooker.io.seatbooker.repository.SeatRepository
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
    private val seatRepository: SeatRepository,
    private val cinemaRepository: CinemaRepository,
    private val movieHallRepository: MovieHallRepository
){

    @GetMapping("/{id}/hall/{hallId}/seats")
    fun findSeats(@PathVariable id: String, @PathVariable hallId: String): ResponseEntity<SeatResponse> {
        try {
            val cinemaId = id.toLong()
            val movieHallId = hallId.toLong()
            val cinema = cinemaRepository.findById(cinemaId)
            if (!cinema.isPresent) {
                return ResponseEntity.status(
                    HttpStatus.NOT_FOUND
                ).build()
            } else {
                val movieHall = cinema.get().movieHalls.find { it.id == movieHallId }
                return if (movieHall == null) {
                    ResponseEntity.status(
                        HttpStatus.NOT_FOUND
                    ).build()
                } else {
                    ResponseEntity.ok(
                        SeatResponse(movieHall.seats.toList())
                    )
                }
            }
        } catch (e: NumberFormatException) {
            return ResponseEntity.status(
                HttpStatus.BAD_REQUEST
            ).build()
        }
    }

    @PostMapping("/seats/book")
    fun bookSeat(@RequestBody seatDto: SeatDto): ResponseEntity<SeatBookResponse> {
        val cinemaId = seatDto.cinemaId
        val movieHallId = seatDto.movieHallId
        val seats = findSeats(cinemaId, movieHallId).body
        if (seats == null) {
            return ResponseEntity.status(
                HttpStatus.NOT_FOUND
            ).build()
        } else {
            val hallSeats = seats.seats
            val seat = hallSeats.find { it.id == movieHallId.toLong() }
            val isBooked = seat?.isBooked ?: true
            if (isBooked) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build()
            } else {
                seat?.let {
                    it.isBooked = true
                    val savedSeat = seatRepository.save(it)
                    return ResponseEntity.ok(
                        SeatBookResponse(
                            seatNumber = savedSeat.seatNumber,
                            isBooked = savedSeat.isBooked,
                            seatPrice = savedSeat.ticketPrice
                        )
                    )
                } ?: kotlin.run {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .build()
                }
            }
        }
    }
}