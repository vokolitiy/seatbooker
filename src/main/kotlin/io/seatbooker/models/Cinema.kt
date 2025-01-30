package io.seatbooker.io.seatbooker.models

import com.fasterxml.jackson.annotation.JsonManagedReference
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table

@Entity
@Table(name = "cinema")
open class Cinema {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    open var id: Long? = null
    @Column(unique = true, length = 100, nullable = false)
    open var name: String = ""
    @OneToMany(mappedBy = "cinema", cascade = [CascadeType.PERSIST])
    @JsonManagedReference
    open var movieHalls: MutableSet<MovieHall> = HashSet()
}