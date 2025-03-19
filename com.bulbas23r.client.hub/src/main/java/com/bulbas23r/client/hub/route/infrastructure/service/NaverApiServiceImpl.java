package com.bulbas23r.client.hub.route.infrastructure.service;

import com.bulbas23r.client.hub.route.infrastructure.dto.DrivingResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class NaverApiServiceImpl implements NaverApiService {

    private final WebClient webClient;

    @Value("${naver.directions.url}")
    private String requestUrl;
    @Value("${naver.api-key.id}")
    private String keyId;
    @Value("${naver.api-key.secret}")
    private String keySecret;

    private static final String HEADER_KEY_ID = "X-NCP-APIGW-API-KEY-ID";
    private static final String HEADER_KEY = "X-NCP-APIGW-API-KEY";


    @Override
    public Mono<DrivingResponse> drivingApi(Double startLatitude, Double startLongitude,
        Double goalLatitude, Double goalLongitude) {
        String start = Double.toString(startLatitude) + ',' + startLongitude;
        String goal = Double.toString(goalLatitude) + ',' + goalLongitude;

        return webClient.get()
            .uri(requestUrl + "?start=" + start + "&goal=" + goal)
            .header(HEADER_KEY_ID, keyId)
            .header(HEADER_KEY, keySecret)
            .retrieve()
            .bodyToMono(DrivingResponse.class);
    }
}
