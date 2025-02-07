package io.seatbooker.io.seatbooker.models

import com.fasterxml.jackson.annotation.JsonBackReference
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.time.ZonedDateTime
import kotlin.collections.HashSet

@Entity
@Table(name = "bookingHistory")
open class UserBookingHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    open var id: Long? = null
    @Column(name = "booked_at")
    open var bookedAt: ZonedDateTime? = null
    @ManyToOne(
        cascade = [CascadeType.ALL]
    )
    @JoinColumn(name="user_id")
    @JsonBackReference
    open var users: User? = null
    @Column(name = "cinema_name")
    open var cinemaName: String? = null
    @Column(name = "hall_id")
    open var hallId: Long? = null
    @Column(name = "booked_seats")
    open var bookedSeats: MutableSet<Int> = HashSet()
}