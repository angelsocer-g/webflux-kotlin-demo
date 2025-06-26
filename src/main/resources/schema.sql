-- Database schema for the Document entity

-- Drop table if it exists
DROP TABLE IF EXISTS documents;

-- Create documents table
CREATE TABLE documents (
    id SERIAL PRIMARY KEY,
    document_id VARCHAR(255) NOT NULL UNIQUE,
    title VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    author VARCHAR(255),
    created_date TIMESTAMP NOT NULL,
    processed_date TIMESTAMP NOT NULL,
    status VARCHAR(50) NOT NULL
);

-- Create indexes for better query performance
CREATE INDEX idx_documents_status ON documents(status);
CREATE INDEX idx_documents_created_date ON documents(created_date);
CREATE INDEX idx_documents_document_id ON documents(document_id);