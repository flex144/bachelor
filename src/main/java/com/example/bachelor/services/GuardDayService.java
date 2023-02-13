package com.example.bachelor.services;

import com.example.bachelor.data.dto.*;
import com.example.bachelor.data.entities.GuardDayEntity;
import com.example.bachelor.data.entities.JournalEntryEntity;
import com.example.bachelor.data.entities.UserEntity;
import com.example.bachelor.data.entities.UserGuardingRelationEntity;
import com.example.bachelor.data.enums.EntryType;
import com.example.bachelor.repositories.GuardDayRepository;
import com.example.bachelor.repositories.UserGuardingRelationRepository;
import com.example.bachelor.utility.helper.JournalHelper;
import com.example.bachelor.utility.mapper.GuardDayMapper;
import com.example.bachelor.utility.weatherapi.WeatherAPI;
import com.example.bachelor.utility.weatherapi.WeatherApiResult;
import org.apache.el.lang.ELArithmetic;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static java.time.DayOfWeek.SUNDAY;

@Service
public class GuardDayService {

    @Autowired
    private GuardDayRepository guardDayRepository;

    @Autowired
    private JournalService journalService;

    @Autowired
    private UserService userService;

    @Autowired
    private GuardDayMapper guardDayMapper;

    @Autowired
    private UserGuardingRelationRepository userGuardingRelationRepository;

    @Autowired
    private WeatherAPI weatherApi;

    public GuardDayDto readGuardDayById(Long guardDayId) {
        GuardDayEntity guardDayEntity = guardDayRepository.findById(guardDayId).orElse(null);

        if (guardDayEntity == null) {
            return null;
        }

        return guardDayMapper.mapGuardDayEntityToDto(guardDayEntity);

    }

    public GuardDayDto readGuardDayByIdWithUsers(Long guardDayId) {
        GuardDayDto guardDayDto = readGuardDayById(guardDayId);

        if (guardDayDto == null) {
            return null;
        }

        guardDayDto.setAllUsers(userService.readAllUserDtos());
        List<UserGuardingRelationDto> allRelations = readUserGuardingRelations(guardDayId);
        guardDayDto.setUserGuardingRelations(allRelations.stream().filter(n -> !n.isBooked() && n.getGuardingEnd() == null).collect(Collectors.toList()));
        guardDayDto.setUserGuardingRelationsBooked(allRelations.stream().filter(n -> n.isBooked()).collect(Collectors.toList()));

        return guardDayDto;
    }

    public List<GuardDayDto> readAllGuardDays() {

        Iterable<GuardDayEntity> guardDays = guardDayRepository.findAll();
        List<GuardDayDto> guardDayDtos = new ArrayList<>();

        for (GuardDayEntity guardDayEntity : guardDays) {
            GuardDayDto guardDayDto = guardDayMapper.mapGuardDayEntityToDto(guardDayEntity);
            List<UserGuardingRelationDto> allRelations = readUserGuardingRelations(guardDayDto.getGuardDayId());
            guardDayDto.setUserGuardingRelations(allRelations.stream().filter(n -> !n.isBooked() && n.getGuardingEnd() == null).collect(Collectors.toList()));
            guardDayDto.setUserGuardingRelationsBooked(allRelations.stream().filter(n -> n.isBooked()).collect(Collectors.toList()));

            guardDayDtos.add(guardDayDto);
        }

        return guardDayDtos;
    }

    public GuardDayEntity saveGuardDayEntity(GuardDayEntity guardDayEntity) {

        guardDayRepository.save(guardDayEntity);

        if (guardDayEntity.getJournalEntries() != null) {
            for (JournalEntryEntity journalEntryEntity : guardDayEntity.getJournalEntries()) {
                journalEntryEntity.setGuardDay(guardDayEntity);
                journalService.saveJournalEntry(journalEntryEntity);
            }
        }

        return guardDayEntity;
    }

    public void saveGuardDayDto(GuardDayDto guardDayDto) {
        saveGuardDayEntity(guardDayMapper.mapGuardDayDtoToEntity(guardDayDto));
    }

    public void saveUserGuardingRelationEntity(UserGuardingRelationEntity userGuardingRelationEntity) {
        userGuardingRelationRepository.save(userGuardingRelationEntity);
    }

    public void saveUserGuardingRelationDto(UserGuardingRelationDto userGuardingRelationDto) {
        saveUserGuardingRelationEntity(guardDayMapper.mapUserGuardingRelationDtoToEntity(userGuardingRelationDto));
    }

    private List<UserGuardingRelationEntity> readUserGuardingRelationEntities(Long guardDayId) {
        List<UserGuardingRelationEntity> result = new ArrayList<>();
        userGuardingRelationRepository.findUserGuardingRelationEntitiesByGuardDayId(guardDayId).forEach(result::add);
        return result;
    }

    public List<UserGuardingRelationDto> readUserGuardingRelations(Long guardDayId) {
        List<UserGuardingRelationDto> result = new ArrayList<>();
        readUserGuardingRelationEntities(guardDayId).forEach(n -> result.add(guardDayMapper.mapUserGuardingRelationEntityToDto(n)));

        return result;
    }

