package com.webflux.armeria.grpc

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class WebfluxArmeriaGrpcApplication

fun main(args: Array<String>) {
    runApplication<WebfluxArmeriaGrpcApplication>(*args)
}