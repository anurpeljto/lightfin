package com.anurpeljto.userlistener.services.impl;

import com.anurpeljto.userlistener.domain.User;
import com.anurpeljto.userlistener.repositories.UserRepository;
import com.anurpeljto.userlistener.services.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User save(User user){
        return userRepository.save(user);
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
}
