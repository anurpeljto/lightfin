package com.anurpeljto.gateway.services.impl;

import com.anurpeljto.gateway.domain.user.User;
import com.anurpeljto.gateway.dto.loan.LoanResponseDto;
import com.anurpeljto.gateway.repositories.user.UserRepository;
import com.anurpeljto.gateway.services.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.hash.Hashing;
import com.google.common.io.BaseEncoding;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Collections;
import java.util.Random;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final PasswordEncoder passwordEncoder;

    private UserRepository userRepository;

    private KafkaTemplate<String, String> kafkaTemplate;

    private ObjectMapper objectMapper;

    private RestTemplate restTemplate;

    @Value("${spring.user.url}")
    private String userUrl;

    public UserServiceImpl(UserRepository userRepository, KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper, RestTemplate restTemplate, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
        this.restTemplate = restTemplate;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void publishUser(User user) {
        try {
            user.setEmailVerified(true);

            SecureRandom random = new SecureRandom();
            byte[] randomBytes = new byte[16];
            random.nextBytes(randomBytes);

            String email_token = java.util.Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
            user.setEmailToken(email_token);

//            String hashPassword = passwordEncoder.encode(user.getPassword());
//            user.setPassword(hashPassword);

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
        try{
            String payload = objectMapper.writeValueAsString(user);
            kafkaTemplate.send("user.update", payload);
        } catch(JsonProcessingException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public Iterable<User> getUsers(Pageable pageable) {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(Integer id){
        String requestUrl = String.format("%s/user/%d", userUrl, id);
        ResponseEntity<User> response = restTemplate.getForEntity(requestUrl, User.class);
        return response.getBody();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String requestUrl = String.format("%s/user/email/%s", userUrl, username);
        ResponseEntity<User> response = restTemplate.getForEntity(requestUrl, User.class);
        User user = response.getBody();

        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        return user;
    }
}
