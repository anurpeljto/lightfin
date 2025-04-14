package com.anurpeljto.userlistener.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name="users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String first_name;

    private String last_name;

    private String email;

    private Boolean emailVerified;

    private String email_token;

    private String password;

    private String passwordToken;
}
