package com.angelsocer.es_r2dbc_webflux_kotlin_demo.service

import com.angelsocer.es_r2dbc_webflux_kotlin_demo.client.ElasticsearchClientService
import com.angelsocer.es_r2dbc_webflux_kotlin_demo.dto.toEntity
import com.angelsocer.es_r2dbc_webflux_kotlin_demo.repository.DocumentRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.LocalDateTime

/**
 * Service for processing documents from Elasticsearch and storing them in PostgreSQL.
 * Uses coroutine flows for reactive, non-blocking operations.
 */
@Service
class DocumentProcessingService(
    private val elasticsearchClientService: ElasticsearchClientService,
    private val documentRepository: DocumentRepository
) {
    private val logger = LoggerFactory.getLogger(DocumentProcessingService::class.java)
    
    /**
     * Process documents with a specific status.
     * Returns a Flow of processed document IDs.
     */
    fun processDocumentsByStatus(status: String): Flow<String> {
        logger.info("Starting to process documents with status: $status")
        
        return elasticsearchClientService.queryDocumentsByStatus(status)
            .onStart { logger.debug("Beginning document processing flow") }
            .onEach { document -> 
                logger.debug("Processing document: ${document.id}") 
            }
            .map { document ->
                val entity = document.toEntity()
                documentRepository.save(entity)
                logger.debug("Saved document with ID: ${document.id}")
                document.id
            }
            .catch { e ->
                logger.error("Error processing documents: ${e.message}", e)
                throw e
            }
            .onCompletion { cause ->
                if (cause == null) {
                    logger.info("Successfully completed processing documents with status: $status")
                } else {
                    logger.error("Document processing completed with error: ${cause.message}", cause)
                }
            }
    }
    
    /**
     * Process documents created after a specific date.
     * Returns a Flow of processed document IDs.
     */
    fun processDocumentsCreatedAfter(date: LocalDateTime): Flow<String> {
        logger.info("Starting to process documents created after: $date")
        
        return elasticsearchClientService.queryDocumentsCreatedAfter(date)
            .onStart { logger.debug("Beginning document processing flow") }
            .onEach { document -> 
                logger.debug("Processing document: ${document.id}") 
            }
            .map { document ->
                val entity = document.toEntity()
                documentRepository.save(entity)
                logger.debug("Saved document with ID: ${document.id}")
                document.id
            }
            .catch { e ->
                logger.error("Error processing documents: ${e.message}", e)
                throw e
            }
            .onCompletion { cause ->
                if (cause == null) {
                    logger.info("Successfully completed processing documents created after: $date")
                } else {
                    logger.error("Document processing completed with error: ${cause.message}", cause)
                }
            }
    }
    
    /**
     * Process documents using a custom query.
     * Returns a Flow of processed document IDs.
     */
    fun processDocumentsWithCustomQuery(queryJson: String): Flow<String> {
        logger.info("Starting to process documents with custom query")
        
        return elasticsearchClientService.queryDocumentsWithCustomQuery(queryJson)
            .onStart { logger.debug("Beginning document processing flow") }
            .onEach { document -> 
                logger.debug("Processing document: ${document.id}") 
            }
            .map { document ->
                val entity = document.toEntity()
                documentRepository.save(entity)
                logger.debug("Saved document with ID: ${document.id}")
                document.id
            }
            .catch { e ->
                logger.error("Error processing documents: ${e.message}", e)
                throw e
            }
            .onCompletion { cause ->
                if (cause == null) {
                    logger.info("Successfully completed processing documents with custom query")
                } else {
                    logger.error("Document processing completed with error: ${cause.message}", cause)
                }
            }
    }
    
    /**
     * Count documents in PostgreSQL by status.
     */
    suspend fun countDocumentsByStatus(status: String): Long {
        return documentRepository.countByStatus(status)
    }
}