package io.seatbooker.io.seatbooker.repository

import io.seatbooker.io.seatbooker.models.UserBookingHistory
import org.springframework.data.jpa.repository.JpaRepository

interface UserBookingHistoryRepository : JpaRepository<UserBookingHistory, Long>