package com.tecsup.orientatec.services;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Map;

@Service
public class CustomChatgptService {

    private final RestTemplate restTemplate;

    @Value("${openai.api.key}")
    private String apiKey;

    @Value("${openai.api.url}")
    private String apiUrl;

    @Value("${openai.api.model}")
    private String model;

    public CustomChatgptService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String sendMessage(String message) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);
        headers.set("Content-Type", "application/json");

        String systemMessage = "Eres un modelo de inteligencia artificial avanzado que responde con precisión y formato adecuado según el contexto de la consulta del usuario. Si se solicita código, incluye etiquetas completas y explica claramente su uso.";

        String body = "{ \"model\": \"" + model + "\", " +
                "\"messages\": [" +
                "{ \"role\": \"system\", \"content\": \"" + systemMessage + "\" }, " +
                "{ \"role\": \"user\", \"content\": \"" + message + "\" }" +
                "] }";

        HttpEntity<String> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(apiUrl, request, String.class);

            JsonObject responseNode = JsonParser.parseString(response.getBody()).getAsJsonObject();
            String chatgptResponse = responseNode.getAsJsonArray("choices")
                    .get(0).getAsJsonObject()
                    .getAsJsonObject("message")
                    .get("content").getAsString();

            if (chatgptResponse.isEmpty()) {
                chatgptResponse = "No se obtuvo respuesta válida de GPT-4.";
            }

            return chatgptResponse;

        } catch (Exception e) {
            e.printStackTrace();
            return "Error al procesar la solicitud: " + e.getMessage();
        }
    }
}