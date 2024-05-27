package com.jobnest.reviewsms.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class ReviewMsgProducer {
    private static final Logger log = LoggerFactory.getLogger(ReviewMsgProducer.class);
    private final String TOPIC = "review-added-topic";

    @Autowired
    private KafkaTemplate<String, Long> kafkaTemplate;

    // Publish message to Kafka topic, to be consumed in Company-Service
    public void publishMessage(Long companyId) {
        log.info("Sending Topic='{}' with Payload='{}' ", TOPIC, companyId);
        kafkaTemplate.send(TOPIC, companyId);
        log.info("Topic sent to consumer");
    }
}