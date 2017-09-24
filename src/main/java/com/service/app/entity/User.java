package com.service.app.entity;

import com.service.app.security.role.SecurityRole;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "users")
@Data
@EntityListeners(AuditingEntityListener.class)
public class User {

    @Id
    @GeneratedValue
    private Long id;

    @Column(length = 36, unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "activation_token")
    private String activationToken;

    @Column(name = "email_change_token")
    private String emailChangeToken;

    @Column(name = "new_email")
    private String newEmail;

    private boolean enabled;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SecurityRole authorities;

    @CreatedDate
    @Column(name = "registration_date", updatable = false, nullable = false)
    private Date registrationDate;

    @LastModifiedDate
    @Column(name = "modified_date", nullable = false)
    private Date modifiedDate;

    @Embedded
    private Address address;



        @Data
        @Embeddable
        public class Address {

            private String country;

            private String state;

            private String city;

            @Column(name = "zip_code")
            private String zipCode;
        }
}
