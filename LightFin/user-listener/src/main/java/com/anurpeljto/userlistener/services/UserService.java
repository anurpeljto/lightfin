package com.anurpeljto.userlistener.services;

import com.anurpeljto.userlistener.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {

    User save(User user);

    User delete(User user);

    User update(User user);

    User getUserById(Integer id);

    Page<User> getAllUsers(Pageable pageable);

    User getUserByEmail(String email);
}
