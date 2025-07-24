package com.anurpeljto.userlistener.listeners;

import com.anurpeljto.userlistener.domain.User;
import com.anurpeljto.userlistener.repositories.UserRepository;
import com.anurpeljto.userlistener.services.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserListener {

    private final UserRepository userRepository;
    private UserService userService;

    private ObjectMapper objectMapper = new ObjectMapper();

    public UserListener(UserService userService, ObjectMapper objectMapper, UserRepository userRepository) {
        this.userService = userService;
        this.objectMapper = objectMapper;
        this.userRepository = userRepository;
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

    @KafkaListener(topics = "user.update")
    public String listenToUpdate(String payload) {
        try {
            User user = objectMapper.readValue(payload, User.class);
            User existing = userRepository.findById(user.getId())
                    .orElseThrow(() -> new RuntimeException("User does not exist"));

            if(user.getFirst_name() != null){
                existing.setFirst_name(user.getFirst_name());
            }
            if(user.getLast_name() != null){
                existing.setLast_name(user.getLast_name());
            }

            if(user.getPassword() != null) {
                existing.setPassword(user.getPassword());
            }
            userRepository.save(existing);
        } catch (JsonProcessingException ex){
            throw new RuntimeException("Failed to parse user data");
        }
        return payload;
    }
}
