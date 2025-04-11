package io.seatbooker.io.seatbooker.models

import com.fasterxml.jackson.annotation.JsonBackReference
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToMany
import jakarta.persistence.Table

@Entity
@Table(name = "privileges")
open class Privilege {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    open var id: Long = 0

    open var name: String = ""

    @ManyToMany(mappedBy = "privileges")
    @JsonBackReference
    open var roles: MutableList<Role> = ArrayList()
}