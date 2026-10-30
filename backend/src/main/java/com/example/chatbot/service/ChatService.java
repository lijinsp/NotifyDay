package com.example.chatbot.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class ChatService {

    private final ChatClient chatClient;

    public ChatService(ChatClient.Builder chatClientBuilder) {

        this.chatClient = chatClientBuilder
                .defaultSystem("You are a helpful AI assistant. Provide clear and accurate answers.")
                .build();
    }

    public String getReply(String message) {

        return chatClient
                .prompt()
                .user(message)
                .call()
                .content();
    }
}