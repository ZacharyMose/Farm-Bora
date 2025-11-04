package com.mose.agribora.service;

import com.mose.agribora.entity.AlertEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaAlertConsumer {
    private static final String ALERT_TOPIC = "alerts";

    private final AlertService alertService;
    public KafkaAlertConsumer(AlertService alertService) {
        this.alertService = alertService;
    }

    @KafkaListener(topics = ALERT_TOPIC,groupId = "${spring.kafka.consumer.group-id}")
    public void handleAlert(AlertEvent event) {
        String subject = "Farm alert:" + event.getTemperature().toString() + event.getHumidity().toString();
        try{
            alertService.sendNotificationEmail(event.getFarmerEmail(), subject);
            alertService.sendNotificationSMS(event.getFarmerPhone());
        } catch(Exception e){
            System.err.println("Error sending SMS " + e.getMessage());
        }
    }
}