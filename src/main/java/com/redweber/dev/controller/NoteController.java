package com.redweber.dev.controller;

import com.redweber.dev.dto.*;
import com.redweber.dev.entity.Note;
import com.redweber.dev.entity.User;
import com.redweber.dev.service.NoteService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/notes")
public class NoteController {

    private final NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    // ✅ CONSISTENT SESSION VALIDATION
    private User requireUser(HttpSession session) {
        User user = (User) session.getAttribute("USER");

        if (user == null) {
            throw new RuntimeException("Unauthorized");
        }

        return user;
    }

    @PostMapping
    public ResponseEntity<NoteResponse> create(
            @RequestBody NoteRequest req,
            HttpSession session
    ) {

        User user = requireUser(session);

        Note note = noteService.createNote(
                user,
                req.getTitle(),
                req.getContent()
        );

        return ResponseEntity.ok(toResponse(note));
    }

    @GetMapping
    public ResponseEntity<List<NoteResponse>> getAll(HttpSession session) {

        User user = requireUser(session);

        List<NoteResponse> notes = noteService.getNotes(user)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(notes);
    }

    @PutMapping
    public ResponseEntity<NoteResponse> update(
            @RequestBody NoteUpdateRequest req,
            HttpSession session
    ) {

        User user = requireUser(session);

        Note note = noteService.updateNote(
                user,
                req.getNoteId(),
                req.getTitle(),
                req.getContent()
        );

        return ResponseEntity.ok(toResponse(note));
    }

    @DeleteMapping("/{noteId}")
    public ResponseEntity<Void> delete(
            @PathVariable Long noteId,
            HttpSession session
    ) {

        User user = requireUser(session);

        noteService.deleteNote(user, noteId);
        return ResponseEntity.ok().build();
    }

    private NoteResponse toResponse(Note note) {
        NoteResponse dto = new NoteResponse();
        dto.setId(note.getId());
        dto.setTitle(note.getTitle());
        dto.setContent(note.getContent());
        return dto;
    }
}
