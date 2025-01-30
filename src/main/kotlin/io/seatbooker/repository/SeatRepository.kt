package io.seatbooker.io.seatbooker.repository

import io.seatbooker.io.seatbooker.models.MovieHallSeat
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SeatRepository : JpaRepository<MovieHallSeat, Long> {
}