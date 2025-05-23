package io.seatbooker

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication
@Configuration
@EnableJpaRepositories(basePackages = [
    "io.seatbooker"
])
@ComponentScan(basePackages = [
    "io.seatbooker"
])
open class SeatBookerApp {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(SeatBookerApp::class.java)
        }
    }
}