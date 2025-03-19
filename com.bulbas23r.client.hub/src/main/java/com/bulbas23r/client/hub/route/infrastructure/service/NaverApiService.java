package com.bulbas23r.client.hub.route.infrastructure.service;

import com.bulbas23r.client.hub.route.infrastructure.dto.DrivingResponse;

public interface NaverApiService {

    DrivingResponse drivingApi(Double startLatitude, Double startLongitude,
        Double goalLatitude, Double goalLongitude);
}
