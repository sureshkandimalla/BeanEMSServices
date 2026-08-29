package com.employeehub.repository;

import com.employeehub.model.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {

    // Most-recent-first — a note history reads naturally newest-on-top.
    List<Note> findByTypeAndEntityIdOrderByDateDesc(String type, Long entityId);
}
