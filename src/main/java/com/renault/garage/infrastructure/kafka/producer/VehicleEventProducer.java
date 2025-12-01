package com.renault.garage.infrastructure.kafka.producer;

import com.renault.garage.infrastructure.kafka.event.VehicleCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

/**
 * Producer Kafka pour publier les événements de création de véhicules.
 * 
 * Pattern utilisé: Observer Pattern, Event-Driven Architecture
 * 
 * @author Renault Team
 * @version 1.0.0
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class VehicleEventProducer {

    private final KafkaTemplate<String, VehicleCreatedEvent> kafkaTemplate;

    @Value("${kafka.topic.vehicle-created}")
    private String vehicleCreatedTopic;

    /**
     * Publie un événement de création de véhicule.
     * 
     * @param event l'événement à publier
     */
    public void publishVehicleCreatedEvent(VehicleCreatedEvent event) {
        log.info("Publishing vehicle created event for vehicle ID: {}", event.getVehicleId());
        
        CompletableFuture<SendResult<String, VehicleCreatedEvent>> future = 
            kafkaTemplate.send(vehicleCreatedTopic, event.getVehicleId().toString(), event);
        
        future.whenComplete((result, ex) -> {
            if (ex == null) {
                log.info("Vehicle created event published successfully. Topic: {}, Partition: {}, Offset: {}",
                    result.getRecordMetadata().topic(),
                    result.getRecordMetadata().partition(),
                    result.getRecordMetadata().offset());
            } else {
                log.error("Failed to publish vehicle created event for vehicle ID: {}", 
                    event.getVehicleId(), ex);
            }
        });
    }
}
