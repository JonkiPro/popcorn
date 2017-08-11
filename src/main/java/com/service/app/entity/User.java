package com.service.app.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "users")
@Data
public class User {

    @Id
    @GeneratedValue
    private Long id;

    private String username;
    private String email;
    private String password;

    @Column(name = "activation_token")
    private String activationToken;

    @Column(name = "email_change_token")
    private String emailChangeToken;

    @Column(name = "new_email")
    private String newEmail;

    private boolean enabled;

    private String authorities;
}
