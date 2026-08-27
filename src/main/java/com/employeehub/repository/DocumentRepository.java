package com.employeehub.repository;

import com.employeehub.model.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {

    List<Document> findByEntityTypeAndEntityId(String entityType, Long entityId);

    // Powers the grid-level "does this row already have a document, and if
    // so which one" indicator — one query for every row on the page instead
    // of a fetch per row.
    List<Document> findByEntityType(String entityType);
}