    public List<UserGuardingRelationDto> readUserGuardingRelationsByUserId(Long userId) {
        List<UserGuardingRelationDto> result = new ArrayList<>();
        userGuardingRelationRepository.findUserGuardingRelationEntitiesByUserId(userId).forEach(n -> result.add(guardDayMapper.mapUserGuardingRelationEntityToDto(n)));
        return result;
    }

    public UserStatisticsDto getUserStatisticsDto(Long userId) {
        List<UserGuardingRelationDto> userRelations = readUserGuardingRelationsByUserId(userId).stream().filter(n -> n.getGuardingEnd() != null).collect(Collectors.toList());
        List<Long> guardingDays = new LinkedList<>();

        long totalSeconds = 0;
        for (UserGuardingRelationDto relation : userRelations) {
            System.out.println("Enddatum: " + relation.getGuardingEnd() + ", StartDatum: " + relation.getGuardingStart());
            long diffInMillies = Math.abs(relation.getGuardingEnd().getTime() - relation.getGuardingStart().getTime());
            totalSeconds = totalSeconds + TimeUnit.SECONDS.convert(diffInMillies, TimeUnit.MILLISECONDS);
            guardingDays.add(relation.getGuardDayId());
        }

        int presentGuardingDays = guardingDays.size();

        UserStatisticsDto statisticsDto = new UserStatisticsDto();
        statisticsDto.setDaysPresent(presentGuardingDays);
        statisticsDto.setHours(totalSeconds / 3600);
        statisticsDto.setMinutes((totalSeconds % 3600) / 60);
        statisticsDto.setSeconds(totalSeconds % 60);
        statisticsDto.setTotalSeconds(totalSeconds);
        statisticsDto.setUserId(userId);

        return statisticsDto;

    }

    public void deleteUserGuardingRelation(Long relationId) {
        userGuardingRelationRepository.deleteById(relationId);
    }

    private List<UserGuardingRelationDto> filterBookedUserGuardingRelations(List<UserGuardingRelationDto> allRelations,
                                                                            boolean booked) {

        return allRelations.stream().filter(n -> n.isBooked() == booked).collect(Collectors.toList());
    }

    public void saveGuardDaySeries(GuardDaySeriesDto guardDaySeriesDto) {

        if (guardDaySeriesDto == null || guardDaySeriesDto.noDaySet()) {
            throw new IllegalStateException("guard day may not be null or empty!");
        }

        LocalDate startDate = convertToLocalDate(guardDaySeriesDto.getStartDate());
        LocalDate endDate = convertToLocalDate(guardDaySeriesDto.getEndDate());

        List<GuardDayDto> createdGuardDays = new ArrayList<>();
        List<DayOfWeek> daysOfWeek = guardDaySeriesDto.getDaysOfWeek();

        while(startDate.isBefore(endDate)) {

            if (daysOfWeek.contains(startDate.getDayOfWeek())) {
                createdGuardDays.add(createGuardDayDtoFromSeries(startDate, guardDaySeriesDto));
            }

            startDate = startDate.plusDays(1);
        }

        for (GuardDayDto guardDayDto : createdGuardDays) {
            saveGuardDayDto(guardDayDto);
        }
    }

    private GuardDayDto createGuardDayDtoFromSeries(LocalDate localDate, GuardDaySeriesDto guardDaySeriesDto) {
        GuardDayDto guardDayDto = new GuardDayDto();

        guardDayDto.setGuardingDate(convertToDate(localDate));
        guardDayDto.setStartTime(guardDaySeriesDto.getStartTime());
        guardDayDto.setEndTime(guardDaySeriesDto.getEndTime());

        return guardDayDto;
    }

    public Date convertToDate(LocalDate dateToConvert) {
        return java.util.Date.from(dateToConvert.atStartOfDay()
                .atZone(ZoneId.systemDefault())
                .toInstant());
    }

    public LocalDate convertToLocalDate(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    @Async
    public void writeWeatherAPIResults(long guardDayId) throws InterruptedException {

        /*
        wait 1h
        read guard day
        while (guardDay is not closed)
            readWeatherApi
            writeWeatherApiResult
            wait 1h
            read guardday;
         */
        Thread.sleep(120000);
        GuardDayDto guardDayDto = readGuardDayById(guardDayId);

        while (guardDayDto.getActualEndTime() == null) {
            WeatherApiResult weatherApiResult = weatherApi.getCurrentWeatherData();
            JournalEntryDto journalEntryDtoWeather = JournalHelper.createJournalEntry(guardDayDto.getGuardDayId(), EntryType.WEATHER, null, weatherApiResult, null, null);
            guardDayDto.getJournalEntries().add(journalEntryDtoWeather);
            saveGuardDayDto(guardDayDto);
            Thread.sleep(10000);
            guardDayDto = readGuardDayById(guardDayId);
        }
    }
}
