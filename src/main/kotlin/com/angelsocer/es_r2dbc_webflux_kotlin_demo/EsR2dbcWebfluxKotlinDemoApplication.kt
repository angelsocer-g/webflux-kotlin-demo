package com.angelsocer.es_r2dbc_webflux_kotlin_demo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

/**
 * Main application class.
 * Enables Spring Boot auto-configuration and scheduling.
 */
@SpringBootApplication
@EnableScheduling
class EsR2dbcWebfluxKotlinDemoApplication

fun main(args: Array<String>) {
	runApplication<EsR2dbcWebfluxKotlinDemoApplication>(*args)
}
