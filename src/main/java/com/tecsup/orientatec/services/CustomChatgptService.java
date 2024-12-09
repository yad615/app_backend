package com.tecsup.orientatec.services;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CustomChatgptService {
    private final RestTemplate restTemplate;
    private final Gson gson;

    @Value("${openai.api.key}")
    private String apiKey;

    @Value("${openai.api.url}")
    private String apiUrl;

    @Value("${openai.api.model}")
    private String apiModel;

    public CustomChatgptService(RestTemplate restTemplate, Gson gson) {
        this.restTemplate = restTemplate;
        this.gson = gson;
    }

    public String sendMessage(String message) {
        String systemMessage = "Soy un asistente de Orientatec, una aplicación desarrollada por:" +
                "\n- Yadhira Alcantara R." +
                "\n- Patrick Chavez J." +
                "\nEstudiantes de TECSUP, 4to ciclo." +
                "\n\nMisión:" +
                "\n1. Guiar en elección vocacional" +
                "\n2. Informar sobre TECSUP" +
                "\n3. Orientar en carreras y becas" +
                "\n4. Acompañar en test vocacional" +
                "\n5. Preparar con pre-simulacros" +
                "\n\nEnfoque: Profesional, empático y preciso.";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        JsonObject requestBody = new JsonObject();
        requestBody.addProperty("model", apiModel);
        requestBody.add("messages", gson.toJsonTree(new Object[]{
                new Message("system", systemMessage),
                new Message("user", message)
        }));

        HttpEntity<String> request = new HttpEntity<>(requestBody.toString(), headers);

        try {
            String responseBody = restTemplate.postForObject(apiUrl, request, String.class);
            return extractResponseContent(responseBody);
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    private String extractResponseContent(String responseBody) {
        try {
            JsonObject response = JsonParser.parseString(responseBody).getAsJsonObject();
            return response.getAsJsonArray("choices")
                    .get(0).getAsJsonObject()
                    .getAsJsonObject("message")
                    .get("content").getAsString();
        } catch (Exception e) {
            return "Procesamiento de respuesta fallido";
        }
    }

    private static class Message {
        String role;
        String content;

        Message(String role, String content) {
            this.role = role;
            this.content = content;
        }
    }
}