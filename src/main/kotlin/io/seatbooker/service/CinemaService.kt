package io.seatbooker.io.seatbooker.service

import io.seatbooker.io.seatbooker.models.Cinema
import io.seatbooker.io.seatbooker.models.MovieHall
import io.seatbooker.io.seatbooker.models.MovieHallSeat
import io.seatbooker.io.seatbooker.models.UserBookingHistory
import io.seatbooker.io.seatbooker.models.response.SuccessResponse
import io.seatbooker.io.seatbooker.repository.CinemaRepository
import io.seatbooker.io.seatbooker.repository.MovieHallRepository
import io.seatbooker.io.seatbooker.repository.SeatRepository
import io.seatbooker.io.seatbooker.repository.UserBookingHistoryRepository
import io.seatbooker.io.seatbooker.repository.UserRepository
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
open class CinemaService @Autowired constructor(
    private val userRepository: UserRepository,
    private val cinemaRepository: CinemaRepository,
    private val seatRepository: SeatRepository,
    private val historyRepository: UserBookingHistoryRepository,
    private val movieHallRepository: MovieHallRepository,
    private val userService: UserService
) {

    fun findCinema(cinemaId: Long): Optional<Cinema>
        = cinemaRepository.findById(cinemaId)

    fun findMovieHall(cinema: Cinema, movieHallId: Long): MovieHall?
        = cinema.movieHalls.find { it.id == movieHallId }

    fun isSeatBooked(seats: List<MovieHallSeat>, movieHallId: Long): Boolean {
        val seat = seats.find { it.id == movieHallId }
        return seat?.isBooked ?: true
    }

    fun bookSeat(
        seats: List<MovieHallSeat>,
        movieHallId: Long,
        cinemaId: Long
    ): SuccessResponse.SeatBookResponse? {
        val seat = seats.find { it.id == movieHallId }
        val isBooked = seat?.isBooked ?: true
        if (!isBooked) {
            if (seat == null) {
                return null
            } else {
                seat.isBooked = true
                val bookedAt = ZonedDateTime.now(
                    ZoneId.of( "Europe/Paris")
                )
                val savedSeat = seatRepository.save(seat)
                addUserBooking(bookedAt, cinemaId, movieHallId, savedSeat.seatNumber)
                return SuccessResponse.SeatBookResponse(
                    savedSeat.seatNumber,
                    savedSeat.isBooked,
                    savedSeat.ticketPrice,
                    userService.currentUser().id ?: 0,
                    cinemaId
                )
            }
        } else {
            return null
        }
    }

    private fun addUserBooking(
        bookTime: ZonedDateTime,
        cinemaId: Long,
        movieHallId: Long,
        seatNumber: Int
    ) {
        val cinemaDb = cinemaRepository.findById(cinemaId)
        val currentUser = userService.currentUser()
        val user = userRepository.findById(currentUser.id ?: 0)
        if (cinemaDb.isPresent && user.isPresent) {
            val dbUser = user.get()
            val history = UserBookingHistory().apply {
                bookedAt = bookTime
                users = dbUser
                cinemaName = cinemaDb.get().name
                hallId = movieHallId
                bookedSeats = mutableSetOf(seatNumber)
            }
            val savedHistory = historyRepository.save(history)
            val updatedUser = dbUser.apply {
                bookingHistory.add(savedHistory)
            }
            userRepository.save(updatedUser)
        }
    }

    fun findHallName(hallId: Long?): String? {
        if (hallId == null) {
            return null
        } else {
            val movieHallOptional = movieHallRepository.findById(hallId)
            return if (movieHallOptional.isPresent) {
                movieHallOptional.get().name
            } else {
                null
            }
        }
    }
}