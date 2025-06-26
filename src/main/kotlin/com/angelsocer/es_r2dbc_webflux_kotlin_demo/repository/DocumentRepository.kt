package com.angelsocer.es_r2dbc_webflux_kotlin_demo.repository

import com.angelsocer.es_r2dbc_webflux_kotlin_demo.entity.Document
import kotlinx.coroutines.flow.Flow
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

/**
 * Repository interface for Document entity using R2DBC with Kotlin Coroutines.
 * Extends CoroutineCrudRepository to provide coroutine-based CRUD operations.
 */
@Repository
interface DocumentRepository : CoroutineCrudRepository<Document, Long> {
    
    /**
     * Find documents by status.
     */
    fun findByStatus(status: String): Flow<Document>
    
    /**
     * Find documents by document ID.
     */
    suspend fun findByDocumentId(documentId: String): Document?
    
    /**
     * Find documents created after a specific date.
     */
    fun findByCreatedDateAfter(date: LocalDateTime): Flow<Document>
    
    /**
     * Find documents by status and created date range.
     */
    @Query("SELECT * FROM documents WHERE status = :status AND created_date BETWEEN :startDate AND :endDate")
    fun findByStatusAndCreatedDateBetween(
        status: String, 
        startDate: LocalDateTime, 
        endDate: LocalDateTime
    ): Flow<Document>
    
    /**
     * Count documents by status.
     */
    suspend fun countByStatus(status: String): Long
}