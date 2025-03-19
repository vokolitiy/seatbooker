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

@Entity
@Table(name = "seat")
open class MovieHallSeat {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    open var id: Long? = null
    @Column(name = "seat_number")
    open var seatNumber: Int = 0
    @Column(name = "seat_price")
    open var ticketPrice: Double = 0.0
    @Column(name = "is_booked")
    open var isBooked: Boolean = false
    @ManyToOne(
        cascade = [CascadeType.ALL]
    )
    @JoinColumn(name="hall_id")
    @JsonBackReference
    open var movieHall: MovieHall? = null
}