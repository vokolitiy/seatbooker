package io.seatbooker.io.seatbooker.models.response

import io.seatbooker.io.seatbooker.models.Cinema
import io.seatbooker.io.seatbooker.models.MovieHallSeat
import io.seatbooker.io.seatbooker.models.User

data class LoginResponse(
    val token: String,
    val expiresIn: Long,
    val errorMessage: String? = null
)

data class CreateUserResponse(
    val user: User? = null,
    val errorMessage: String? = null
)

data class CreateCinemaResponse(
    val cinema: Cinema? = null,
    val errorMessage: String? = null
)

data class SeatResponse(
    val seats: List<MovieHallSeat>
)

data class SeatBookResponse(
    val seatNumber: Int,
    val isBooked: Boolean,
    val seatPrice: Double
)