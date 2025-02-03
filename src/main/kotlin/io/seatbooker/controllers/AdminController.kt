package io.seatbooker.io.seatbooker.controllers

import io.seatbooker.io.seatbooker.models.Cinema
import io.seatbooker.io.seatbooker.models.CreateCinemaResult
import io.seatbooker.io.seatbooker.models.MovieHall
import io.seatbooker.io.seatbooker.models.dto.CinemaWithHallsDto
import io.seatbooker.io.seatbooker.models.response.ApiResponse
import io.seatbooker.io.seatbooker.models.response.ErrorResponse
import io.seatbooker.io.seatbooker.models.response.SuccessResponse
import io.seatbooker.io.seatbooker.service.AdminService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/admin")
class AdminController @Autowired constructor(
    private val service: AdminService
) {

    @PostMapping("/cinema/create")
    fun createCinema(@RequestBody dto: CinemaWithHallsDto): ResponseEntity<ApiResponse> {
        val result = service.findCinema(dto.cinemaName)
        when (result) {
            is CreateCinemaResult.CinemaExists -> {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ErrorResponse.CreateCinemaErrorResponse(errorMessage = "Create cinema failed"))
            }

            is CreateCinemaResult.CinemaNotFound -> {
                val cinemaName = dto.cinemaName
                val cinemaHalls = dto.cinemaHalls
                val hallSeats = cinemaHalls.flatMap { it.seats }
                service.createSeats(hallSeats)
                val cinema = Cinema().apply {
                    name = cinemaName
                    movieHalls = cinemaHalls.toHashSet()
                }
                val createdCinema = service.createCinema(cinema)
                val movieHalls = cinemaHalls.map { hall ->
                    MovieHall().apply {
                        id = hall.id
                        name = hall.name
                        seats = hall.seats
                        this.cinema = createdCinema
                    }
                }
                service.createMovieHalls(movieHalls)
                return ResponseEntity.ok(
                    SuccessResponse.CreateCinemaResponse(
                        cinema = createdCinema
                    )
                )
            }
        }
    }

    @PutMapping("/cinema/update")
    fun updateCinema(@RequestBody dto: CinemaWithHallsDto): ResponseEntity<ApiResponse> {
        when (val result = service.findCinema(dto.cinemaName)) {
            is CreateCinemaResult.CinemaExists -> {
                val dbCinema = result.cinema
                service.createMovieHalls(dto.cinemaHalls)
                dbCinema.movieHalls = dto.cinemaHalls.toHashSet()
                val updatedCinema = service.createCinema(dbCinema)
                return ResponseEntity.ok(
                    SuccessResponse.CreateCinemaResponse(
                        cinema = updatedCinema
                    )
                )
            }

            is CreateCinemaResult.CinemaNotFound -> {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(
                        ErrorResponse.CreateCinemaErrorResponse(
                            errorMessage = "Cinema with name ${dto.cinemaName} not found"
                        )
                    )
            }
        }
    }

}