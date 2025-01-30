package io.seatbooker.io.seatbooker.models

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.Date
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

@Entity
@Table(name = "users")
open class User : UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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
    private var isEnabled: Boolean = true
    @CreationTimestamp
    private var createdAt: Date? = null
    @UpdateTimestamp
    private var updatedAt: Date? = null

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

    override fun isEnabled(): Boolean = isEnabled
}