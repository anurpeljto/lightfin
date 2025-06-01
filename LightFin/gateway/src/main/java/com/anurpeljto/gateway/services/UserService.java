package com.anurpeljto.gateway.services;

import com.anurpeljto.gateway.domain.user.User;
import org.springframework.data.domain.Pageable;

public interface UserService {

    void publishUser(User user);

    void removeUser(User user);

    void updateUser(User user);

    Iterable<User> getUsers(Pageable pageable);

    User getUserById(Integer id);
}
