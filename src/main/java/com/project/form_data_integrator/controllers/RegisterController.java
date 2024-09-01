package com.project.form_data_integrator.controllers;

import com.project.form_data_integrator.dto.RegistrationDTO;
import com.project.form_data_integrator.services.GoogleSheetsService;
import com.project.form_data_integrator.services.UserService;
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
import java.security.GeneralSecurityException;

@Controller
@RequiredArgsConstructor
public class RegisterController {
    private final ExcelService excelService;
    private final UserService userService;
    private final GoogleSheetsService googleSheetsService;

    @GetMapping("/success")
    public String successView(Model model){
        return "success";
    }

    @GetMapping("/excel-error")
    public String errorExcel(Model model){
        return "errorExcel";
    }


    @GetMapping("/register")
    public ModelAndView registerView(RegistrationDTO registrationDTO) throws GeneralSecurityException, IOException {
        return new ModelAndView("register");
    }

    @PostMapping("/register")
    public ModelAndView register(@Valid RegistrationDTO registrationDTO, BindingResult bindingResult) throws IOException, GeneralSecurityException {
        boolean excelEmail = excelService.checkEmail(registrationDTO.email());
        boolean postgresEmail = userService.findByEmail(registrationDTO.email()) != null;
        boolean googleSheetsEmail = googleSheetsService.checkEmail(registrationDTO.email());
        String exportOpinion = registrationDTO.exportOption();
        if(excelEmail || postgresEmail){
            ModelAndView mv = new ModelAndView();
            mv.addObject("anyError", true);
            if(excelEmail && postgresEmail && exportOpinion.equals("all")){
                mv.addObject("allEmails", true);
                return mv;
            }
            if(excelEmail && exportOpinion.equals("excel")) {
                mv.addObject("excelEmail", true);
                return mv;
            }
            if(postgresEmail && exportOpinion.equals("postgres")){
                mv.addObject("postgresEmail", true);
                return mv;
            }
        }

        if(!registrationDTO.password().equals(registrationDTO.confirmPassword())){
            ModelAndView mv = new ModelAndView("register");
            mv.addObject("passwordError", true);
            return mv;
        }

        if(bindingResult.hasErrors()){
            return new ModelAndView("register");
        }

        if(exportOpinion.equals("excel")){
            excelService.registerNewUser(registrationDTO);
        }

        if(exportOpinion.equals("postgres")){
            userService.registerNewUser(registrationDTO);
        }

        if(exportOpinion.equals("gsheets")){
            googleSheetsService.registerNewUser(registrationDTO);
        }

        if(exportOpinion.equals("all")){
            excelService.registerNewUser(registrationDTO);
            userService.registerNewUser(registrationDTO);
        }

        return new ModelAndView("redirect:/success");
    }
}
