package io.seatbooker.io.seatbooker.models.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class LoginUserDto(
    @JsonProperty("username")
    val username: String,
    @JsonProperty("password")
    val password: String
)