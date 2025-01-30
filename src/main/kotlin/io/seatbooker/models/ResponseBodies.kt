package io.seatbooker.io.seatbooker.models

import com.fasterxml.jackson.annotation.JsonProperty

data class UserExistsResponseBody(
    @JsonProperty("message")
    val message: String
)