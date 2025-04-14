package com.anurpeljto.gateway.services.impl;

import com.anurpeljto.gateway.domain.user.User;
import com.anurpeljto.gateway.repositories.user.UserRepository;
import com.anurpeljto.gateway.services.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.hash.Hashing;
import com.google.common.io.BaseEncoding;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Random;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    private KafkaTemplate<String, String> kafkaTemplate;

    private ObjectMapper objectMapper;

    public UserServiceImpl(UserRepository userRepository, KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.userRepository = userRepository;
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public void publishUser(User user) {
        try {
            user.setEmailVerified(false);

            SecureRandom random = new SecureRandom();
            BaseEncoding baseEncoding = BaseEncoding.base64Url().omitPadding();
            byte[] randomBytes = new byte[16];
            random.nextBytes(randomBytes);

            String email_token = baseEncoding.encode(randomBytes);
            user.setEmailToken(email_token);

            String hashPassword = Hashing.sha256().hashString(user.getPassword(), StandardCharsets.UTF_8).toString();
            user.setPassword(hashPassword);

            String payload = objectMapper.writeValueAsString(user);

            kafkaTemplate.send("user.published", payload);
        } catch (JsonProcessingException ex){
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void removeUser(User user) {
        userRepository.delete(user);
    }

    @Override
    public void updateUser(User user) {
        userRepository.save(user);
    }

    @Override
    public Iterable<User> getUsers(Pageable pageable) {
        return userRepository.findAll();
    }
}
