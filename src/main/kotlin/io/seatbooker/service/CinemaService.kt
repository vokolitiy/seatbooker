package io.seatbooker.io.seatbooker.service

import io.seatbooker.io.seatbooker.models.BookingResult
import io.seatbooker.io.seatbooker.models.MovieHallSeat
import io.seatbooker.io.seatbooker.models.dto.SeatDto
import io.seatbooker.io.seatbooker.repository.CinemaRepository
import io.seatbooker.io.seatbooker.repository.SeatRepository
import java.time.ZoneId
import java.time.ZonedDateTime
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
open class CinemaService @Autowired constructor(
    private val cinemaRepository: CinemaRepository,
    private val seatRepository: SeatRepository,
    private val userService: UserService
) {

    fun bookSeat(seatDto: SeatDto): BookingResult {
        val cinemaId = seatDto.cinemaId
        val movieHallId = seatDto.movieHallId
        val seatNumber = seatDto.seatNumber

        val seatStatus = seatStatus(seatNumber, movieHallId.toLong(), cinemaId.toLong())
        val seat = seatStatus.first
        val isSeatBooked = seatStatus.second
        if (!isSeatBooked) {
            seat.apply {
                isBooked = true
            }
            val cinemaName = cinemaRepository.findById(cinemaId.toLong()).get().name
            val bookedAt = ZonedDateTime.now(ZoneId.of( "Europe/Paris"))
            val savedSeat = seatRepository.save(seat)
            return userService.addBooking(
                bookedAt,
                cinemaName,
                seat.ticketPrice,
                movieHallId.toLong(),
                savedSeat.seatNumber
            )
        } else {
            return BookingResult.SeatAlreadyBooked("Sorry. Seat with number $seatNumber in movie hall $movieHallId is already booked")
        }
    }

    private fun seatStatus(seatNumber: Int, movieHallId: Long, cinemaId: Long): Pair<MovieHallSeat, Boolean> {
        val seat = findSeat(seatNumber, movieHallId, cinemaId)
        if (seat == null) {
            throw IllegalStateException("Seat with number $seatNumber not found")
        } else {
            return Pair(seat, seat.isBooked)
        }
    }

    private fun findSeat(seatNumber: Int, movieHallId: Long, cinemaId: Long): MovieHallSeat? {
        val cinema = cinemaRepository.findById(cinemaId).get()
        val movieHalls = cinema.movieHalls
        return movieHalls.flatMap { it.seats }
            .filter { it.movieHall != null  }
            .filter { it.movieHall!!.id == movieHallId }
            .find { it.seatNumber == seatNumber }
    }
 }