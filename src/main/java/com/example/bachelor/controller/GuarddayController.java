package com.example.bachelor.controller;

import com.example.bachelor.data.dto.GuardDayDto;
import com.example.bachelor.services.GuardDayService;
import com.example.bachelor.utility.constants.HtmlConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class GuarddayController {

    @Autowired
    GuardDayService guardDayService;

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



}
