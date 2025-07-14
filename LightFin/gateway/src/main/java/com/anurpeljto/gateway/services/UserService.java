package com.anurpeljto.gateway.services;

import com.anurpeljto.gateway.domain.user.User;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    void publishUser(User user);

    void removeUser(User user);

    void updateUser(User user);

    Iterable<User> getUsers(Pageable pageable);

    User getUserById(Integer id);
}
