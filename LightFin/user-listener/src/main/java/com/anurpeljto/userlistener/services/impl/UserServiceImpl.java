package com.anurpeljto.userlistener.services.impl;

import com.anurpeljto.userlistener.domain.User;
import com.anurpeljto.userlistener.repositories.UserRepository;
import com.anurpeljto.userlistener.services.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final JavaMailSender mailSender;

    public UserServiceImpl(UserRepository userRepository, JavaMailSender mailSender) {
        this.userRepository = userRepository;
        this.mailSender = mailSender;
    }

    public void sendEmail(String toEmail, String activationLink){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Activate Your Account");
        message.setText("Click the link to activate: " + activationLink);
        message.setFrom("admin.lightfin@gmail.com");

        mailSender.send(message);
    }

    @Override
    public User save(User user){
        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());
        if(existingUser.isPresent()){
            throw new RuntimeException("User already exists");
        }
        User saved = userRepository.save(user);
        sendEmail(user.getEmail(), user.getEmailToken());
        return saved;
    }

    @Override
    public User delete(User user){
        userRepository.delete(user);
        return user;
    }

    @Override
    public User update(User user){
        return userRepository.save(user);
    }

    @Override
    public User getUserById(Integer id){
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public Page<User> getAllUsers(Pageable pageable){
        return userRepository.findAll(pageable);
    }
}
