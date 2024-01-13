package com.example.bachelor.controller;

import com.example.bachelor.data.dto.ConfirmationTokenDto;
import com.example.bachelor.data.dto.UserDto;
import com.example.bachelor.data.entities.ConfirmationTokenEntity;
import com.example.bachelor.services.EmailSenderService;
import com.example.bachelor.services.UserService;
import com.example.bachelor.utility.constants.HtmlConstants;
import com.example.bachelor.utility.exceptions.UserAlreadyExistsException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Objects;

@Controller
@RequestMapping("/registration")
public class RegistrationController {

    @Autowired
    UserService userService;

    @Autowired
    EmailSenderService emailSenderService;

    @Autowired
    Environment environment;

    @ModelAttribute("user")
    public UserDto registrationDto() {
        return new UserDto();
    }

    @GetMapping
    public String showRegistrationForm(Model model) {

        return HtmlConstants.REGISTRATION;
    }

    @PostMapping
    public ModelAndView registerUser(@ModelAttribute("user") UserDto userDto, ModelMap model) {

        String errorMessage = null;

        if (userDto.getPassword().equals(userDto.getConfirmationPassword())) {
            try {
                userDto.getEmail().trim();
                userService.createUser(userDto);
            } catch (UserAlreadyExistsException e) {
                errorMessage = e.getLocalizedMessage();
            }
        } else {
            errorMessage = "Passwörter stimmen nicht überein!";
        }

        if (errorMessage != null) {
            model.addAttribute("errorMessage", errorMessage);
            return new ModelAndView(HtmlConstants.REGISTRATION, model);
        }

        ConfirmationTokenEntity confirmationToken = userService.createNewConfirmationToken(userDto.getEmail());

        String uri = ServletUriComponentsBuilder.fromCurrentContextPath().build().toString();
        emailSenderService.sendConfirmationToken(userDto, confirmationToken, uri);
        emailSenderService.sendMessageToAdmins(userDto, uri);

        model.addAttribute("success", "success");
        return new ModelAndView("redirect:/login", model);

    }

}
