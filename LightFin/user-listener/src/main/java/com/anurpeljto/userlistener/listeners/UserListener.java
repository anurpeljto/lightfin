package com.anurpeljto.userlistener.listeners;

import com.anurpeljto.userlistener.domain.User;
import com.anurpeljto.userlistener.services.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class UserListener {

    private UserService userService;

    private ObjectMapper objectMapper = new ObjectMapper();

    public UserListener(UserService userService, ObjectMapper objectMapper) {
        this.userService = userService;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "user.published")
    public String listen(String payload) {
        try{
            User user = objectMapper.readValue(payload, User.class);
            userService.save(user);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return payload;
    }
}
