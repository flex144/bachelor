package com.example.bachelor.services;

import com.example.bachelor.data.dto.GuardDayDto;
import com.example.bachelor.data.entities.GuardDayEntity;
import com.example.bachelor.data.entities.JournalEntryEntity;
import com.example.bachelor.repositories.GuardDayRepository;
import org.apache.el.lang.ELArithmetic;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GuardDayService {

    @Autowired
    private GuardDayRepository guardDayRepository;

    @Autowired
    private JournalService journalService;

    @Autowired
    private UserService userService;

    @Autowired
    private ModelMapper modelMapper;

    public GuardDayDto readGuardDayById(Long guardDayId) {
        GuardDayEntity guardDayEntity = guardDayRepository.findById(guardDayId).orElse(null);

        return mapGuardDayEntityToDto(guardDayEntity);

    }

    private GuardDayDto mapGuardDayEntityToDto(GuardDayEntity guardDayEntity) {

        GuardDayDto guardDayDto = null;

        if (guardDayEntity != null) {

            TypeMap<GuardDayEntity, GuardDayDto> propertyMapper = modelMapper.createTypeMap(GuardDayEntity.class, GuardDayDto.class);
            propertyMapper.addMappings(mapper -> mapper.skip(GuardDayDto::setJournalEntries));
            guardDayDto = modelMapper.map(guardDayEntity, GuardDayDto.class);

            guardDayDto.setJournalEntries(journalService.mapJournalEntriesToDto(guardDayEntity.getJournalEntries()));

        }

        return guardDayDto;
    }

    public GuardDayEntity saveGuardDay(GuardDayEntity guardDayEntity) {

        guardDayEntity = guardDayRepository.save(guardDayEntity);

        if (guardDayEntity.getJournalEntries() != null) {
            for (JournalEntryEntity journalEntryEntity : guardDayEntity.getJournalEntries()) {
                journalEntryEntity.setGuardDay(guardDayEntity);
                journalService.saveJournalEntry(journalEntryEntity);
            }
        }

        return guardDayEntity;
    }
}
