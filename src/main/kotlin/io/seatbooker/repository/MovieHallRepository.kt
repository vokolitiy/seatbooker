package io.seatbooker.io.seatbooker.repository

import io.seatbooker.io.seatbooker.models.MovieHall
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MovieHallRepository : JpaRepository<MovieHall, Long>