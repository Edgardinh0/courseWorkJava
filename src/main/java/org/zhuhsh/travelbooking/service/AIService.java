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
        return sendMessage("Предложи персональные туристические маршруты для пользователя с такими предпочтениями : " + preferences + " Не используй какие-то особые символы, просто обычныйц текст (выведи информацию очень кратко), а если запрос содержит что-то несвязанное с путешествием и туром, то выведи ЗДЕСЬ Я НЕ МОГУ ВАМ ПОМОЧЬ",  300);
    }

    // Прогноз популярности направления
    public String predictDestinationPopularity(String destination) throws Exception {
        return sendMessage("Предскажи популярность направления " + destination + " среди туристов в этом сезоне и дай краткий совет (информацию выведи кратко)" + "Не используй какие-то особые символы, просто обычныйц текст (выведи информацию очень кратко), а если запрос содержит что-то несвязанное с путешествием и туром, то выведи ЗДЕСЬ Я НЕ МОГУ ВАМ ПОМОЧЬ", 300);
    }

    // Универсальный метод отправки запроса на DeepSeek через OpenRouter
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
