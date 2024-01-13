package com.example.bachelor.controller;

import com.example.bachelor.data.dto.UserDetailsPrincipal;
import com.example.bachelor.data.dto.UserDto;
import com.example.bachelor.data.dto.UserStatisticsDto;
import com.example.bachelor.data.entities.UserEntity;
import com.example.bachelor.services.EmailSenderService;
import com.example.bachelor.services.GuardDayService;
import com.example.bachelor.services.UserService;
import com.example.bachelor.utility.constants.HtmlConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.nio.file.attribute.UserPrincipal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@SessionAttributes({"user"})
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private GuardDayService guardDayService;

    @Autowired
    private EmailSenderService emailSenderService;

    @GetMapping("/users")
    public String getUsers(Model model) {

        List<UserDto> allUsers = userService.readAllUserDtos();
        model.addAttribute("users", allUsers);

        Map<Long, UserStatisticsDto> userStatisticsMap = new HashMap<>();
        for (UserDto user : allUsers) {
            userStatisticsMap.put(user.getUserId(), guardDayService.getUserStatisticsDto(user.getUserId()));
        }
        model.addAttribute("statisticsmap", userStatisticsMap);

        return HtmlConstants.USER_OVERVIEW;
    }

    @PreAuthorize("#query == principal.getUserId().toString() ||" +
            "hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/user/{query}")
    public String searchUser(@PathVariable("query") String query, Model model) {

        if (!isLong(query)) {
            throw new IllegalArgumentException("id  muss nummer sein");
        }
        UserDto user = userService.readUserDtoById(Long.parseLong(query));

        if (user == null) {
            throw new IllegalStateException("User not found");
        }

        model.addAttribute("user", user);

        UserStatisticsDto userStatisticsDto = guardDayService.getUserStatisticsDto(user.getUserId());
        model.addAttribute("statistics", userStatisticsDto);

        return HtmlConstants.USERPROFILE;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(value = "/activateUser")
    public String activateUser(@ModelAttribute("user") UserDto user, Model model) {

        user.setActive(true);

        userService.saveUserDto(user);

        model.addAttribute("user", user);

        UserStatisticsDto userStatisticsDto = guardDayService.getUserStatisticsDto(user.getUserId());
        model.addAttribute("statistics", userStatisticsDto);

        String uri = ServletUriComponentsBuilder.fromCurrentContextPath().build().toString();
        emailSenderService.sendActivationNotice(user, uri);

        return HtmlConstants.REDIRECT + "user/" + user.getUserId();
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

        UserStatisticsDto userStatisticsDto = guardDayService.getUserStatisticsDto(userDto.getUserId());
        model.addAttribute("statistics", userStatisticsDto);

        return HtmlConstants.USERPROFILE;
    }

    static boolean isLong(String toCheck) {
        try {
            Long.parseLong(toCheck);
        } catch (NumberFormatException numberFormatException) {
            return false;
        }
        return true;
    }
}
