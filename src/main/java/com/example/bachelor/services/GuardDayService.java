package com.example.bachelor.services;

import com.example.bachelor.data.dto.GuardDayDto;
import com.example.bachelor.data.dto.UserDto;
import com.example.bachelor.data.dto.UserGuardingRelationDto;
import com.example.bachelor.data.dto.UserStatisticsDto;
import com.example.bachelor.data.entities.GuardDayEntity;
import com.example.bachelor.data.entities.JournalEntryEntity;
import com.example.bachelor.data.entities.UserEntity;
import com.example.bachelor.data.entities.UserGuardingRelationEntity;
import com.example.bachelor.repositories.GuardDayRepository;
import com.example.bachelor.repositories.UserGuardingRelationRepository;
import com.example.bachelor.utility.mapper.GuardDayMapper;
import org.apache.el.lang.ELArithmetic;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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

    public GuardDayDto readGuardDayById(Long guardDayId) {
        GuardDayEntity guardDayEntity = guardDayRepository.findById(guardDayId).orElse(null);

        if (guardDayEntity == null) {
            //TODO: throw exception
        }

        return guardDayMapper.mapGuardDayEntityToDto(guardDayEntity);

    }

    public GuardDayDto readGuardDayByIdWithUsers(Long guardDayId) {
        GuardDayDto guardDayDto = readGuardDayById(guardDayId);

        guardDayDto.setAllUsers(userService.readAllUserDtos());
        List<UserGuardingRelationDto> allRelations = readUserGuardingRelations(guardDayId);
        guardDayDto.setUserGuardingRelations(allRelations.stream().filter(n -> !n.isBooked() && n.getGuardingEnd() == null).collect(Collectors.toList()));
        guardDayDto.setUserGuardingRelationsBooked(allRelations.stream().filter(n -> n.isBooked()).collect(Collectors.toList()));

        return guardDayDto;
    }

    public List<GuardDayDto> readAllGuardDays() {

        Iterable<GuardDayEntity> guardDays = guardDayRepository.findAll();
        List<GuardDayDto> guardDayDtos = new ArrayList<>();

        guardDays.forEach(g -> guardDayDtos.add(guardDayMapper.mapGuardDayEntityToDto(g)));

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

    private List<UserGuardingRelationDto> readUserGuardingRelations(Long guardDayId) {
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
}
