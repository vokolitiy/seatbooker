package io.seatbooker.io.seatbooker.models

import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonManagedReference
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.Table

@Entity
@Table(name = "movieHall")
open class MovieHall {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    open var id: Long? = null
    @Column(unique = true, length = 100, nullable = false)
    open var name: String = ""
    @OneToMany(
        mappedBy = "movieHall",
        cascade = [CascadeType.PERSIST],
        fetch = FetchType.EAGER
    )
    @JsonManagedReference
    open var seats: MutableSet<MovieHallSeat> = HashSet()
    @ManyToOne(
        cascade = [CascadeType.PERSIST]
    )
    @JoinColumn(name="cinema_id")
    @JsonBackReference
    open var cinema: Cinema? = null
}