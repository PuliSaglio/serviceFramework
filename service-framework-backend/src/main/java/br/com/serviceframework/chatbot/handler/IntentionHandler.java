package br.com.serviceframework.chatbot.handler;

import br.com.serviceframework.chatbot.model.UserState;

public interface IntentionHandler {
    String handle(String mensagem, String chatId, UserState userState);
}
