package io.seatbooker.io.seatbooker.models.dto

import com.fasterxml.jackson.annotation.JsonProperty
import io.seatbooker.io.seatbooker.models.MovieHall

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
    val id: Long,
    @JsonProperty("userId")
    val userId: Long
)