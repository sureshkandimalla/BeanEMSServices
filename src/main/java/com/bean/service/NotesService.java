package com.bean.service;

import com.bean.exception.ResourceNotFoundException;
import com.bean.model.Employee;
import com.bean.model.Notes;
import com.bean.repository.EmployeeRepository;
import com.bean.repository.NotesRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotesService {

    private final NotesRepository notesRepository;
    private final EmployeeRepository employeeRepository;

    public NotesService(NotesRepository notesRepository, EmployeeRepository employeeRepository) {
        this.notesRepository = notesRepository;
        this.employeeRepository = employeeRepository;
    }

    public List<Notes> getAllNotes() {
        return notesRepository.findAll();
    }

    public List<Notes> getNotesByEmployeeId(Long employeeId) {
        return notesRepository.findByEmployee_EmployeeId(employeeId);
    }

    public Notes getNoteById(Long id) {
        return notesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Note not found with id: " + id));
    }

    public Notes saveNote(Notes note) {
        resolveEmployee(note);
        return notesRepository.save(note);
    }

    public Notes updateNote(Long id, Notes details) {
        Notes note = getNoteById(id);
        note.setNotesType(details.getNotesType());
        note.setDetails(details.getDetails());
        if (details.getEmployee() != null) {
            resolveEmployee(details);
            note.setEmployee(details.getEmployee());
        }
        return notesRepository.save(note);
    }

    public void deleteNote(Long id) {
        if (!notesRepository.existsById(id)) {
            throw new ResourceNotFoundException("Note not found with id: " + id);
        }
        notesRepository.deleteById(id);
    }

    private void resolveEmployee(Notes note) {
        if (note.getEmployee() != null && note.getEmployee().getEmployeeId() != 0) {
            Employee emp = employeeRepository.findById(note.getEmployee().getEmployeeId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Employee not found with id: " + note.getEmployee().getEmployeeId()));
            note.setEmployee(emp);
        }
    }
}
