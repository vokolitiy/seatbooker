package io.seatbooker.io.seatbooker.config

import io.seatbooker.io.seatbooker.component.JwtAuthenticationFilter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
open class SecurityConfiguration @Autowired constructor(
    @Qualifier("authenticationProvider")
    private val authenticationProvider: AuthenticationProvider,
    private val jwtAuthenticationFilter: JwtAuthenticationFilter
) {

    @Bean
    open fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http.csrf { customizer ->
            customizer.disable()
                .authorizeHttpRequests { requests ->
                    requests.requestMatchers(
                        "/api/v1/users/me",
                        "/api/v1/users/me/bookings"
                    ).authenticated()
                }.authorizeHttpRequests { requests ->
                    requests.requestMatchers(
                        "/api/v1/auth/**",
                        "/api/v1/home",
                        "/api/v1/admin",
                        "/api/v1/cinema"
                    ).permitAll()
                        .anyRequest()
                        .permitAll()
                }.sessionManagement { sessionCustomizer ->
                    sessionCustomizer.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                }.authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
        }

        return http.build()
    }
}