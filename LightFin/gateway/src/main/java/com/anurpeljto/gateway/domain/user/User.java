package com.anurpeljto.gateway.domain.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Table(name="users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String first_name;

    private String last_name;

    private String email;

    @Column(name="email_verified")
    private Boolean emailVerified;

    @Column(name = "email_token")
    private String emailToken;

    private String password;

    @Column(name="password_token")
    private String passwordToken;
}
