package com.angelsocer.es_r2dbc_webflux_kotlin_demo.client

import co.elastic.clients.elasticsearch.ElasticsearchClient
import co.elastic.clients.elasticsearch.core.SearchRequest
import co.elastic.clients.json.JsonData
import com.angelsocer.es_r2dbc_webflux_kotlin_demo.dto.DocumentSource
import com.angelsocer.es_r2dbc_webflux_kotlin_demo.dto.ElasticsearchDocument
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Service for interacting with Elasticsearch using coroutines.
 * Provides methods to query documents from Elasticsearch based on various criteria.
 */
@Service
class ElasticsearchClientService(
    private val elasticsearchClient: ElasticsearchClient
) {
    private val logger = LoggerFactory.getLogger(ElasticsearchClientService::class.java)
    
    @Value("\${es.document.query.index}")
    private lateinit var indexName: String
    
    @Value("\${es.document.query.size:100}")
    private var querySize: Int = 100
    
    private val objectMapper = ObjectMapper()
        .registerModule(JavaTimeModule())
        .registerKotlinModule()
    
    /**
     * Query documents from Elasticsearch based on status.
     * Returns a Flow of ElasticsearchDocument.
     */
    fun queryDocumentsByStatus(status: String): Flow<ElasticsearchDocument> = flow {
        logger.debug("Querying Elasticsearch for documents with status: $status")
        
        val searchRequest = SearchRequest.Builder()
            .index(indexName)
            .size(querySize)
            .query { q -> 
                q.term { t -> 
                    t.field("status").value(status) 
                } 
            }
            .build()
        
        val searchResponse = withContext(Dispatchers.IO) {
            elasticsearchClient.search(searchRequest, ElasticsearchDocument::class.java)
        }
        
        for (hit in searchResponse.hits().hits()) {
            hit.source()?.let { document ->
                logger.debug("Found document with ID: ${hit.id()}")
                emit(document)
            }
        }
    }.flowOn(Dispatchers.IO)
    
    /**
     * Query documents from Elasticsearch created after a specific date.
     * Returns a Flow of ElasticsearchDocument.
     */
    fun queryDocumentsCreatedAfter(date: LocalDateTime): Flow<ElasticsearchDocument> = flow {
        logger.debug("Querying Elasticsearch for documents created after: $date")
        
        val dateStr = date.format(DateTimeFormatter.ISO_DATE_TIME)
        
        val searchRequest = SearchRequest.Builder()
            .index(indexName)
            .size(querySize)
            .query { q -> 
                q.range { r -> 
                    r.field("created_date").gt(JsonData.of(dateStr)) 
                } 
            }
            .build()
        
        val searchResponse = withContext(Dispatchers.IO) {
            elasticsearchClient.search(searchRequest, ElasticsearchDocument::class.java)
        }
        
        for (hit in searchResponse.hits().hits()) {
            hit.source()?.let { document ->
                logger.debug("Found document with ID: ${hit.id()}")
                emit(document)
            }
        }
    }.flowOn(Dispatchers.IO)
    
    /**
     * Query documents from Elasticsearch with a custom query.
     * Returns a Flow of ElasticsearchDocument.
     */
    fun queryDocumentsWithCustomQuery(queryJson: String): Flow<ElasticsearchDocument> = flow {
        logger.debug("Executing custom query against Elasticsearch: $queryJson")
        
        try {
            val searchRequest = SearchRequest.Builder()
                .index(indexName)
                .withJson(java.io.StringReader(queryJson))
                .build()
            
            val searchResponse = withContext(Dispatchers.IO) {
                elasticsearchClient.search(searchRequest, Map::class.java)
            }
            
            for (hit in searchResponse.hits().hits()) {
                val sourceMap = hit.source()
                if (sourceMap != null) {
                    val sourceJson = objectMapper.writeValueAsString(sourceMap)
                    val source = objectMapper.readValue(sourceJson, DocumentSource::class.java)
                    
                    val document = ElasticsearchDocument(
                        id = hit.id(),
                        index = hit.index(),
                        score = hit.score()?.toFloat(),
                        source = source
                    )
                    
                    logger.debug("Found document with ID: ${hit.id()}")
                    emit(document)
                }
            }
        } catch (e: Exception) {
            logger.error("Error executing custom query: ${e.message}", e)
            throw e
        }
    }.flowOn(Dispatchers.IO)
    
    /**
     * Get a document by ID.
     * Returns a single ElasticsearchDocument or null if not found.
     */
    suspend fun getDocumentById(id: String): ElasticsearchDocument? {
        logger.debug("Getting document by ID: $id")
        
        return try {
            val getResponse = withContext(Dispatchers.IO) {
                elasticsearchClient.get({ g -> g.index(indexName).id(id) }, ElasticsearchDocument::class.java)
            }
            
            if (getResponse.found()) {
                logger.debug("Found document with ID: $id")
                getResponse.source()
            } else {
                logger.debug("Document with ID: $id not found")
                null
            }
        } catch (e: Exception) {
            logger.error("Error getting document by ID: ${e.message}", e)
            null
        }
    }
}