package com.project.form_data_integrator.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RegistrationDTO(
        @NotBlank(message = "Name is mandatory")
        @Size(min = 10, max = 30, message = "Name must be between 10 and 30")
        String name,
        @NotBlank(message = "Phone is mandatory")
        String phone,
        @NotBlank(message = "Country is mandatory")
        String country,
        @NotBlank(message = "Email is mandatory")
        String email,
        @NotBlank(message = "Password is mandatory")
        String password,
        @NotBlank(message = "Confirm password is mandatory")
        String confirmPassword,
        String exportOption
) {
}
