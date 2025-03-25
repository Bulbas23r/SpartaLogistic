package com.bulbas23r.client.hub.route.infrastructure.service;

import com.bulbas23r.client.hub.route.infrastructure.dto.DrivingResponse;
import reactor.core.publisher.Mono;

public interface NaverApiService {

    Mono<DrivingResponse> drivingApi(Double startLatitude, Double startLongitude,
        Double goalLatitude, Double goalLongitude);
}
