package com.bulbas23r.client.deliverymanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = {"com.bulbas23r.client.deliverymanager", "common"})
@EnableFeignClients
public class DeliverymanagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(DeliverymanagerApplication.class, args);
    }

}
