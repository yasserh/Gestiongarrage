package com.renault.garage.infrastructure.kafka.consumer;

import com.renault.garage.infrastructure.kafka.event.VehicleCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

/**
 * Consumer Kafka pour consommer les événements de création de véhicules.
 * 
 * Pattern utilisé: Observer Pattern, Event-Driven Architecture
 * 
 * @author Renault Team
 * @version 1.0.0
 */
@Component
@Slf4j
public class VehicleEventConsumer {

    /**
     * Consomme les événements de création de véhicules.
     * 
     * @param event l'événement reçu
     * @param partition la partition Kafka
     * @param offset l'offset du message
     */
    @KafkaListener(
        topics = "${kafka.topic.vehicle-created}",
        groupId = "${spring.kafka.consumer.group-id}",
        containerFactory = "kafkaListenerContainerFactory"
    )
    public void consumeVehicleCreatedEvent(
            @Payload VehicleCreatedEvent event,
            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
            @Header(KafkaHeaders.OFFSET) long offset) {
        
        log.info("Received vehicle created event from partition {} with offset {}", partition, offset);
        log.info("Vehicle details - ID: {}, Brand: {}, Model: {}, Garage: {}",
            event.getVehicleId(),
            event.getBrand(),
            event.getModel(),
            event.getGarageName());
        
        // Ici, vous pouvez ajouter la logique métier pour traiter l'événement
        // Par exemple: envoyer une notification, mettre à jour un cache, etc.
        processVehicleCreatedEvent(event);
    }

    /**
     * Traite l'événement de création de véhicule.
     * 
     * @param event l'événement à traiter
     */
    private void processVehicleCreatedEvent(VehicleCreatedEvent event) {
        log.info("Processing vehicle created event for vehicle ID: {}", event.getVehicleId());
        
        // Logique métier personnalisée
        // Exemple: envoi d'email, mise à jour de statistiques, etc.
        
        log.info("Vehicle created event processed successfully for vehicle ID: {}", event.getVehicleId());
    }
}
