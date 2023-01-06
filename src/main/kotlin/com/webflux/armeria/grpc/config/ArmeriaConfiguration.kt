package com.webflux.armeria.grpc.config

import com.linecorp.armeria.client.ClientFactory
import com.linecorp.armeria.client.WebClientBuilder
import com.linecorp.armeria.client.circuitbreaker.CircuitBreakerClient
import com.linecorp.armeria.client.circuitbreaker.CircuitBreakerRule
import com.linecorp.armeria.server.ServerBuilder
import com.linecorp.armeria.server.docs.DocService
import com.linecorp.armeria.server.logging.AccessLogWriter
import com.linecorp.armeria.server.logging.LoggingService
import com.linecorp.armeria.spring.ArmeriaServerConfigurator
import com.linecorp.armeria.spring.web.reactive.ArmeriaClientConfigurator
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class ArmeriaConfiguration{

    @Bean
    fun armeriaServerConfigurator() = ArmeriaServerConfigurator {
        builder: ServerBuilder ->
            builder.serviceUnder("/docs", DocService())
            builder.decorator(LoggingService.newDecorator())
            builder.accessLogWriter(AccessLogWriter.combined(), false)
    }

    @Bean
    fun clientFactory(): ClientFactory? {
        return ClientFactory.insecure()
    }

    @Bean
    fun armeriaClientConfigurator(clientFactory: ClientFactory?): ArmeriaClientConfigurator? {
        return ArmeriaClientConfigurator { builder: WebClientBuilder ->

            val rule = CircuitBreakerRule.builder()
                .onServerErrorStatus()
                .onException()
                .thenFailure()
            builder.decorator(
                CircuitBreakerClient.builder(rule)
                    .newDecorator()
            )

            builder.factory(clientFactory!!)
        }
    }
}