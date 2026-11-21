
package com.example.chatbot.service;

import com.example.chatbot.entity.Event;
import com.example.chatbot.repository.EventRepository;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventService {

    private final EventRepository eventRepository;

    // Constructor injection
    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    // 1. Create a new event
    public Event createEvent(Event event) {
        return eventRepository.save(event);
    }

    // 2. Get all events
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    // 3. Get an event by ID
    public Event getEventById(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Event not found with ID: " + id
                        )
                );
    }

    // 4. Update an existing event
    public Event updateEvent(Long id, Event updatedEvent) {

        Event existingEvent = getEventById(id);

        existingEvent.setTitle(updatedEvent.getTitle());
        existingEvent.setDescription(updatedEvent.getDescription());
        existingEvent.setEventType(updatedEvent.getEventType());
        existingEvent.setStartDateTime(updatedEvent.getStartDateTime());
        existingEvent.setEndDateTime(updatedEvent.getEndDateTime());
        existingEvent.setAllDay(updatedEvent.isAllDay());
        existingEvent.setLocation(updatedEvent.getLocation());
        existingEvent.setRecurrenceRule(updatedEvent.getRecurrenceRule());

        return eventRepository.save(existingEvent);
    }

    // 5. Delete an event
    public void deleteEvent(Long id) {

        Event existingEvent = getEventById(id);

        eventRepository.delete(existingEvent);
    }
}
