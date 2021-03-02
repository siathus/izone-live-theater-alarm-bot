package com.izone.onethestorycgvalarm.service;

import com.izone.onethestorycgvalarm.component.TheaterProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Service
public class TheaterService {

    private static final Logger logger = LoggerFactory.getLogger(TheaterService.class);

    private TheaterProperty theaterProperty;
    private SendTelegramMessageService sendTelegramMessageService;
    private RestTemplate restTemplate;

    public TheaterService(TheaterProperty theaterProperty, SendTelegramMessageService sendTelegramMessageService, RestTemplate restTemplate) {
        this.theaterProperty = theaterProperty;
        this.sendTelegramMessageService = sendTelegramMessageService;
        this.restTemplate = restTemplate;
    }

//    @PostConstruct
    @Scheduled(initialDelay = 1000, fixedRate = 1000)
    @Async
    public void getMovieList() {
        List<Map<String, String>> cgvList = theaterProperty.getCgv();
        Iterator<Map<String, String>> iterator = cgvList.iterator();

        while (iterator.hasNext()) {
            Map<String, String> theater = iterator.next();
            String theaterName = theater.get("name");
            logger.info("{} 상영시간표 요청", theaterName);

            UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl("http://www.cgv.co.kr/common/showtimes/iframeTheater.aspx");
            for (String key : theater.keySet()) {
                if (key.equals("name")) {
                    continue;
                }
                uriComponentsBuilder.queryParam(key, theater.get(key));
            }
            URI uri = uriComponentsBuilder.build().toUri();
            logger.info("상영시간표 요청 URI : {}", uri.toString());
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(uri, String.class);
            if (!responseEntity.getStatusCode().is2xxSuccessful()) {
                logger.error("상영 시간표 요청에 대한 응답 코드가 200이 아님");
                logger.error("응답 HTTP Code : {}", responseEntity.getStatusCodeValue());
                continue;
            }
            String responseBody = responseEntity.getBody();
            if (responseBody.contains("midx=84415") || responseBody.contains("IZ*ONE")) {
                logger.info("키ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ타");
                logger.info("텔레그램 봇으로 메세지 전송");
                sendTelegramMessageService.sendMessage(theaterName, uri.toString());
                iterator.remove();
            }
        }
    }
}
