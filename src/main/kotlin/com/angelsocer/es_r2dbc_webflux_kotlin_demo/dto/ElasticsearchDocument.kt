package com.angelsocer.es_r2dbc_webflux_kotlin_demo.dto

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDateTime

/**
 * DTO representing a document retrieved from Elasticsearch.
 * This class maps the JSON structure of documents in Elasticsearch.
 */
data class ElasticsearchDocument(
    @JsonProperty("_id")
    val id: String,
    
    @JsonProperty("_index")
    val index: String,
    
    @JsonProperty("_score")
    val score: Float?,
    
    @JsonProperty("_source")
    val source: DocumentSource
)

/**
 * DTO representing the source field of an Elasticsearch document.
 * Contains the actual document content and metadata.
 */
data class DocumentSource(
    val title: String,
    
    val content: String,
    
    val author: String? = null,
    
    @JsonProperty("status")
    val status: String = "NEW",
    
    @JsonProperty("created_date")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    val createdDate: LocalDateTime,
    
    @JsonProperty("updated_date")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    val updatedDate: LocalDateTime? = null,
    
    @JsonProperty("metadata")
    val metadata: Map<String, Any>? = null
)

/**
 * Extension function to convert ElasticsearchDocument to Document entity.
 */
fun ElasticsearchDocument.toEntity() = com.angelsocer.es_r2dbc_webflux_kotlin_demo.entity.Document(
    documentId = this.id,
    title = this.source.title,
    content = this.source.content,
    author = this.source.author,
    createdDate = this.source.createdDate,
    status = this.source.status
)