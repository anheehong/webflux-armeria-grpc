package com.webflux.armeria.grpc.config

import com.mongodb.reactivestreams.client.MongoClient
import com.mongodb.reactivestreams.client.MongoClients
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration
import org.springframework.data.mongodb.core.ReactiveMongoTemplate


@Configuration
class MongoDBConfig(
    @Value("\${spring.data.mongodb.port}") private val port: String = "",
    @Value("\${spring.data.mongodb.database}") private val dbName: String = ""
) : AbstractReactiveMongoConfiguration() {


    override fun reactiveMongoClient(): MongoClient {
        return MongoClients.create()
    }

    override fun getDatabaseName(): String {
        return dbName
    }

    @Bean
    fun reactiveMongoTemplate(): ReactiveMongoTemplate? {
        return ReactiveMongoTemplate(reactiveMongoClient()!!, databaseName!!)
    }
}