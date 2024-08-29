package com.project.form_data_integrator.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "users_tb")
@Table(name = "users_tb")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private String phone;
    private String country;
    private String email;
    private String password;

    public User(String name, String phone, String country, String email, String encryptedPassword) {
        this.name = name;
        this.phone = phone;
        this.country = country;
        this.email = email;
        this.password = encryptedPassword;
    }
}
