package io.seatbooker.io.seatbooker.service

import io.jsonwebtoken.Claims
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.security.Key
import java.util.*
import java.util.function.Function
import javax.crypto.SecretKey

@Service
class JwtService {

    private val jwtSecret: SecretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256)

    @Value("\${security.jwt.expiration-time}")
    val jwtExpiration: Long = 0
        get() = field

    fun generateToken(username: String, roles: List<String>): String {
        return Jwts.builder()
            .setSubject(username)
            .claim("roles", roles)
            .setIssuedAt(Date())
            .setExpiration(Date(System.currentTimeMillis() + jwtExpiration))
            .signWith(jwtSecret)
            .compact()
    }

    fun extractUsername(token: String): String =
        Jwts.parserBuilder().setSigningKey(jwtSecret).build()
            .parseClaimsJws(token).body.subject

    fun extractRoles(token: String): List<String> =
        Jwts.parserBuilder().setSigningKey(jwtSecret).build()
            .parseClaimsJws(token).body["roles"] as List<String>

    fun validateToken(token: String): Boolean = try {
        Jwts.parserBuilder().setSigningKey(jwtSecret).build().parseClaimsJws(token)
        true
    } catch (e: JwtException) {
        false
    }
}