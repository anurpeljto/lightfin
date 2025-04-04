package com.anurpeljto.gateway.repositories;

import com.anurpeljto.gateway.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
}
