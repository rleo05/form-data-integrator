package com.project.form_data_integrator.controllers;

import com.project.form_data_integrator.dto.RegistrationDTO;
import com.project.form_data_integrator.services.ExcelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
public class RegisterController {
    private final ExcelService excelService;

    @GetMapping("/success")
    public String successView(Model model){
        return "success";
    }


    @GetMapping("/register")
    public ModelAndView registerView(RegistrationDTO registrationDTO){
        return new ModelAndView("register");
    }

    @PostMapping("/register")
    public ModelAndView register(@Valid RegistrationDTO registrationDTO, BindingResult bindingResult) throws IOException {
        if(excelService.checkEmail(registrationDTO.email())){
            System.out.println("");
        }
        if(!registrationDTO.password().equals(registrationDTO.confirmPassword())){
            ModelAndView mv = new ModelAndView("register");
            mv.addObject("passwordError", true);
            return mv;
        }

        if(bindingResult.hasErrors()){
            return new ModelAndView("register");
        }

        if(registrationDTO.exportOption().equals("excel")){

        }

        return new ModelAndView("redirect:/success");
    }
}
