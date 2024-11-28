package com.example.telegrambot.bot;

import com.example.telegrambot.config.BotConfig;
import com.example.telegrambot.controller.TelegramUpdate;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SetWebhook;
import com.pengrad.telegrambot.response.BaseResponse;
import com.pengrad.telegrambot.response.SendResponse;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TelegramBotService {
    private final TelegramBot bot;

    @Autowired
    public TelegramBotService(BotConfig botConfig) {
        this.bot = new TelegramBot(botConfig.getBotToken());
        SetWebhook setWebhook = new SetWebhook().url("https://2ddb-78-107-205-71.ngrok-free.app/webhook");
        //Сервер локальный от ngrok
        BaseResponse response = bot.execute(setWebhook);
        if (response.isOk()) {
            System.out.println("Webhook установлен успешно!");
        } else {
            System.err.println("Ошибка установки вебхука: " + response.errorCode() + " " + response.description());
        }
    }

    public void handleUpdate(TelegramUpdate update) {
        if (update.getMessage() != null && update.getMessage().getText() != null) {
            String chatId = update.getMessage().getChat().getId().toString();
            String userMessage = update.getMessage().getText();

            String botResponse = generateResponse(userMessage);

            SendMessage message = new SendMessage(chatId, botResponse);
            SendResponse response = bot.execute(message);

            if (!response.isOk()) {
                System.err.println("Failed to send message: " + response.errorCode() + " " + response.description());
            }
        }
    }

    private String generateResponse(String userMessage) {
        if (userMessage.equalsIgnoreCase("/start")) {
            return "Привет! Я ваш чат-бот. Спросите что-нибудь или используйте команду /help. \n Все запросы обработываю на англиском языке.";
        } else if (userMessage.equalsIgnoreCase("/help")) {
            return "Я могу:\n- При команде /weather показать погоду(Без города, погода не покажется)";
        } else if (userMessage.startsWith("/weather")) {
            String city = userMessage.replace("/weather ","").trim();
            return getWeather(city);
        }else {
            return "Извините, я пока не понимаю: \"" + userMessage + "\"";
        }
    }
    private String getWeather(String city) {
        String apiKey = "3e6a47a75677f9e68eeaa13de5761a67";//openWeatherMap API ключ
        String url = String.format("https://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s&units=metric&lang=ru", city, apiKey);

        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

            JSONObject json = new JSONObject(response.getBody());
            String weather = json.getJSONArray("weather").getJSONObject(0).getString("description");
            double temp = json.getJSONObject("main").getDouble("temp");

            return String.format("Погода в %s: %s, температура: %.1f°C", city, weather, temp);
        } catch (Exception e) {
            return "Не удалось получить данные о погоде.";
        }
    }
}
