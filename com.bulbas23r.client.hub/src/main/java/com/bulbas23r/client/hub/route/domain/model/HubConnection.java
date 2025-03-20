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

    private static final String SEOUL = "서울특별시 센터";
    private static final String GYEONGGGI_NORTH = "경기 북부 센터";
    private static final String GYEONGGGI_SOUTH = "경기 남부 센터";
    private static final String BUSAN = "부산광역시 센터";
    private static final String DAEGU = "대구광역시 센터";
    private static final String INCHEON = "인천광역시 센터";
    private static final String GWANGJU = "광주광역시 센터";
    private static final String DAEJEON = "대전광역시 센터";
    private static final String ULSAN = "울산광역시 센터";
    private static final String SEJONG = "세종특별자치시 센터";
    private static final String GANGWON = "강원특별자치도 센터";
    private static final String CHUNGCHEONGBUK = "충청북도 센터";
    private static final String CHUNGCHEONGNAM = "충청남도 센터";
    private static final String JEOLLABUK = "전북특별자치도 센터";
    private static final String JEOLLANAM = "전라남도 센터";
    private static final String GYEONGSANGBUK = "경상북도 센터";
    private static final String GYEONGSANGNAM = "경상남도 센터";

    @PostConstruct
    public void init() {
        connections.put(GYEONGGGI_SOUTH,
            List.of(GYEONGGGI_NORTH, SEOUL, INCHEON, GANGWON, GYEONGSANGBUK,
                DAEJEON, DAEGU));

        connections.put(DAEJEON,
            List.of(CHUNGCHEONGNAM, CHUNGCHEONGBUK, SEJONG, JEOLLABUK, GWANGJU,
                JEOLLANAM, GYEONGGGI_SOUTH, DAEGU));

        connections.put(DAEGU,
            List.of(GYEONGSANGBUK, GYEONGSANGNAM, BUSAN, ULSAN, GYEONGSANGBUK,
                GYEONGGGI_SOUTH, DAEJEON));

        connections.put(GYEONGSANGBUK, List.of(GYEONGGGI_SOUTH, DAEGU));
    }

    public Map<String, List<String>> getConnections() {
        return Collections.unmodifiableMap(connections);
    }
}
