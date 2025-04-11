package io.seatbooker.io.seatbooker.component

import io.seatbooker.io.seatbooker.models.Privilege
import io.seatbooker.io.seatbooker.models.Role
import io.seatbooker.io.seatbooker.models.User
import io.seatbooker.io.seatbooker.repository.PrivilegeRepository
import io.seatbooker.io.seatbooker.repository.RoleRepository
import io.seatbooker.io.seatbooker.repository.UserRepository
import java.util.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationListener
import org.springframework.context.annotation.Configuration
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.transaction.annotation.Transactional


@Configuration
open class BootstrapDataConfigurer @Autowired constructor(
    private val userRepository: UserRepository,
    private val roleRepository: RoleRepository,
    private val privilegeRepository: PrivilegeRepository,
    private val passwordEncoder: PasswordEncoder
): ApplicationListener<ContextRefreshedEvent> {

   private var alreadySetup: Boolean = false

    @Transactional
    override fun onApplicationEvent(event: ContextRefreshedEvent) {
        if (alreadySetup) return
        val readPrivilege = createPrivilegeIfNotFound("READ_PRIVILEGE")
        val writePrivilege = createPrivilegeIfNotFound("WRITE_PRIVILEGE")

        val adminPrivileges: List<Privilege> = Arrays.asList(
            readPrivilege, writePrivilege
        )
        createRoleIfNotFound("ROLE_ADMIN", adminPrivileges)
        createRoleIfNotFound("ROLE_USER", Arrays.asList(readPrivilege))

        val adminRole = roleRepository.findByName("ROLE_ADMIN")
        val user = User()
        user.firstName = "admin"
        user.lastName = "admin"
        user.password = passwordEncoder.encode("admin")
        user.email = "admin@admin.com"
        user.roles = Arrays.asList(adminRole)
        user.enabled = true
        userRepository.save(user)

        alreadySetup = true
    }

    @Transactional
    fun createPrivilegeIfNotFound(name: String): Privilege {
        var privilege = privilegeRepository.findByName(name)
        if (privilege == null) {
            privilege = Privilege()
            privilege.name = name
            privilegeRepository.save(privilege)
        }
        return privilege
    }

    @Transactional
    fun createRoleIfNotFound(name: String, privileges: List<Privilege>) {
        var role = roleRepository.findByName(name)
        if (role == null) {
            role = Role()
            role.name = name
            role.privileges = privileges.toMutableList()
            roleRepository.save(role)
        }
    }
}