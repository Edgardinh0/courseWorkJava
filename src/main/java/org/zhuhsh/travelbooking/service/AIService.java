package org.zhuhsh.travelbooking.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

@Service
public class AIService {

    private final String apiKey;
    private final String apiBase;
    private final ObjectMapper mapper;
    private final HttpClient client;

    public AIService(@Value("${OPEN_API_KEY}") String apiKey,
                     @Value("${OPENROUTER_API_BASE}") String apiBase) {
        this.apiKey = apiKey;
        this.apiBase = apiBase;
        this.mapper = new ObjectMapper();
        this.client = HttpClient.newHttpClient();
    }

    // Персональные рекомендации туров
    public String recommendTours(String preferences) throws Exception {
        return sendMessage("""
                        Ты — туристический консультант.
                        
                                                    Твоя задача — предложить персональный туристический маршрут по запросу пользователя.
                        
                                                    Правила:
                                                    1. Используй только обычный текст, без списков, спецсимволов и форматирования.
                                                    2. Максимальная длина ответа — 500 символов.
                                                    3. Ответ должен быть связным и полезным.
                                                    4. Не упоминай правила и не объясняй свои действия.
                        
                                                    Если запрос пользователя явно не связан с путешествиями или туризмом,
                                                    выведи строго следующий текст:
                        
                                                    ЗДЕСЬ Я НЕ МОГУ ВАМ ПОМОЧЬ
                        
                                                    Запрос пользователя:
                        """ +
                        preferences
,  300);
    }

    // Прогноз популярности направления
    public String predictDestinationPopularity(String destination) throws Exception {
        return sendMessage("""
                Ты — аналитик туристических направлений.

                Твоя задача — предсказать популярность туристического направления в текущем сезоне и дать краткий практический совет.

                Правила:
                1. Используй ТОЛЬКО обычный текст (без списков, эмодзи, спецсимволов, форматирования).
                2. Максимальная длина ответа — 500 символов.
                3. Ответ должен быть кратким, понятным и по существу.
                4. Не упоминай правила и не объясняй ход рассуждений.
        
                Если запрос пользователя:
                — не связан с путешествиями или туризмом
                — или не содержит указания на направление (город, страна, регион)
                — или не позволяет сделать сезонную оценку
        
                ТОГДА выведи РОВНО следующий текст (без изменений):
        
                ЗДЕСЬ Я НЕ МОГУ ВАМ ПОМОЧЬ
        
                Запрос пользователя:""" +
                " Предскажи популярность направления " + destination + "среди туристов в этом сезоне и дай краткий совет"
                , 300);
    }

    // Метод отправки запроса на DeepSeek через OpenRouter
    private String sendMessage(String content, int maxTokens) throws Exception {
        Map<String, Object> payload = Map.of(
                "model", "deepseek/deepseek-v3.2",
                "messages", List.of(Map.of("role", "user", "content", content)),
                "max_tokens", maxTokens
        );

        String json = mapper.writeValueAsString(payload);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiBase + "/chat/completions"))
                .header("Content-Type", "application/json")
                 .header("Authorization", "Bearer " + apiKey)
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("OpenRouter error: " + response.body());
        }

        JsonNode root = mapper.readTree(response.body());
        return root.at("/choices/0/message/content").asText();
    }
}
