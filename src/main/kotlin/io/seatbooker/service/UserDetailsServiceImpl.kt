package io.seatbooker.io.seatbooker.service

import io.seatbooker.io.seatbooker.models.Privilege
import io.seatbooker.io.seatbooker.models.Role
import io.seatbooker.io.seatbooker.repository.RoleRepository
import io.seatbooker.io.seatbooker.repository.UserRepository
import java.util.*
import kotlin.collections.ArrayList
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
open class UserDetailsServiceImpl : UserDetailsService {

    @Autowired
    private lateinit var userRepository: UserRepository
    @Autowired
    private lateinit var roleRepository: RoleRepository

    override fun loadUserByUsername(username: String): UserDetails {
        val user = userRepository.findByUsername(username)
            ?: return User(
                " ", " ", true, true, true, true,
                getAuthorities(
                    Arrays.asList(
                        roleRepository.findByName("ROLE_USER")
                    )
                )
            )

        return User(
            user.email, user.getPassword(), user.isEnabled(), true, true,
            true, getAuthorities(user.roles)
        )
    }

    private fun getAuthorities(
        roles: MutableList<Role>
    ): List<GrantedAuthority> {
        return getGrantedAuthorities(getPrivileges(roles))
    }

    private fun getPrivileges(roles: Collection<Role>): List<String> {
        val privileges: MutableList<String> = ArrayList()
        val collection: MutableList<Privilege> = ArrayList()
        for (role in roles) {
            privileges.add(role.name)
            collection.addAll(role.privileges)
        }
        for (item in collection) {
            privileges.add(item.name)
        }
        return privileges
    }

    private fun getGrantedAuthorities(privileges: List<String>): List<GrantedAuthority> {
        val authorities: MutableList<GrantedAuthority> = ArrayList()
        for (privilege in privileges) {
            authorities.add(SimpleGrantedAuthority(privilege))
        }
        return authorities
    }
}