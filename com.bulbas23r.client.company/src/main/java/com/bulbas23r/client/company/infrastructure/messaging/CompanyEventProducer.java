package com.bulbas23r.client.company.infrastructure.messaging;

import common.event.DeleteCompanyDto;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CompanyEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendDeleteCompanyEvent(UUID companyId) {
        DeleteCompanyDto eventDto = new DeleteCompanyDto(companyId);

        kafkaTemplate.send("delete-company", eventDto);
    }

}
