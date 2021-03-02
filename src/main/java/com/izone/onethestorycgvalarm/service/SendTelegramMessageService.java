package com.izone.onethestorycgvalarm.service;

import com.izone.onethestorycgvalarm.component.TelegramBotProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Service
public class SendTelegramMessageService {
    private static final Logger logger = LoggerFactory.getLogger(SendTelegramMessageService.class);

    private TelegramBotProperty telegramBotProperty;
    private RestTemplate restTemplate;
    private ShortUrlService shortUrlService;

    public SendTelegramMessageService(TelegramBotProperty telegramBotProperty, ShortUrlService shortUrlService, RestTemplate restTemplate) {
        this.telegramBotProperty = telegramBotProperty;
        this.shortUrlService = shortUrlService;
        this.restTemplate = restTemplate;
    }

    public void sendMessage(String theaterName, String reservationUri) {
        String token = telegramBotProperty.getToken();
        String chatId = telegramBotProperty.getChatId();
        String message = "*" + theaterName + "*" + " 예매 가능 \n연결 링크 : ";

        String shortenReservationUrl = shortUrlService.getShortUrl(reservationUri);
        if (shortenReservationUrl == null) {
            shortenReservationUrl = reservationUri;
        }
        shortenReservationUrl = shortenReservationUrl.replaceAll("\\.", "\\\\.");
        message = message + shortenReservationUrl;

        URI uri =
                UriComponentsBuilder.fromUriString("https://api.telegram.org")
                .path("bot" + token)
                .path("/sendMessage")
                .queryParam("chat_id", chatId)
                .queryParam("text", message)
                .queryParam("disable_web_page_preview", "true")
                .queryParam("parse_mode", "MarkdownV2")
                .build()
                .toUri();

        logger.info("텔레그램 메세지 전송 URI : {}", uri.toString());

        ResponseEntity<String> responseEntity = restTemplate.getForEntity(uri, String.class);
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            logger.info("텔레그램 메세지 전송 완료.");
        } else {
            logger.error("텔레그램 메세지 전송 실패. 응답 코드 : {}", responseEntity.getStatusCodeValue());
            logger.error("에러 응답 바디 : {}", responseEntity.getBody());
        }
    }
}
