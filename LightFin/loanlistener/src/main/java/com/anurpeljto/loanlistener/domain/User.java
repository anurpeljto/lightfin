package com.anurpeljto.loanlistener.domain;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Integer id;

    private String first_name;

    private String last_name;

    private String email;

    private Boolean emailVerified;

    private String emailToken;

    private String password;

    private String passwordToken;
}
