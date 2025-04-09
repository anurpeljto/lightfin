package com.anurpeljto.gateway.repositories.user;

import com.anurpeljto.gateway.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
}
