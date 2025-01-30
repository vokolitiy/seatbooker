package io.seatbooker.io.seatbooker.exception

import io.jsonwebtoken.ExpiredJwtException
import org.springframework.http.HttpStatusCode
import org.springframework.http.ProblemDetail
import org.springframework.security.authentication.AccountStatusException
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.lang.Exception
import java.security.SignatureException

@RestControllerAdvice
open class GlobalExceptionHandler {

    @ExceptionHandler(Exception::class)
    fun handleSecurityException(exception: Exception): ProblemDetail? {
        var errorDetail: ProblemDetail? = null
        exception.printStackTrace()
        if (exception is BadCredentialsException) {
            errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(401), exception.message)
            errorDetail.setProperty("description", "The username or password is incorrect")

            return errorDetail;
        }

        if (exception is AccountStatusException) {
            errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(403), exception.message)
            errorDetail.setProperty("description", "The account is locked")
        }

        if (exception is AccessDeniedException) {
            errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(403), exception.message)
            errorDetail.setProperty("description", "You are not authorized to access this resource")
        }

        if (exception is SignatureException) {
            errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(403), exception.message)
            errorDetail.setProperty("description", "The JWT signature is invalid");
        }

        if (exception is ExpiredJwtException) {
            errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(403), exception.message)
            errorDetail.setProperty("description", "The JWT token has expired")
        }

        if (errorDetail == null) {
            errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(500), exception.message)
            errorDetail.setProperty("description", "Unknown internal server error.")
        }

        return errorDetail
    }
}