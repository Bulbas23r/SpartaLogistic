package com.bulbas23r.client.hub.route.domain.model;

import jakarta.annotation.PostConstruct;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class HubConnection {

    private final Map<String, List<String>> connections = new HashMap<>();

    @PostConstruct
    public void init() {
        connections.put("경기남부 센터",
            List.of("경기북부 센터", "서울특별시 센터", "인천광역시 센터", "강원특별자치도 센터", "경상북도 센터",
                "대전광역시 센터", "대구광역시 센터"));

        connections.put("대전광역시 센터",
            List.of("충청남도 센터", "충청북도 센터", "세종특별자치시 센터", "전북특별자치도 센터", "광주광역시 센터",
                "전라남도 센터", "경기남부 센터", "대구광역시 센터"));

        connections.put("대구광역시 센터",
            List.of("경상북도 센터", "경상남도 센터", "부산광역시 센터", "울산광역시 센터", "경상북도 센터",
                "경기남부 센터", "대전광역시 센터"));

        connections.put("경상북도 센터", List.of("경기남부 센터", "대구광역시 센터"));
    }

    public Map<String, List<String>> getConnections() {
        return Collections.unmodifiableMap(connections);
    }
}
