package com.example.bachelor.utility.helper;

import com.example.bachelor.data.dto.JournalEntryDto;
import com.example.bachelor.data.dto.UserDetailsPrincipal;
import com.example.bachelor.data.dto.UserDto;
import com.example.bachelor.data.enums.EntryType;
import com.example.bachelor.utility.weatherapi.WeatherApiResult;
import org.apache.catalina.User;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Date;

public final class JournalHelper {

    private JournalHelper() {};

    public static JournalEntryDto createJournalEntry(Long guardDayId, EntryType entryType,
                                                     String description, WeatherApiResult weatherApiResult,
                                                     UserDto userBooked) {
        JournalEntryDto journalEntryDto = new JournalEntryDto();
        //TODO: Hier NutzerId als Parameter übergeben, wenn null dann aus Principal
        UserDetailsPrincipal principal = (UserDetailsPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        journalEntryDto.setUserId(principal.getUserId());
        journalEntryDto.setGuardDayId(guardDayId);
        journalEntryDto.setEntryType(entryType);
        journalEntryDto.setCreation(new Date());
        journalEntryDto.setDescription(createJournalDescription(entryType, description, weatherApiResult, userBooked));

        return journalEntryDto;
    }

    private static String createJournalDescription(EntryType entryType,
                                                   String description,
                                                   WeatherApiResult weatherApiResult,
                                                   UserDto userBooked) {

        switch (entryType) {
            case DEFAULT:
            case INCIDENT:
                return description;
            case WATER_TEMP:
                return "Wassertemperatur: " + description + "° C";
            case USER_GUARD_BEGIN:
                return "Wachanmeldung " + userBooked.getFirstName() + " " + userBooked.getLastName();
            case USER_GUARD_END:
                return "Wachabmeldung " + userBooked.getFirstName() + " " + userBooked.getLastName();
            case GUARD_BEGIN:
                return "Wachstart";
            case GUARD_END:
                return "Wachende";
            case WEATHER:
                return "Wetter: Temperatur: " + weatherApiResult.getTemp() + "° C, Windgeschwindigkeit: "
                        + weatherApiResult.getWindspeed() + " kmh, Regenwarscheinlichkeit: "
                        + weatherApiResult.getPrecip() + " %, Luftfeuchte: " + weatherApiResult.getHumidity()
                        + " %, Beschreibung: " + weatherApiResult.getConditions();
            case ILS_ACTIVE:
                return "Anmeldung ILS";
            case ILS_INACTIVE:
                return "Abmeldung ILS";
            default:
                return "";
        }
    }
}
