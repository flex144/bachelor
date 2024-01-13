package com.example.bachelor.controller;

import com.example.bachelor.data.dto.*;
import com.example.bachelor.data.enums.EntryType;
import com.example.bachelor.services.GuardDayPDFExportService;
import com.example.bachelor.services.GuardDayService;
import com.example.bachelor.services.UserService;
import com.example.bachelor.utility.constants.HtmlConstants;
import com.example.bachelor.utility.exceptions.UserAlreadyExistsException;
import com.example.bachelor.utility.helper.JournalHelper;
import com.example.bachelor.utility.weatherapi.WeatherAPI;
import com.example.bachelor.utility.weatherapi.WeatherApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@SessionAttributes({"guarddaydto", "guarddayseries"})
public class GuarddayController {

    @Autowired
    WeatherAPI weatherApi;

    @Autowired
    GuardDayService guardDayService;

    @Autowired
    UserService userService;

    @Autowired
    GuardDayPDFExportService guardDayPDFExportService;

    @GetMapping("/guardday_creation")
    public String getGuardDayCreation(Model model) {

        GuardDayDto guardDayDto = new GuardDayDto();
        model.addAttribute("guarddaydto", guardDayDto);

        GuardDaySeriesDto guardDaySeriesDto = new GuardDaySeriesDto();
        model.addAttribute("guarddayseries", guardDaySeriesDto);

        return HtmlConstants.GUARDDAY_CREATION;
    }

    @PostMapping("/guardday_creation_single")
    public String saveGuardDayCreation(@ModelAttribute(name = "guarddaydto") GuardDayDto guardDayDto) {

        guardDayService.saveGuardDayDto(guardDayDto);

        return HtmlConstants.REDIRECT + HtmlConstants.GUARDDAY_OVERVIEW;
    }

    @PostMapping("/guardday_creation_series")
    public String saveGuardDayCreationSeries(@ModelAttribute(name = "guarddayseries") GuardDaySeriesDto guardDaySeries) {

        guardDayService.saveGuardDaySeries(guardDaySeries);

        return HtmlConstants.REDIRECT + HtmlConstants.GUARDDAY_OVERVIEW;
    }

    @GetMapping("/guardday_overview")
    public String getGuardDayOverview(Model model) {

        List<GuardDayDto> guardDays = guardDayService.readAllGuardDays();
        model.addAttribute("guarddays", guardDays);
        model.addAttribute("events", getEventsFromGuardDays(guardDays));

        return HtmlConstants.GUARDDAY_OVERVIEW;
    }

    private String getEventsFromGuardDays(List<GuardDayDto> guardDays) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (GuardDayDto guardDayDto : guardDays) {
            sb.append("{ ");
            sb.append(mapGuardDayToJson(guardDayDto));
            sb.append("}, ");
        }

