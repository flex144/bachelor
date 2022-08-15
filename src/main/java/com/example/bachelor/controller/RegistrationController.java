package com.example.bachelor.controller;

import com.example.bachelor.data.dto.UserDto;
import com.example.bachelor.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Controller
@RequestMapping("/registration")
public class RegistrationController {

    @Autowired
    UserService userService;

    @ModelAttribute("user")
    public UserDto registrationDto() {
        return new UserDto();
    }

    @GetMapping
    public String showRegistrationForm(Model model) {
        return "registration";
    }

    @PostMapping
    public String registerUser(@ModelAttribute("user") UserDto userDto, Model model,
                               RedirectAttributes redirectAttributes) {

        String errorMessage = "";

        try {
            userService.createUser(userDto.getEmail(), userDto.getPassword());
        } catch (IllegalStateException e ) { //TODO: Create Own Excption
            errorMessage = e.getLocalizedMessage();
        }

        if (errorMessage.equals("")) {



            /*ConfirmationToken confirmationToken = new ConfirmationToken(userDto.getEmail());
            userService.createConfirmationToken(confirmationToken);

            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(userDto.getEmail());
            mailMessage.setFrom("core.ep18@gmail.com");
            mailMessage.setSubject("Complete Registration!");
            String uri = ServletUriComponentsBuilder.fromCurrentContextPath().build().toString();
            String url = uri + "/confirm?token=" + confirmationToken.getConfirmationToken();
            mailMessage.setText("To confirm your account, please click here : " + url + "  (If that" +
                    " doesn't work, please copy and paste the link into your browser.)");

            emailSenderService.sendEmail(mailMessage);
            redirectAttributes.addFlashAttribute("email", userDto.getEmail());*/
            return "redirect:/login";
        } else {
            model.addAttribute("errorMessage", errorMessage);
            return "registration";
        }
    }

}
