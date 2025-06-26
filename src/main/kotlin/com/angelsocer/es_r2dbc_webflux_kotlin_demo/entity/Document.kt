package com.angelsocer.es_r2dbc_webflux_kotlin_demo.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

/**
 * Entity representing a document stored in PostgreSQL.
 * This is the target entity where processed Elasticsearch document data will be stored.
 */
@Table("documents")
data class Document(
    @Id
    val id: Long? = null,
    
    @Column("document_id")
    val documentId: String,
    
    @Column("title")
    val title: String,
    
    @Column("content")
    val content: String,
    
    @Column("author")
    val author: String?,
    
    @Column("created_date")
    val createdDate: LocalDateTime,
    
    @Column("processed_date")
    val processedDate: LocalDateTime = LocalDateTime.now(),
    
    @Column("status")
    val status: String
)