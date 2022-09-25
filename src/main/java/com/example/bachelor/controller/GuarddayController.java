package com.example.bachelor.controller;

import com.example.bachelor.data.dto.GuardDayDto;
import com.example.bachelor.data.dto.UserDto;
import com.example.bachelor.data.dto.UserGuardingRelationDto;
import com.example.bachelor.services.GuardDayService;
import com.example.bachelor.services.UserService;
import com.example.bachelor.utility.constants.HtmlConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@SessionAttributes({"guarddaydto"})
public class GuarddayController {

    @Autowired
    GuardDayService guardDayService;

    @Autowired
    UserService userService;

    @GetMapping("/guardday_creation")
    public String getGuardDayCreation(Model model) {

        GuardDayDto guardDayDto = new GuardDayDto();
        model.addAttribute("guarddaydto", guardDayDto);

        return HtmlConstants.GUARDDAY_CREATION;
    }

    @PostMapping("/guardday_creation")
    public String saveGuardDayCreation(Model model,
                                       @ModelAttribute(name="guarddaydto") GuardDayDto guardDayDto) {

        guardDayService.saveGuardDayDto(guardDayDto);

        return HtmlConstants.REDIRECT + HtmlConstants.GUARDDAY_OVERVIEW;
    }

    @GetMapping("/guardday_overview")
    public String getGuardDayOverview(Model model) {

        List<GuardDayDto> guardDays = guardDayService.readAllGuardDays();
        model.addAttribute("guarddays", guardDays);

        return HtmlConstants.GUARDDAY_OVERVIEW;
    }

    @GetMapping("/guardday_execution/{id}")
    public String getGuardDayExecution(Model model, @PathVariable Long id) {

        //Wachtag anhängen
        GuardDayDto guardDay = guardDayService.readGuardDayByIdWithUsers(id);
        //TODO: Fehlerbehandlung wenn keiner gefunden wird
        model.addAttribute("guarddaydto", guardDay);

        //Aktuelle Zeit anhängen
        Date currentTime = new Date();
        model.addAttribute("currentTime", currentTime);

        return HtmlConstants.GUARDDAY_EXECUTION;
    }

    @PostMapping("/guardday_execution/saveUser")
    public String saveUser(Model model,
                           @ModelAttribute(name="guarddaydto") GuardDayDto guardDayDto) {


        model.addAttribute("guarddaydto", guardDayDto);
        if (guardDayDto.getUserToSave() == null) {
            //TODO: throw error
        }

        Long userIdToSave = guardDayDto.getUserToSave();
        UserGuardingRelationDto userGuardingRelationDto = new UserGuardingRelationDto();
        UserDto userToSave = guardDayDto.getAllUsers().stream().filter(n -> n.getUserId().equals(userIdToSave)).findFirst().orElse(null);
        if (userToSave == null) {
            //TODO: throw error
        }

        if (guardDayDto.getActualStartTime() != null) {
            userGuardingRelationDto.setGuardingStart(new Date());
        } else {
            userGuardingRelationDto.setBooked(true);
        }

        userGuardingRelationDto.setGuardDayId(guardDayDto.getGuardDayId());
        userGuardingRelationDto.setUserDto(userToSave);
        userGuardingRelationDto.setUserId(userIdToSave);

        guardDayService.saveUserGuardingRelationDto(userGuardingRelationDto);

        return HtmlConstants.REDIRECT + HtmlConstants.GUARDDAY_EXECUTION + "/" + guardDayDto.getGuardDayId();
    }





}
