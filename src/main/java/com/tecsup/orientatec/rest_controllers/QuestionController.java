package com.tecsup.orientatec.rest_controllers;

import com.tecsup.orientatec.services.CustomChatgptService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/questions")
@RequiredArgsConstructor
public class QuestionController {

    private final CustomChatgptService customChatgptService;

    @GetMapping("/send")
    public ResponseEntity<?> send(@RequestParam String message) {
        try {
            String responseMessage = customChatgptService.sendMessage(message);
            return ResponseEntity.ok(new ChatGptResponse(responseMessage));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(new ErrorResponse("Error al procesar la solicitud: " + e.getMessage()));
        }
    }

    @PostMapping("/send")
    public ResponseEntity<?> sendPost(@RequestBody Map<String, String> payload) {
        try {
            String message = payload.get("message");
            if (message == null || message.isEmpty()) {
                return ResponseEntity.badRequest().body(new ErrorResponse("El mensaje está vacío"));
            }
            String responseMessage = customChatgptService.sendMessage(message);
            return ResponseEntity.ok(new ChatGptResponse(responseMessage));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(new ErrorResponse("Error al procesar la solicitud: " + e.getMessage()));
        }
    }

    public static class ChatGptResponse {
        private String response;

        public ChatGptResponse(String response) {
            this.response = response;
        }

        public String getResponse() {
            return response;
        }

        public void setResponse(String response) {
            this.response = response;
        }
    }

    public static class ErrorResponse {
        private String error;

        public ErrorResponse(String error) {
            this.error = error;
        }

        public String getError() {
            return error;
        }

        public void setError(String error) {
            this.error = error;
        }
    }
}