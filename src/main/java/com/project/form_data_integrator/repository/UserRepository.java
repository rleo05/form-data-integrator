package com.project.form_data_integrator.repository;

import com.project.form_data_integrator.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByEmail(String email);
}
