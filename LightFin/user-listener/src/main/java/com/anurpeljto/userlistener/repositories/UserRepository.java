package com.anurpeljto.userlistener.repositories;

import com.anurpeljto.userlistener.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
}
