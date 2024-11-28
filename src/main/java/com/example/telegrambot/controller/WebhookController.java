package com.example.telegrambot.controller;

import com.example.telegrambot.bot.TelegramBotService;
import org.springframework.web.bind.annotation.*;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class WebhookController {

    private final TelegramBotService telegramBotService;

    public WebhookController(TelegramBotService telegramBotService) {
        this.telegramBotService = telegramBotService;
    }

    @PostMapping("/webhook")
    public String handleWebhook(@RequestBody String updateJson) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            TelegramUpdate update = objectMapper.readValue(updateJson, TelegramUpdate.class);

            telegramBotService.handleUpdate(update);

            return "OK";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }
}