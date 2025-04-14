package io.seatbooker.io.seatbooker.component

import io.seatbooker.io.seatbooker.service.JwtService
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.servlet.HandlerExceptionResolver


@Component
open class JwtAuthenticationFilter @Autowired constructor(
    private val jwtService: JwtService,
    private val userDetailsService: UserDetailsService,
    @Qualifier("handlerExceptionResolver")
    private val exceptionResolver: HandlerExceptionResolver
): OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val header = request.getHeader("Authorization")
        if (header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response)
            return
        }

        try {
            val authHeader = request.getHeader("Authorization")

            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                val token = authHeader.substring(7)
                if (jwtService.validateToken(token)) {
                    val username = jwtService.extractUsername(token)
                    val roles = jwtService.extractRoles(token)
                    val authorities = roles.map { SimpleGrantedAuthority(it) }

                    val auth = UsernamePasswordAuthenticationToken(username, null, authorities)
                    auth.details = WebAuthenticationDetailsSource().buildDetails(request)
                    SecurityContextHolder.getContext().authentication = auth
                }
            }

            filterChain.doFilter(request, response)
        } catch (exc: Exception) {
            exceptionResolver.resolveException(request, response, null, exc)
        }
    }
}