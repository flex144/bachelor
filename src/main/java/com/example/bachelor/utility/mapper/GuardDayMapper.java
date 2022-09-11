package com.example.bachelor.utility.mapper;

import com.example.bachelor.data.dto.GuardDayDto;
import com.example.bachelor.data.entities.GuardDayEntity;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GuardDayMapper extends BaseMapper {

    @Autowired
    JournalMapper journalMapper;

    public GuardDayDto mapGuardDayEntityToDto(GuardDayEntity guardDayEntity) {

        GuardDayDto guardDayDto = null;

        if (guardDayEntity != null) {

            setPropertyMapper(GuardDayEntity.class, GuardDayDto.class, GuardDayDto::setJournalEntries);

            guardDayDto = modelMapper.map(guardDayEntity, GuardDayDto.class);
            guardDayDto.setJournalEntries(journalMapper.mapJournalEntryEntitiesToDto(guardDayEntity.getJournalEntries()));
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
}
