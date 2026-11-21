package com.example.chatbot.repository;

import com.example.chatbot.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository
        extends JpaRepository<Event, Long> {

}