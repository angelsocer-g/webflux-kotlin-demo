package com.angelsocer.es_r2dbc_webflux_kotlin_demo.config

import co.elastic.clients.elasticsearch.ElasticsearchClient
import co.elastic.clients.json.jackson.JacksonJsonpMapper
import co.elastic.clients.transport.ElasticsearchTransport
import co.elastic.clients.transport.rest_client.RestClientTransport
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import org.apache.http.HttpHost
import org.apache.http.auth.AuthScope
import org.apache.http.auth.UsernamePasswordCredentials
import org.apache.http.impl.client.BasicCredentialsProvider
import org.elasticsearch.client.RestClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * Configuration class for Elasticsearch client.
 */
@Configuration
class ElasticsearchConfig {

    @Value("\${spring.elasticsearch.uris}")
    private lateinit var elasticsearchUri: String

    @Value("\${spring.elasticsearch.username}")
    private lateinit var elasticsearchUsername: String

    @Value("\${spring.elasticsearch.password}")
    private lateinit var elasticsearchPassword: String

    /**
     * Creates and configures an Elasticsearch client.
     */
    @Bean
    fun elasticsearchClient(): ElasticsearchClient {
        val credentialsProvider = BasicCredentialsProvider()
        credentialsProvider.setCredentials(
            AuthScope.ANY,
            UsernamePasswordCredentials(elasticsearchUsername, elasticsearchPassword)
        )

        val uri = java.net.URI.create(elasticsearchUri)
        val restClient = RestClient.builder(
            HttpHost(uri.host, uri.port, uri.scheme)
        )
            .setHttpClientConfigCallback { httpClientBuilder ->
                httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider)
            }
            .build()

        val objectMapper = ObjectMapper().registerModule(JavaTimeModule())
        val transport: ElasticsearchTransport = RestClientTransport(
            restClient, JacksonJsonpMapper(objectMapper)
        )

        return ElasticsearchClient(transport)
    }
}