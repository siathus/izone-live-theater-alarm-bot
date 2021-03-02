package com.izone.onethestorycgvalarm.component;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "telegram.bot")
public class TelegramBotProperty {
    private String token;
    private String chatId;

    public void setToken(String token) {
        this.token = token;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getToken() {
        return token;
    }

    public String getChatId() {
        return chatId;
    }
}
