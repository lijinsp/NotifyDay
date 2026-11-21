
package com.example.chatbot.controller;

import com.example.chatbot.dto.EventRequest;
import com.example.chatbot.entity.Event;
import com.example.chatbot.service.EventService;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventService eventService;

    // Constructor injection
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    // 1. Create a new event
    @PostMapping
    public ResponseEntity<Event> createEvent(
            @RequestBody EventRequest request) {

        Event event = convertToEntity(request);

        Event savedEvent = eventService.createEvent(event);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(savedEvent);
    }

    // 2. Get all events
    @GetMapping
    public ResponseEntity<List<Event>> getAllEvents() {

        List<Event> events = eventService.getAllEvents();

        return ResponseEntity.ok(events);
    }

    // 3. Get event by ID
    @GetMapping("/{id}")
    public ResponseEntity<Event> getEventById(
            @PathVariable Long id) {

        Event event = eventService.getEventById(id);

        return ResponseEntity.ok(event);
    }

    // 4. Update an existing event
    @PutMapping("/{id}")
    public ResponseEntity<Event> updateEvent(
            @PathVariable Long id,
            @RequestBody EventRequest request) {

        Event updatedEvent = convertToEntity(request);

        Event savedEvent =
                eventService.updateEvent(id, updatedEvent);

        return ResponseEntity.ok(savedEvent);
    }

    // 5. Delete an event
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(
            @PathVariable Long id) {

        eventService.deleteEvent(id);

        return ResponseEntity.noContent().build();
    }

    // Convert EventRequest DTO into Event entity
    private Event convertToEntity(EventRequest request) {

        Event event = new Event();

        event.setTitle(request.getTitle());
        event.setDescription(request.getDescription());
        event.setEventType(request.getEventType());
        event.setStartDateTime(request.getStartDateTime());
        event.setEndDateTime(request.getEndDateTime());
        event.setAllDay(request.isAllDay());
        event.setLocation(request.getLocation());
        event.setRecurrenceRule(request.getRecurrenceRule());

        return event;
    }

    // Handle event not found exception
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleNotFound(
            EntityNotFoundException exception) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", exception.getMessage()));
    }
}
