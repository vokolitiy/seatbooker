package io.seatbooker.io.seatbooker.models.response

import io.seatbooker.io.seatbooker.models.Cinema
import io.seatbooker.io.seatbooker.models.MovieHallSeat
import io.seatbooker.io.seatbooker.models.User
import io.seatbooker.io.seatbooker.models.dto.BookingHistoryDto

sealed interface ApiResponse

sealed interface ErrorResponse : ApiResponse {
    data class LoginErrorResponse(
        val errorMessage: String? = null
    ): ErrorResponse

    data class CreateUserErrorResponse(
        val errorMessage: String? = null
    ): ErrorResponse

    data class CreateCinemaErrorResponse(
        val errorMessage: String? = null
    ): ErrorResponse

    data class SeatErrorResponse(
        val errorMessage: String? = null
    ): ErrorResponse
}

sealed interface SuccessResponse : ApiResponse {
    data class LoginResponse(
        val token: String,
        val expiresIn: Long
    ) : SuccessResponse

    data class CreateUserResponse(
        val user: User? = null
    ): SuccessResponse

    data class CreateCinemaResponse(
        val cinema: Cinema? = null
    ): SuccessResponse

    data class SeatResponse(
        val seats: List<MovieHallSeat>
    ): SuccessResponse

    data class SeatBookResponse(
        val seatNumber: Int,
        val isBooked: Boolean,
        val seatPrice: Double,
        val userId: Long,
        val cinemaId: Long
    ): SuccessResponse

    data class BookingHistoryResponse(
        val bookings: List<BookingHistoryDto>
    ): SuccessResponse
}