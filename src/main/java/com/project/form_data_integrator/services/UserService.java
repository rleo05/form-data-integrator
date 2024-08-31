package com.project.form_data_integrator.services;

import com.project.form_data_integrator.domain.User;
import com.project.form_data_integrator.dto.RegistrationDTO;
import com.project.form_data_integrator.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User findByEmail(String email){
        return userRepository.findByEmail(email);
    }

    public void registerNewUser(RegistrationDTO registrationDTO){
        String encryptedPassword = BCrypt.hashpw(registrationDTO.password(), BCrypt.gensalt());
        User user = new User(registrationDTO.name(), registrationDTO.phone(),
                registrationDTO.country(), registrationDTO.email(), encryptedPassword);
        userRepository.save(user);
    }
}
