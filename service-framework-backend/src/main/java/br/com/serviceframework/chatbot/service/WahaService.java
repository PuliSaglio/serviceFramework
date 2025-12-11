package br.com.serviceframework.chatbot.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.*;

import java.util.HashMap;
import java.util.Map;

@Service
public class WahaService {
    private final RestTemplate restTemplate = new RestTemplate();
    private final String WAHA_URL = "http://localhost:3000/api/";

    public void sendMessage(String sessionId, String chatId, String resposta) {
        String url = String.format("%s/sendText", WAHA_URL);

        Map<String, String> body = new HashMap<>();
        body.put("chatId", chatId);
        body.put("text", resposta);
        body.put("session", sessionId);

        restTemplate.postForObject(url, body, Void.class);
    }

    public void sendSeen(){

    }
}
