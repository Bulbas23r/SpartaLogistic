package com.bulbas23r.client.deliverymanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.bulbas23r.client.deliverymanager", "common"})
public class DeliverymanagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(DeliverymanagerApplication.class, args);
    }

}
