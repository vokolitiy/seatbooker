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
    fun createCinema(@RequestBody dto: List<CinemaWithHallsDto>): ResponseEntity<ApiResponse> {
        val createdCinemas = mutableListOf<Cinema>()
        if (dto.isNotEmpty()) {
            dto.forEach { cinemaDto ->
                val result = service.findCinema(cinemaDto.cinemaName)
                when (result) {
                    is CreateCinemaResult.CinemaExists -> {
                        return ResponseEntity.status(HttpStatus.CONFLICT)
                            .body(ErrorResponse.CreateCinemaErrorResponse(errorMessage = "Create cinema failed"))
                    }

                    is CreateCinemaResult.CinemaNotFound -> {
                        val cinemaName = cinemaDto.cinemaName
                        val cinemaHalls = cinemaDto.cinemaHalls
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
                        createdCinemas.add(createdCinema)
                    }
                }
            }
            return ResponseEntity.ok(
                SuccessResponse.CreateCinemaResponse(
                    cinemas = createdCinemas
                )
            )
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .build()
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