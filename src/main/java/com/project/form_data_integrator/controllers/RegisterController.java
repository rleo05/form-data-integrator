package com.project.form_data_integrator.controllers;

import com.project.form_data_integrator.dto.RegistrationDTO;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("register")
public class RegisterController {

    @GetMapping
    public ModelAndView registerView(RegistrationDTO registrationDTO){
        return new ModelAndView("register");
    }

    @PostMapping
    public ModelAndView register(@Valid RegistrationDTO registrationDTO){
        return new ModelAndView("register");
    }
}
