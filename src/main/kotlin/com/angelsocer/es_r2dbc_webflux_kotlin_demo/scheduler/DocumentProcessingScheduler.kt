package com.angelsocer.es_r2dbc_webflux_kotlin_demo.scheduler

import com.angelsocer.es_r2dbc_webflux_kotlin_demo.service.DocumentProcessingService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Scheduler component that periodically triggers document processing.
 * Uses Spring's @Scheduled annotation for scheduling and Kotlin coroutines for execution.
 */
@Component
class DocumentProcessingScheduler(
    private val documentProcessingService: DocumentProcessingService
) {
    private val logger = LoggerFactory.getLogger(DocumentProcessingScheduler::class.java)
    private val schedulerScope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    private val isProcessing = AtomicBoolean(false)
    
    @Value("\${es.document.query.index}")
    private lateinit var indexName: String
    
    /**
     * Scheduled task to process new documents.
     * Runs according to the cron expression defined in application.properties.
     */
    @Scheduled(cron = "\${es.document.scheduler.cron}")
    fun processNewDocuments() {
        if (isProcessing.compareAndSet(false, true)) {
            logger.info("Starting scheduled document processing for new documents")
            
            schedulerScope.launch {
                try {
                    val processedCount = processDocumentsWithStatus("NEW")
                    logger.info("Scheduled processing completed. Processed $processedCount new documents")
                } catch (e: Exception) {
                    logger.error("Error in scheduled document processing: ${e.message}", e)
                } finally {
                    isProcessing.set(false)
                }
            }
        } else {
            logger.warn("Previous document processing task is still running. Skipping this execution")
        }
    }
    
    /**
     * Process documents with a specific status.
     * Returns the count of processed documents.
     */
    private suspend fun processDocumentsWithStatus(status: String): Int {
        logger.debug("Processing documents with status: $status")
        
        var count = 0
        documentProcessingService.processDocumentsByStatus(status)
            .collect {
                count++
                if (count % 100 == 0) {
                    logger.info("Processed $count documents so far")
                }
            }
        
        return count
    }
    
    /**
     * Process documents created after a specific date.
     * This method can be called manually or scheduled separately if needed.
     */
    fun processRecentDocuments(hoursBack: Int = 24) {
        if (isProcessing.compareAndSet(false, true)) {
            logger.info("Starting document processing for documents from the last $hoursBack hours")
            
            val startTime = LocalDateTime.now().minusHours(hoursBack.toLong())
            
            schedulerScope.launch {
                try {
                    var count = 0
                    documentProcessingService.processDocumentsCreatedAfter(startTime)
                        .collect {
                            count++
                            if (count % 100 == 0) {
                                logger.info("Processed $count documents so far")
                            }
                        }
                    
                    logger.info("Processing completed. Processed $count documents from the last $hoursBack hours")
                } catch (e: Exception) {
                    logger.error("Error processing recent documents: ${e.message}", e)
                } finally {
                    isProcessing.set(false)
                }
            }
        } else {
            logger.warn("Previous document processing task is still running. Skipping this execution")
        }
    }
    
    /**
     * Process documents with a custom query.
     * This method can be called manually when needed.
     */
    fun processWithCustomQuery(queryJson: String) {
        if (isProcessing.compareAndSet(false, true)) {
            logger.info("Starting document processing with custom query")
            
            schedulerScope.launch {
                try {
                    var count = 0
                    documentProcessingService.processDocumentsWithCustomQuery(queryJson)
                        .collect {
                            count++
                            if (count % 100 == 0) {
                                logger.info("Processed $count documents so far")
                            }
                        }
                    
                    logger.info("Processing completed. Processed $count documents with custom query")
                } catch (e: Exception) {
                    logger.error("Error processing documents with custom query: ${e.message}", e)
                } finally {
                    isProcessing.set(false)
                }
            }
        } else {
            logger.warn("Previous document processing task is still running. Skipping this execution")
        }
    }
}