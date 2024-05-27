package com.JobNest.companyms.kafka.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class CompanyMsgProducer {
    private static final Logger log = LoggerFactory.getLogger(CompanyMsgProducer.class);
    private final String TOPIC = "company-deleted-topic";

    @Autowired
    private KafkaTemplate<String, Long> kafkaTemplate;

    // Publish message to Kafka topic, to be consumed in Reviews-Service
    public void publishMessage(Long companyId) {

        log.info("Sending Topic='{}' with Payload='{}' ", TOPIC, companyId);

        kafkaTemplate.send(TOPIC, companyId);

        log.info("Topic sent to consumer");
    }
}