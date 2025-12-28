package com.redweber.dev.service;

import com.redweber.dev.entity.Note;
import com.redweber.dev.entity.User;
import com.redweber.dev.repository.NoteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {

    private final NoteRepository noteRepo;

    public NoteService(NoteRepository noteRepo) {
        this.noteRepo = noteRepo;
    }

    public Note createNote(User user, String title, String content) {

        if (title == null || title.isBlank()) {
            throw new RuntimeException("Title cannot be empty");
        }

        if (content == null || content.isBlank()) {
            throw new RuntimeException("Content cannot be empty");
        }

        Note note = new Note();
        note.setTitle(title);
        note.setContent(content);
        note.setUser(user);

        return noteRepo.save(note);
    }

    public List<Note> getNotes(User user) {
        return noteRepo.findByUser(user);
    }

    public Note updateNote(
            User user,
            Long noteId,
            String title,
            String content
    ) {

        Note note = noteRepo
                .findByIdAndUser(noteId, user)
                .orElseThrow(() -> new RuntimeException("Access denied"));

        note.setTitle(title);
        note.setContent(content);

        return noteRepo.save(note);
    }

    public void deleteNote(User user, Long noteId) {
        Note note = noteRepo
                .findByIdAndUser(noteId, user)
                .orElseThrow(() -> new RuntimeException("Access denied"));

        noteRepo.delete(note);
    }
}
