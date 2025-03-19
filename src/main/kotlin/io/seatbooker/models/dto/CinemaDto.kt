package io.seatbooker.io.seatbooker.models.dto

import com.fasterxml.jackson.annotation.JsonProperty
import io.seatbooker.io.seatbooker.models.MovieHall
import java.util.*

data class CinemaWithHallsDto(
    @JsonProperty("name")
    val cinemaName: String,
    @JsonProperty("halls")
    val cinemaHalls: List<MovieHall>
)

data class SeatDto(
    @JsonProperty("cinemaId")
    val cinemaId: String,
    @JsonProperty("movieHallId")
    val movieHallId: String,
    @JsonProperty("id")
    val seatNumber: Int
)

data class BookingHistoryDto(
    val bookingDate: Date? = null,
    val cinemaName: String? = null,
    val hallName: String? = null,
    val seats: List<Int>
)