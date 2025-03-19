package io.seatbooker.io.seatbooker.models

sealed class Result {
    data class Success<T>(
        val success: T
    ) : Result()
    data class Error(
        val throwable: Throwable? = null,
        val errorMessage: String? = null
    ) : Result()
}

sealed class UserLookupResult {
    data class UserExists(
        val user: User
    ): UserLookupResult()
    data class UserNotFound(
        val message: String
    ): UserLookupResult()
}

sealed interface CreateCinemaResult {
    data class CinemaExists(
        val cinema: Cinema
    ): CreateCinemaResult
    data class CinemaNotFound(
        val message: String
    ): CreateCinemaResult
}

sealed interface BookingResult {
    data class SeatAlreadyBooked(
        val message: String
    ): BookingResult

    data class BookingAdded(
        val booking: UserBookingHistory
    ): BookingResult
}