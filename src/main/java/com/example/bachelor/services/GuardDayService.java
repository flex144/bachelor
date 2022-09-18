package com.example.bachelor.services;

import com.example.bachelor.data.dto.GuardDayDto;
import com.example.bachelor.data.dto.UserDto;
import com.example.bachelor.data.dto.UserGuardingRelationDto;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
        guardDayDto.setUserGuardingRelations(readUserGuardingRelations(guardDayDto.getGuardDayId()));

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
}
