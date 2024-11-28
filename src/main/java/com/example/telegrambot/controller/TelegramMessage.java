package com.example.telegrambot.controller;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TelegramMessage { 
    @JsonProperty("message_id")
    private Long messageId;

    @JsonProperty("text")
    private String text;

    @JsonProperty("chat")
    private Chat chat;

    // Геттеры и сеттеры
    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public String getText() {  // Должен быть public
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Chat getChat() {  // Должен быть public
        return chat;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }
}
