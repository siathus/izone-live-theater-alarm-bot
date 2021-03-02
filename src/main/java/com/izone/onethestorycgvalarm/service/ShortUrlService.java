package com.izone.onethestorycgvalarm.service;

import com.izone.onethestorycgvalarm.dto.NaverShortUrlResponseDto;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class ShortUrlService {

    private static final Logger logger = LoggerFactory.getLogger(ShortUrlService.class);

    private RestTemplate restTemplate;

    @Value("${naver.clientId}")
    private String clientId;

    @Value("${naver.clientSecret}")
    private String clientSecret;

    public ShortUrlService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getShortUrl(String originalUrl) {
        String requestUrl = "https://openapi.naver.com/v1/util/shorturl";
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("url", originalUrl);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        httpHeaders.add("X-Naver-Client-Id", clientId);
        httpHeaders.add("X-Naver-Client-Secret", clientSecret);

        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(params, httpHeaders);
        ResponseEntity<NaverShortUrlResponseDto> responseEntity = restTemplate.exchange(requestUrl, HttpMethod.POST, httpEntity, NaverShortUrlResponseDto.class);
        if (responseEntity.getStatusCode().isError()) {
            logger.error("단축 URL 생성 요청 도중 에러 발생 : {}", responseEntity.getStatusCodeValue());
            return null;
        }
        NaverShortUrlResponseDto responseDto = responseEntity.getBody();
        Map<String, String> result = responseDto.getResult();
        String shortenUrl = result.get("url");
        logger.info("단축 URL 생성 성공. URL : {}", shortenUrl);

        return shortenUrl;
    }
}
