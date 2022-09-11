package com.example.bachelor.services;

import com.example.bachelor.data.dto.GuardDayDto;
import com.example.bachelor.data.entities.GuardDayEntity;
import com.example.bachelor.data.entities.JournalEntryEntity;
import com.example.bachelor.repositories.GuardDayRepository;
import com.example.bachelor.utility.mapper.GuardDayMapper;
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
    private GuardDayMapper guardDayMapper;

    public GuardDayDto readGuardDayById(Long guardDayId) {
        GuardDayEntity guardDayEntity = guardDayRepository.findById(guardDayId).orElse(null);

        return guardDayMapper.mapGuardDayEntityToDto(guardDayEntity);

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
}
