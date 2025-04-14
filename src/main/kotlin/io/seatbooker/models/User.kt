package io.seatbooker.io.seatbooker.models

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonManagedReference
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.JoinTable
import jakarta.persistence.ManyToMany
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashSet
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails


@Entity
@Table(name = "users")
open class User : UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    open var id: Long? = null
    @Column(unique = true, length = 100, nullable = false)
    private var username: String = ""
    @Column(length = 100, nullable = false)
    open var firstName: String = ""
    @Column(length = 100, nullable = false)
    open var lastName: String = ""
    @Column(unique = true, length = 100, nullable = false)
    open var email: String = ""
    @Column(length = 100, nullable = false)
    private var password: String = ""
    private var accountNonExpired: Boolean = true
    private var accountNonLocked: Boolean = true
    private var credentialsNonExpired: Boolean = true
    open var enabled: Boolean = true
    @CreationTimestamp
    private var createdAt: Date? = null
    @UpdateTimestamp
    private var updatedAt: Date? = null
    @OneToMany(
        mappedBy = "users",
        cascade = [CascadeType.ALL],
        fetch = FetchType.EAGER
    )
    @JsonManagedReference
    open var bookingHistory: MutableSet<UserBookingHistory> = HashSet()

    @ManyToMany(cascade = [CascadeType.ALL])
    @JoinTable(
        name = "users_roles",
        joinColumns = [JoinColumn(name = "user_id", referencedColumnName = "id")],
        inverseJoinColumns = [JoinColumn(name = "role_id", referencedColumnName = "id")]
    )
    @JsonManagedReference
    open var roles: MutableList<Role> = ArrayList()


    @JsonIgnore
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> = mutableListOf()

    override fun getPassword(): String = password

    override fun getUsername(): String = username

    open fun setUsername(name: String) {
        this.username = name
    }

    open fun setPassword(password: String) {
        this.password = password
    }

    override fun isAccountNonExpired(): Boolean = accountNonExpired

    override fun isAccountNonLocked(): Boolean = accountNonLocked

    override fun isCredentialsNonExpired(): Boolean = credentialsNonExpired

    override fun isEnabled(): Boolean = enabled
}