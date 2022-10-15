package com.example.bachelor.controller;

import com.example.bachelor.data.dto.GuardDayDto;
import com.example.bachelor.data.dto.JournalEntryDto;
import com.example.bachelor.data.dto.UserDto;
import com.example.bachelor.data.dto.UserGuardingRelationDto;
import com.example.bachelor.data.enums.EntryType;
import com.example.bachelor.services.GuardDayService;
import com.example.bachelor.services.UserService;
import com.example.bachelor.utility.constants.HtmlConstants;
import com.example.bachelor.utility.helper.JournalHelper;
import com.example.bachelor.utility.weatherapi.WeatherAPI;
import com.example.bachelor.utility.weatherapi.WeatherApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@SessionAttributes({"guarddaydto"})
public class GuarddayController {

    @Autowired
    WeatherAPI weatherApi;

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
                                       @ModelAttribute(name = "guarddaydto") GuardDayDto guardDayDto) {

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

        WeatherApiResult currentWeatherData = weatherApi.getCurrentWeatherData();
        model.addAttribute("currentWeatherData", currentWeatherData);

        //Aktuelle Zeit anhängen
        Date currentTime = new Date();
        model.addAttribute("currentTime", currentTime);

        return HtmlConstants.GUARDDAY_EXECUTION;
    }

    @PostMapping("/guardday_execution/saveUser")
    public String saveUser(Model model,
                           @ModelAttribute(name = "guarddaydto") GuardDayDto guardDayDto) {


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
            JournalEntryDto journalEntryDto = JournalHelper.createJournalEntry(guardDayDto.getGuardDayId(), EntryType.USER_GUARD_BEGIN, null, null, userToSave);

            guardDayDto.getJournalEntries().add(journalEntryDto);
        } else {
            userGuardingRelationDto.setBooked(true);
        }

        userGuardingRelationDto.setGuardDayId(guardDayDto.getGuardDayId());
        userGuardingRelationDto.setUserDto(userToSave);
        userGuardingRelationDto.setUserId(userIdToSave);

        guardDayService.saveUserGuardingRelationDto(userGuardingRelationDto);
        guardDayService.saveGuardDayDto(guardDayDto);

        return HtmlConstants.REDIRECT + HtmlConstants.GUARDDAY_EXECUTION + "/" + guardDayDto.getGuardDayId();
    }

    @PostMapping("/guardday_execution/startEndGuardday")
    public String startEndGuardday(Model model,
                                   @ModelAttribute(name = "guarddaydto") GuardDayDto guardDayDto) {

        EntryType entryType;

        //Wenn noch keine Startzeit gesetzt wurde muss 'Wachstart' gedrückt worden sein
        if (guardDayDto.getActualStartTime() == null) {
            guardDayDto.setActualStartTime(new Date());
            entryType = EntryType.GUARD_BEGIN;

        } else {
            guardDayDto.setActualEndTime(new Date());
            entryType = EntryType.GUARD_END;
            endUserRelations(guardDayDto);
        }
        WeatherApiResult weatherApiResult = weatherApi.getCurrentWeatherData();
        JournalEntryDto journalEntryDtoWeather = JournalHelper.createJournalEntry(guardDayDto.getGuardDayId(), EntryType.WEATHER, null, weatherApiResult, null);

        JournalEntryDto journalEntryDto = JournalHelper.createJournalEntry(guardDayDto.getGuardDayId(), entryType, null, null, null);
        guardDayDto.getJournalEntries().add(journalEntryDto);
        guardDayDto.getJournalEntries().add(journalEntryDtoWeather);
        guardDayService.saveGuardDayDto(guardDayDto);

        return HtmlConstants.REDIRECT + HtmlConstants.GUARDDAY_EXECUTION + "/" + guardDayDto.getGuardDayId();
    }

    private void endUserRelations(GuardDayDto guardDayDto) {

        for (UserGuardingRelationDto relation : guardDayDto.getUserGuardingRelations()) {
            if (relation.getGuardingEnd() == null) {
                relation.setGuardingEnd(guardDayDto.getActualEndTime());
                guardDayService.saveUserGuardingRelationDto(relation);
                JournalEntryDto journalEntryDto = JournalHelper.createJournalEntry(guardDayDto.getGuardDayId(), EntryType.USER_GUARD_END, null, null, relation.getUserDto());
                guardDayDto.getJournalEntries().add(journalEntryDto);
            }
        }

    }

    @PostMapping("/guardday_execution/saveWatertemp")
    public String saveWatertemp(Model model,
                                @ModelAttribute(name = "guarddaydto") GuardDayDto guardDayDto,
                                Authentication authentication) {

        if (guardDayDto.getWaterTemp() == null || guardDayDto.getWaterTemp().isEmpty()) {
            //TODO: ERROR
        } else {
            JournalEntryDto journalEntryDto = JournalHelper.createJournalEntry(guardDayDto.getGuardDayId(), EntryType.WATER_TEMP, guardDayDto.getWaterTemp(), null, null);
            guardDayDto.getJournalEntries().add(journalEntryDto);
            guardDayService.saveGuardDayDto(guardDayDto);
        }

        return HtmlConstants.REDIRECT + HtmlConstants.GUARDDAY_EXECUTION + "/" + guardDayDto.getGuardDayId();
    }

    @PostMapping("/guardday_execution/saveJournalEntry")
    public String saveJournalEntry(@ModelAttribute(name = "guarddaydto") GuardDayDto guardDayDto,
                                   Authentication authentication) {

        if (guardDayDto.getJournalDescription() == null || guardDayDto.getJournalDescription().isEmpty()) {
            //TODO: ERROR
        } else {
            JournalEntryDto journalEntryDto = JournalHelper.createJournalEntry(guardDayDto.getGuardDayId(), EntryType.DEFAULT, guardDayDto.getJournalDescription(), null, null);
            guardDayDto.getJournalEntries().add(journalEntryDto);
            guardDayService.saveGuardDayDto(guardDayDto);
        }

        return HtmlConstants.REDIRECT + HtmlConstants.GUARDDAY_EXECUTION + "/" + guardDayDto.getGuardDayId();
    }

    @RequestMapping(value = "/guardday_execution/deleteUser/{id}")
    private String deleteUser(@ModelAttribute(name = "guarddaydto") GuardDayDto guardDayDto,
                              @PathVariable(name = "id") Long relationId) {
        System.out.println("Student_Id : " + relationId);

        List<UserGuardingRelationDto> allRelations = new ArrayList<>();
        allRelations.addAll(guardDayDto.getUserGuardingRelationsBooked());
        allRelations.addAll(guardDayDto.getUserGuardingRelations());

        UserGuardingRelationDto guardingRelationDto = allRelations.stream().filter(n -> relationId.equals(n.getRelationId())).findFirst().orElse(null);

        if (guardingRelationDto == null) {
            throw new IllegalStateException("Zu löschende Relation existiert nicht");
        }

        //Wenn nur gebucht dann löschen wir direkt
        if (guardingRelationDto.isBooked()) {
            guardDayService.deleteUserGuardingRelation(guardingRelationDto.getRelationId());
        } else {
            //Wachende setzen und speichern
            guardingRelationDto.setGuardingEnd(new Date());
            guardDayService.saveUserGuardingRelationDto(guardingRelationDto);
            //Wachbucheintrag schreiben und speichern
            JournalEntryDto journalEntryDto = JournalHelper.createJournalEntry(guardDayDto.getGuardDayId(), EntryType.USER_GUARD_END, null, null, guardingRelationDto.getUserDto());
            guardDayDto.getJournalEntries().add(journalEntryDto);
            guardDayService.saveGuardDayDto(guardDayDto);
        }

        return HtmlConstants.REDIRECT + HtmlConstants.GUARDDAY_EXECUTION + "/" + guardDayDto.getGuardDayId();
    }

}