        sb.delete(sb.length() -2, sb.length());
        sb.append("]");
        return sb.toString();
    }

    private String mapGuardDayToJson(GuardDayDto guardDayDto) {
        StringBuilder sb = new StringBuilder();

        long guardingDate = guardDayDto.getGuardingDate().getTime() / 1000;
        long guardingStart = guardingDate + (guardDayDto.getStartTime().getTime() / 1000) + 3600;
        long guardingEnd = guardingDate + (guardDayDto.getEndTime().getTime() / 1000) + 3600;

        sb.append("start: ");
        sb.append("'").append(guardingStart).append("', ");
        sb.append("end: ");
        sb.append("'").append(guardingEnd).append("', ");
        sb.append("title: ");
        sb.append("'Wachtag-").append(guardDayDto.getGuardDayId()).append("', ");
        sb.append("content: ");
        sb.append("'<button onclick=\"rowClicked(").append(guardDayDto.getGuardDayId()).append(")\"> Wachtag öffnen </button>',");
        sb.append("category: ");
        sb.append("'").append(getGuardDayCategory(guardDayDto)).append("'");
        return sb.toString();
    }

    private String getGuardDayCategory(GuardDayDto guardDayDto) {
        if (guardDayDto.getActualEndTime() != null) {
            return "Geschlossen";
        } else if (guardDayDto.getUserGuardingRelationsBooked() == null || guardDayDto.getUserGuardingRelationsBooked().isEmpty()) {
            return "Ungebucht";
        } else {
            return "Gebucht";
        }
    }

    @GetMapping("/guardday_execution/{id}")
    public String getGuardDayExecution(Model model, @PathVariable Long id,
                                       RedirectAttributes redirectAttributes) {

        //Wachtag anhängen
        GuardDayDto guardDay = guardDayService.readGuardDayByIdWithUsers(id);
        if (guardDay == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Wachtag existiert nicht!");
            return HtmlConstants.REDIRECT + HtmlConstants.GUARDDAY_OVERVIEW;
        }
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
                           RedirectAttributes redirectAttributes,
                           @ModelAttribute(name = "guarddaydto") GuardDayDto guardDayDto) {

        final String redirectAdress = HtmlConstants.REDIRECT + HtmlConstants.GUARDDAY_EXECUTION + "/" + guardDayDto.getGuardDayId();

        model.addAttribute("guarddaydto", guardDayDto);
        if (guardDayDto.getUserToSave() == null && (guardDayDto.getFreetextUser() == null || guardDayDto.getFreetextUser().isEmpty())) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ein Nutzer muss ausgewählt oder eingegeben werden!");
        } else if (guardDayDto.getUserToSave() != null && guardDayDto.getFreetextUser() != null && !guardDayDto.getFreetextUser().isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Es darf nicht ein Nutzer aus der Liste gewählt und gleichzeitig eingegeben werden!");
        }
        if (redirectAttributes.getFlashAttributes().containsKey("errorMessage")) {
            return redirectAdress;
        }

        UserGuardingRelationDto relationDto = null;

        if (guardDayDto.getUserToSave() != null) {
            try {
                relationDto = createRegisteredUserRelation(guardDayDto);
            } catch (UserAlreadyExistsException e) {
                redirectAttributes.addFlashAttribute("errorMessage", e.getLocalizedMessage());
                return redirectAdress;
            }

            if (relationDto == null) {
                redirectAttributes.addFlashAttribute("errorMessage", "Nutzer konnte nicht gespeichert werden!");
                return redirectAdress;
            }
        }

        if (guardDayDto.getFreetextUser() != null && !guardDayDto.getFreetextUser().isEmpty()) {
            relationDto = createFreetextUserRelation(guardDayDto);
        }

        guardDayService.saveUserGuardingRelationDto(relationDto);
        guardDayService.saveGuardDayDto(guardDayDto);

        return redirectAdress;
    }

    private UserGuardingRelationDto createRegisteredUserRelation(GuardDayDto guardDayDto) throws UserAlreadyExistsException {
        UserGuardingRelationDto userGuardingRelationDto = new UserGuardingRelationDto();

        Long userIdToSave = guardDayDto.getUserToSave();
        UserDto userToSave = guardDayDto.getAllUsers().stream().filter(n -> n.getUserId().equals(userIdToSave)).findFirst().orElse(null);
        if (userToSave == null) {
            return null;
        }

        if (guardDayDto.getActualStartTime() != null) {
            //Prüfen ob Nutzer bereits vorhanden
            UserGuardingRelationDto existingRelation = guardDayDto.getUserGuardingRelations().stream().filter(n -> guardDayDto.getUserToSave().equals(n.getUserId()) && n.getGuardingEnd() == null).findFirst().orElse(null);

            if (existingRelation != null) {
                throw new UserAlreadyExistsException("Nutzer wurde bereits gespeichert!");
            }

            userGuardingRelationDto.setGuardingStart(new Date());
            JournalEntryDto journalEntryDto = JournalHelper.createJournalEntry(guardDayDto.getGuardDayId(), EntryType.USER_GUARD_BEGIN, null, null, userToSave, null);

            guardDayDto.getJournalEntries().add(journalEntryDto);
        } else {
            //Prüfen ob Nutzer bereits vorhanden
            UserGuardingRelationDto existingRelation = guardDayDto.getUserGuardingRelationsBooked().stream().filter(n -> guardDayDto.getUserToSave().equals(n.getUserId())).findFirst().orElse(null);

            if (existingRelation != null) {
                throw new UserAlreadyExistsException("Nutzer wurde bereits gespeichert!");
            }

            userGuardingRelationDto.setBooked(true);
        }

        userGuardingRelationDto.setGuardDayId(guardDayDto.getGuardDayId());
        userGuardingRelationDto.setUserDto(userToSave);
        userGuardingRelationDto.setUserId(userIdToSave);

        return userGuardingRelationDto;
    }

    private UserGuardingRelationDto createFreetextUserRelation(GuardDayDto guardDayDto) {
        UserGuardingRelationDto userGuardingRelationDto = new UserGuardingRelationDto();

        userGuardingRelationDto.setUserFreetext(guardDayDto.getFreetextUser());

        if (guardDayDto.getActualStartTime() != null) {
            userGuardingRelationDto.setGuardingStart(new Date());
            JournalEntryDto journalEntryDto = JournalHelper.createJournalEntry(guardDayDto.getGuardDayId(), EntryType.USER_GUARD_BEGIN, guardDayDto.getFreetextUser(), null, null, null);

            guardDayDto.getJournalEntries().add(journalEntryDto);
        } else {
            userGuardingRelationDto.setBooked(true);
        }

        userGuardingRelationDto.setGuardDayId(guardDayDto.getGuardDayId());

        return userGuardingRelationDto;
    }

    @PostMapping("/guardday_execution/startEndGuardday")
    public String startEndGuardday(@ModelAttribute(name = "guarddaydto") GuardDayDto guardDayDto) throws InterruptedException {

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

        JournalEntryDto journalEntryDto = JournalHelper.createJournalEntry(guardDayDto.getGuardDayId(), entryType, null, null, null, null);
        guardDayDto.getJournalEntries().add(journalEntryDto);
        guardDayService.saveGuardDayDto(guardDayDto);

        if (guardDayDto.getActualEndTime() == null) {
            guardDayService.writeWeatherAPIResults(guardDayDto.getGuardDayId());
        }

        return HtmlConstants.REDIRECT + HtmlConstants.GUARDDAY_EXECUTION + "/" + guardDayDto.getGuardDayId();
    }

    private void endUserRelations(GuardDayDto guardDayDto) {

        for (UserGuardingRelationDto relation : guardDayDto.getUserGuardingRelations()) {
            if (relation.getGuardingEnd() == null) {
                relation.setGuardingEnd(guardDayDto.getActualEndTime());
                guardDayService.saveUserGuardingRelationDto(relation);
                JournalEntryDto journalEntryDto = JournalHelper.createJournalEntry(guardDayDto.getGuardDayId(), EntryType.USER_GUARD_END, relation.getUserFreetext(), null, relation.getUserDto(), null);
                guardDayDto.getJournalEntries().add(journalEntryDto);
            }
        }

    }

    @PostMapping("/guardday_execution/saveWatertemp")
    public String saveWatertemp(RedirectAttributes redirectAttributes,
                                @ModelAttribute(name = "guarddaydto") GuardDayDto guardDayDto) {

        if (guardDayDto.getWaterTemp() == null || guardDayDto.getWaterTemp().isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Es muss eine Wassertemperatur eingegeben werden!");
        } else {
            JournalEntryDto journalEntryDto = JournalHelper.createJournalEntry(guardDayDto.getGuardDayId(), EntryType.WATER_TEMP, guardDayDto.getWaterTemp(), null, null, null);
            guardDayDto.getJournalEntries().add(journalEntryDto);
            guardDayService.saveGuardDayDto(guardDayDto);
        }

        return HtmlConstants.REDIRECT + HtmlConstants.GUARDDAY_EXECUTION + "/" + guardDayDto.getGuardDayId();
    }

    @PostMapping("/guardday_execution/saveJournalEntry")
    public String saveJournalEntry(@ModelAttribute(name = "guarddaydto") GuardDayDto guardDayDto,
                                   RedirectAttributes redirectAttributes) {

        if (guardDayDto.getJournalDescription() == null || guardDayDto.getJournalDescription().isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Es muss eine Beschreibung eingegeben werden!");
        } else {
            JournalEntryDto journalEntryDto = JournalHelper.createJournalEntry(guardDayDto.getGuardDayId(), EntryType.DEFAULT, guardDayDto.getJournalDescription(), null, null, guardDayDto.getUserToSave());
            guardDayDto.getJournalEntries().add(journalEntryDto);
            guardDayService.saveGuardDayDto(guardDayDto);
        }

        return HtmlConstants.REDIRECT + HtmlConstants.GUARDDAY_EXECUTION + "/" + guardDayDto.getGuardDayId();
    }

    @RequestMapping(value = "/guardday_execution/deleteUser/{id}")
    public String deleteUser(@ModelAttribute(name = "guarddaydto") GuardDayDto guardDayDto,
                              @PathVariable(name = "id") Long relationId) {

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
            JournalEntryDto journalEntryDto;
            if (guardingRelationDto.getUserId() != null) {
                journalEntryDto = JournalHelper.createJournalEntry(guardDayDto.getGuardDayId(), EntryType.USER_GUARD_END, null, null, guardingRelationDto.getUserDto(), null);
            } else {
                journalEntryDto = JournalHelper.createJournalEntry(guardDayDto.getGuardDayId(), EntryType.USER_GUARD_END, guardingRelationDto.getUserFreetext(), null, null, null);
            }
            guardDayDto.getJournalEntries().add(journalEntryDto);
            guardDayService.saveGuardDayDto(guardDayDto);
        }

        return HtmlConstants.REDIRECT + HtmlConstants.GUARDDAY_EXECUTION + "/" + guardDayDto.getGuardDayId();
    }

    @RequestMapping(value = "/guardday_execution/transferUser/{id}")
    public String transferUser(RedirectAttributes redirectAttributes,
                               @ModelAttribute(name = "guarddaydto") GuardDayDto guardDayDto,
                               @PathVariable(name = "id") Long relationId) {

        final String redirectAdress = HtmlConstants.REDIRECT + HtmlConstants.GUARDDAY_EXECUTION + "/" + guardDayDto.getGuardDayId();

        UserGuardingRelationDto guardingRelationDto = guardDayDto.getUserGuardingRelationsBooked().stream().filter(n -> relationId.equals(n.getRelationId())).findFirst().orElse(null);

        if (guardingRelationDto == null) {
            throw new IllegalStateException("Zu übertragende Relation existiert nicht");
        } else if (guardingRelationDto.getUserId() != null) {
            UserGuardingRelationDto guardingRelationDtoPresent = guardDayDto.getUserGuardingRelations().stream().filter(n -> guardingRelationDto.getUserId().equals(n.getUserId())).findFirst().orElse(null);
            if (guardingRelationDtoPresent != null) {
                redirectAttributes.addFlashAttribute("errorMessage", "Nutzer bereits gespeichert!");
                return redirectAdress;
            }
        }

        UserGuardingRelationDto relationDto = null;
        if (guardingRelationDto.getUserId() != null) {
            guardDayDto.setUserToSave(guardingRelationDto.getUserId());
            relationDto = createRegisteredUserRelation(guardDayDto);
            if (relationDto == null) {
                redirectAttributes.addFlashAttribute("errorMessage", "Nutzer konnte nicht gespeichert werden!");
                return redirectAdress;
            }
        }

        if (guardingRelationDto.getUserFreetext() != null && !guardingRelationDto.getUserFreetext().isEmpty()) {
            guardDayDto.setFreetextUser(guardingRelationDto.getUserFreetext());
            relationDto = createFreetextUserRelation(guardDayDto);
        }

        guardDayService.saveUserGuardingRelationDto(relationDto);
        guardDayService.saveGuardDayDto(guardDayDto);

        return redirectAdress;
    }

    @PostMapping("/guardday_execution/changeIlsActivity")
    public String changeIlsActivity(@ModelAttribute(name = "guarddaydto") GuardDayDto guardDayDto) {

        //Wenn ILS aktiv ist, setzen wir es auf inaktiv
        EntryType entryType = guardDayDto.isIlsActive() ? EntryType.ILS_INACTIVE : EntryType.ILS_ACTIVE;

        JournalEntryDto journalEntryDto = JournalHelper.createJournalEntry(guardDayDto.getGuardDayId(), entryType, null, null, null, null);
        guardDayDto.getJournalEntries().add(journalEntryDto);
        guardDayDto.setIlsActive(!guardDayDto.isIlsActive());
        guardDayService.saveGuardDayDto(guardDayDto);

        return HtmlConstants.REDIRECT + HtmlConstants.GUARDDAY_EXECUTION + "/" + guardDayDto.getGuardDayId();
    }

    @RequestMapping("/exportToPdf")
    public String exportGuardDayToPdf(HttpServletResponse response,
                                    RedirectAttributes redirectAttributes,
                                    @ModelAttribute(name = "guarddaydto") GuardDayDto guardDayDto) throws IOException {
        response.setContentType("application/pdf");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=Wachtag-" + currentDateTime + ".pdf";
        response.setHeader(headerKey, headerValue);

        try {
            guardDayPDFExportService.export(response, guardDayDto);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getLocalizedMessage());

        }
        return HtmlConstants.REDIRECT + HtmlConstants.GUARDDAY_EXECUTION + "/" + guardDayDto.getGuardDayId();

    }

}
