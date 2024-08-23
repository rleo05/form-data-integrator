package com.project.form_data_integrator.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("register")
public class RegisterController {

    @GetMapping
    public ModelAndView register(){
        return new ModelAndView("register");
    }


}
