package io.seatbooker.io.seatbooker.models.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class RegisterUserDto(
    @JsonProperty("email")
    val email: String,
    @JsonProperty("password")
    val password: String,
    @JsonProperty("username")
    val username: String,
    @JsonProperty("firstName")
    val firstName: String,
    @JsonProperty("lastName")
    val lastName: String,
    @JsonProperty("role")
    val role: String,
)