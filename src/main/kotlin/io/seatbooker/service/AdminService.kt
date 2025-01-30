package io.seatbooker.io.seatbooker.service

import io.seatbooker.io.seatbooker.models.Cinema
import io.seatbooker.io.seatbooker.models.CreateCinemaResult
import io.seatbooker.io.seatbooker.models.MovieHall
import io.seatbooker.io.seatbooker.models.MovieHallSeat
import io.seatbooker.io.seatbooker.repository.CinemaRepository
import io.seatbooker.io.seatbooker.repository.MovieHallRepository
import io.seatbooker.io.seatbooker.repository.SeatRepository
import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
open class AdminService @Autowired constructor(
    open val cinemaRepository: CinemaRepository,
    open val movieHallRepository: MovieHallRepository,
    open val seatRepository: SeatRepository
) {

    fun findCinema(name: String): CreateCinemaResult {
        val cinema = cinemaRepository.findAll()
            .filterNotNull()
            .find { it.name == name }
        return if (cinema == null) {
            CreateCinemaResult.CinemaNotFound("Cinema not found")
        } else {
            CreateCinemaResult.CinemaExists(cinema)
        }
    }

    @PostConstruct
    open fun init() {
        println("${this.javaClass.name} is initialized correctly")
    }

    @Transactional
    open fun createCinema(cinema: Cinema): Cinema = cinemaRepository.save(cinema)

    @Transactional
    open fun createSeats(seats: List<MovieHallSeat>): MutableList<MovieHallSeat>
        = seatRepository.saveAll(seats)

    @Transactional
    open fun createMovieHalls(movieHalls: List<MovieHall>): MutableList<MovieHall>
        = movieHallRepository.saveAll(movieHalls)
}