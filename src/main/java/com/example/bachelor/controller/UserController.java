package com.example.bachelor.controller;

import com.example.bachelor.data.dto.UserDetailsPrincipal;
import com.example.bachelor.data.dto.UserDto;
import com.example.bachelor.data.entities.UserEntity;
import com.example.bachelor.services.UserService;
import com.example.bachelor.utility.constants.HtmlConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.nio.file.attribute.UserPrincipal;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public String getUsers(Model model) {
        model.addAttribute("users", userService.readAllUsers());
        return HtmlConstants.USER_OVERVIEW;
    }

    @GetMapping("/profile")
    public String getUserProfile(Model model) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String userEmail = userDetails.getUsername();
        UserDto userDto = userService.findUserDtoByEmail(userEmail);

        if (userDto == null) {
            throw new IllegalStateException("kein User gefunden"); // TODO Fehlerbehandlung
        }

        model.addAttribute("user", userDto);
        return HtmlConstants.USERPROFILE;
    }
}
