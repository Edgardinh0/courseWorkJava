package org.zhuhsh.travelbooking.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.zhuhsh.travelbooking.service.AIService;

@RestController
@RequestMapping("/api/ai")
public class AIController {

    private final AIService aiService;

    public AIController(AIService aiService) {
        this.aiService = aiService;
    }

    // Рекомендации туров
    @GetMapping("/recommend")
    public String recommend(@RequestParam String preferences) throws Exception {
        return aiService.recommendTours(preferences);
    }

    // Прогноз популярности направления
    @GetMapping("/predict")
    public String predict(@RequestParam String destination) throws Exception {
        return aiService.predictDestinationPopularity(destination);
    }
}
