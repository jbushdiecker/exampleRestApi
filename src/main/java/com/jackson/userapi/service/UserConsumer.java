package com.jackson.userapi.service;

import com.jackson.userapi.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class UserConsumer {
    private final Logger logger = LoggerFactory.getLogger(UserConsumer.class);

    @KafkaListener(topics = "users", groupId = "group_id")
    public void consume(User user) {
        logger.info("Consumed user message -> {}", user);
    }
}
