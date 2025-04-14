package com.anurpeljto.userlistener.services;

import com.anurpeljto.userlistener.domain.User;

public interface UserService {

    User save(User user);

    User delete(User user);

    User update(User user);
}
