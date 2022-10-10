package com.example.bachelor.utility.mapper;

import com.example.bachelor.data.dto.GuardDayDto;
import com.example.bachelor.data.dto.JournalEntryDto;
import com.example.bachelor.data.dto.UserGuardingRelationDto;
import com.example.bachelor.data.entities.GuardDayEntity;
import com.example.bachelor.data.entities.UserGuardingRelationEntity;
import com.example.bachelor.services.UserService;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;

@Service
public class GuardDayMapper extends BaseMapper {

    @Autowired
    JournalMapper journalMapper;

    @Autowired
    UserService userService;

    public GuardDayDto mapGuardDayEntityToDto(GuardDayEntity guardDayEntity) {

        GuardDayDto guardDayDto = null;

        if (guardDayEntity != null) {

            setPropertyMapper(GuardDayEntity.class, GuardDayDto.class, GuardDayDto::setJournalEntries);
            setPropertyMapper(GuardDayEntity.class, GuardDayDto.class, GuardDayDto::setAllUsers);
            setPropertyMapper(GuardDayEntity.class, GuardDayDto.class, GuardDayDto::setUserGuardingRelations);
            setPropertyMapper(GuardDayEntity.class, GuardDayDto.class, GuardDayDto::setUserGuardingRelationsBooked);

            guardDayDto = modelMapper.map(guardDayEntity, GuardDayDto.class);
            guardDayDto.setJournalEntries(journalMapper.mapJournalEntryEntitiesToDto(guardDayEntity.getJournalEntries()));

            guardDayDto.getJournalEntries().sort((o1, o2) -> o2.getCreation().compareTo(o1.getCreation()));
        }

        return guardDayDto;
    }

    public GuardDayEntity mapGuardDayDtoToEntity(GuardDayDto guardDayDto) {

        GuardDayEntity guardDayEntity = null;

        if (guardDayDto != null) {

            setPropertyMapper(GuardDayDto.class, GuardDayEntity.class, GuardDayEntity::setJournalEntries);

            guardDayEntity = modelMapper.map(guardDayDto, GuardDayEntity.class);
            guardDayEntity.setJournalEntries(journalMapper.mapJournalEntryDtosToEntity(guardDayDto.getJournalEntries()));
        }

        return guardDayEntity;
    }

    public UserGuardingRelationDto mapUserGuardingRelationEntityToDto(UserGuardingRelationEntity entity) {

        UserGuardingRelationDto dto = null;

        if (entity != null) {
            dto = modelMapper.map(entity, UserGuardingRelationDto.class);
            if (dto.getUserId() != null) {
                dto.setUserDto(userService.readUserDtoById(dto.getUserId()));
            }
        }

        return dto;
    }

    public UserGuardingRelationEntity mapUserGuardingRelationDtoToEntity(UserGuardingRelationDto dto) {

        UserGuardingRelationEntity entity = null;

        if (dto != null) {

            entity = modelMapper.map(dto, UserGuardingRelationEntity.class);
        }

        return entity;
    }
}
